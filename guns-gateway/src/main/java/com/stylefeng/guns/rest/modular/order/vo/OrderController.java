package com.stylefeng.guns.rest.modular.order.vo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 订单模块
 * @author: Tim_Huo
 * @created: 2020/09/28 16:52
 */
@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {

    @Reference(interfaceClass = OrderServiceAPI.class, check = false)
    private OrderServiceAPI orderServiceAPI;

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
    @PostMapping("buyTickets")
    public ResponseVo buyTickets(Integer fieldId, String soldSeats, String seatsName) {

        try {
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
            Page<OrderVO> result = orderServiceAPI.getOrderByUserId(Integer.parseInt(userId), page);

            return ResponseVo.success(nowPage, (int)result.getPages(), "", result.getRecords());
        }

         return null;
    }


}
