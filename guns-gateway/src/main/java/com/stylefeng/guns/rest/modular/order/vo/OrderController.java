package com.stylefeng.guns.rest.modular.order.vo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.plugins.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stylefeng.guns.api.alipay.AliPayServiceAPI;
import com.stylefeng.guns.api.alipay.vo.AliPayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AliPayResultVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.api.util.TokenBucket;
import com.stylefeng.guns.api.util.ToolUtil;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 订单模块
 * @author: Tim_Huo
 * @created: 2020/09/28 16:52
 */
@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {

    private static TokenBucket tokenBucket = new TokenBucket();

    private static final String IMG_PRE="http://img.meetingshop.cn/";

    @Reference(
            interfaceClass = OrderServiceAPI.class,
            check = false,
            group = "group2018"
    )
    private OrderServiceAPI orderServiceAPI;

    @Reference(
            interfaceClass = OrderServiceAPI.class,
            check = false,
            group = "group2017"
    )
    private OrderServiceAPI orderServiceAPI2017;

    public ResponseVo error(Integer fieldId, String soldSeats, String seatsName) {
        return ResponseVo.serviceFail("抱歉，下单的人太多了，请稍后重试");
    }

    @Reference(interfaceClass = AliPayServiceAPI.class,check = false)
    private AliPayServiceAPI aliPayServiceAPI;

    /**
     * 用户下单购票接口
     *
     * @auther: Tim_Huo
     * @param: fieldId 场次编号
     * @param: soldSeats 购买座位的编号
     * @param: seatsName 购买座位的名称
     * @return: ResponseVo
     * @date: 2020/9/28 4:58 下午
     */
    @HystrixCommand(fallbackMethod = "error", commandProperties = {
            @HystrixProperty(name="execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"),
                    @HystrixProperty(name = "maxQueueSize", value = "10"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "8"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1500")
            })
    @PostMapping("buyTickets")
    public ResponseVo buyTickets(Integer fieldId, String soldSeats, String seatsName) {

        try {
            //tokenBucket限流：令牌桶
            if(tokenBucket.getToken()) {
                //验证售出的票是否为真
                boolean isTrue = orderServiceAPI.isTrueSeats(fieldId + "", soldSeats);

                //已经销售的座位里，有没有这些座位
                boolean isNotSold = orderServiceAPI.isNotSoldSeats(fieldId + "", soldSeats);

                if(isTrue && isNotSold) {
                    //创建订单信息（需要获取登陆人）
                    String userId = CurrentUser.getCurrentUser();
                    if(userId == null || userId.trim().length() == 0) {
                        return ResponseVo.serviceFail("用户未登陆");
                    }
                    OrderVO orderVO = orderServiceAPI.saveOrderInfo(fieldId, soldSeats, seatsName, Integer.parseInt(userId));
                    if(orderVO == null) {
                        log.error("购票未成功");
                        return ResponseVo.serviceFail("购票业务异常");
                    } else {
                        return ResponseVo.success(orderVO);
                    }
                } else {
                    return ResponseVo.serviceFail("订单中的座位编号有问题");
                }
            } else {
                return ResponseVo.serviceFail("购票人数过多，请稍后在试");
            }

        } catch (Exception e) {
            log.error("购票业务异常", e);
            return ResponseVo.serviceFail("购票业务异常");
        }


    }

    /**
     * 获取用户订单信息接口
     *
     * @auther: Tim_Huo
     * @param: nowPage 当前页
     * @param: pageSize 每页多少条
     * @return: ResponseVo
     * @date: 2020/9/28 4:59 下午
     */
    @PostMapping("getOrderInfo")
    public ResponseVo getOrderInfo(@RequestParam(name = "nowPage", required = false, defaultValue = "1") Integer nowPage,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize) {

        //获取当前登陆人信息
        String userId = CurrentUser.getCurrentUser();

        //使用当前登陆人获取已经购买的订单
        Page<OrderVO> page = new Page<>(nowPage, pageSize);
        if(userId != null && userId.trim().length() > 0) {

            //2018数据
            Page<OrderVO> result = orderServiceAPI.getOrderByUserId(Integer.parseInt(userId), page);

            //2017数据
            Page<OrderVO> result2017 = orderServiceAPI2017.getOrderByUserId(Integer.parseInt(userId), page);

            //合并结果
            int totalPages = (int)(result.getPages() + result2017.getPages());
            List<OrderVO> orderVOList = new ArrayList<>();
            orderVOList.addAll(result.getRecords());
            orderVOList.addAll(result2017.getRecords());

            return ResponseVo.success(nowPage, totalPages, "", orderVOList);
        }

         return null;
    }

    /**
     * 获取支付二维码
     *
     * @auther: Tim_Huo
     * @param: orderId 订单编号
     * @return: ResponseVO
     * @date: 2020/9/30 4:43 下午
     */
    @RequestMapping(value = "getPayInfo",method = RequestMethod.POST)
    public ResponseVo getPayInfo(@RequestParam("orderId") String orderId){
        // 获取当前登陆人的信息
        String userId = CurrentUser.getCurrentUser();
        if(userId==null || userId.trim().length()==0){
            return ResponseVo.serviceFail("抱歉，用户未登陆");
        }
        // 订单二维码返回结果
        AliPayInfoVO aliPayInfoVO = aliPayServiceAPI.getQRCode(orderId);
        return ResponseVo.success(IMG_PRE,aliPayInfoVO);
    }

    /**
     * 获取订支付信息
     *
     * @auther: Tim_Huo
     * @param: orderId 订单编号
     * @param: tryNums 重试次数
     * @return: ResponseVO
     * @date: 2020/9/30 4:46 下午
     */
    @RequestMapping(value = "getPayResult",method = RequestMethod.POST)
    public ResponseVo getPayResult(
            @RequestParam("orderId") String orderId,
            @RequestParam(name="tryNums",required = false,defaultValue = "1") Integer tryNums){
        // 获取当前登陆人的信息
        String userId = CurrentUser.getCurrentUser();
        if(userId==null || userId.trim().length()==0){
            return ResponseVo.serviceFail("抱歉，用户未登陆");
        }

        // 将当前登陆人的信息传递给后端
        RpcContext.getContext().setAttachment( "userId",userId);

        // 判断是否支付超时
        if(tryNums>=4){
            return ResponseVo  .serviceFail("订单支付失败，请稍后重试");
        }else{
            AliPayResultVO aliPayResultVO = aliPayServiceAPI.getOrderStatus(orderId);
            if(aliPayResultVO == null || ToolUtil.isEmpty(aliPayResultVO.getOrderId())){
                AliPayResultVO serviceFailVO = new AliPayResultVO();
                serviceFailVO.setOrderId(orderId);
                serviceFailVO.setOrderStatus(0);
                serviceFailVO.setOrderMsg("支付不成功");
                return ResponseVo.success(serviceFailVO);
            }
            return ResponseVo.success(aliPayResultVO);
        }
    }

}
