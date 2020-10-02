package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.OrderQueryVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.api.util.UUIDUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.persistence.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Tim_Huo
 * @created: 2020/09/29 06:20
 */
@Slf4j
@Component
@Service(interfaceClass = OrderServiceAPI.class, group = "default")
public class DefaultOrderService implements OrderServiceAPI {

    @Autowired
    private MoocOrderTMapper moocOrderTMapper;

    @Reference(interfaceClass = CinemaServiceAPI.class, check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    @Autowired
    private FTPUtil ftpUtil;

    /**
     * 验证售出的票是否为真
     *
     * @auther: Tim_Huo
     * @param: fieldId 场次编号
     * @param: seats 座位
     * @return: boolean
     * @date: 2020/9/29 6:33 上午
     */
    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
        //根据fieldId找到对应的座位位置图
        String seatPath = moocOrderTMapper.getSeatsByFieldId(fieldId);

        //读取位置图，判断seats是否为真
        String fileStrByAddress = ftpUtil.getFileStrByAddress(seatPath);

        //将fileStrByAddress转换为Json对象
        JSONObject jsonObject = JSONObject.parseObject(fileStrByAddress);
        String ids = jsonObject.get("ids").toString();

        //seats: "1,2,3"  ids："1,3,5,6,7,8,4"
        String[] idArrs = ids.split(",");
        String[] seatsArrs = seats.split(",");
        int isTrue = 0;
        //循环遍历， 查看ids中是否全部包含seats
        for(String id : idArrs) {
            for(String seat : seatsArrs) {
                if(seat.equalsIgnoreCase(id)) {
                    //每次匹配上，isTrue都加1
                    isTrue++;
                }
            }
        }
        //匹配上的数量与已售座位数一致， 则表示全部匹配成功
        if(seatsArrs.length == isTrue) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 已经销售的座位里，有没有这些座位
     *
     * @auther: Tim_Huo
     * @param: fieldId 场次编号
     * @param: seats 座位
     * @return: boolean
     * @date: 2020/9/29 7:47 上午
     */
    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id",fieldId);

        List<MoocOrderT> list = moocOrderTMapper.selectList(entityWrapper);
        String[] seatArrs = seats.split(",");
        // 有任何一个编号匹配上，则直接返回失败
        for(MoocOrderT moocOrderT : list){
            String[] ids = moocOrderT.getSeatsIds().split(",");
            for(String id : ids){
                for(String seat : seatArrs){
                    if(id.equalsIgnoreCase(seat)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 创建订单信息
     *
     * @auther: Tim_Huo
     * @param: fieldId 放映场次编号
     * @param: soldSeats 购买座位编号
     * @param: seatsName 已售座位名称
     * @param: userId 用户id
     * @return: OrderVO
     * @date: 2020/9/29 7:59 上午
     */
    @Override
    public OrderVO  saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {

        //编号
        String uuid = UUIDUtil.genUuid();

        //影片信息
        FilmInfoVO filmInfoVO = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
        Integer filmId = Integer.parseInt(filmInfoVO.getFilmId());

        //获取影院信息
        OrderQueryVO orderQueryVO = cinemaServiceAPI.getOrderNeeds(fieldId);
        Integer cinemaId = Integer.parseInt(orderQueryVO.getCinemaId());
        double filmPrice = Double.parseDouble(orderQueryVO.getFilmPrice());

        //票数  //1,2,3,4,5
        int solds = soldSeats.split(",").length;
        double totalPrice = getTotalPrice(solds, filmPrice);

        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(uuid);
        moocOrderT.setSeatsName(seatsName);
        moocOrderT.setSeatsIds(soldSeats);
        moocOrderT.setOrderUser(userId);
        moocOrderT.setOrderPrice(totalPrice);
        moocOrderT.setFilmPrice(filmPrice);
        moocOrderT.setFilmId(filmId);
        moocOrderT.setFieldId(fieldId);
        moocOrderT.setCinemaId(cinemaId);

        Integer integer = moocOrderTMapper.insert(moocOrderT);
        if(integer > 0) {
            //返回查询结果
            OrderVO orderVO = moocOrderTMapper.getOrderInfoById(uuid);
            if(orderVO == null || orderVO.getOrderId() == null) {
                log.error("订单信息查询失败，订单编号为{}", uuid);
                return null;
            } else {
                return orderVO;
            }
        } else {
            //插入出错
            log.error("订单插入失败");
            return null;
        }

    }

    /**
     * @auther: Tim_Huo
     * @param: solds 票数
     * @param: filmPrice 单个电影票的票价
     * @return: double
     * @date: 2020/9/29 10:08 上午
     */
    private double getTotalPrice(int solds, double filmPrice) {
        BigDecimal soldDesi = new BigDecimal(solds);
        BigDecimal filmPriceDesi = new BigDecimal(filmPrice);

        //将票数与单个票价格相乘
        BigDecimal result = soldDesi.multiply(filmPriceDesi);

        //将价格四舍五入 , 取小数点后两位
        BigDecimal bigDecimal = result.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    /**
     * 使用当前登陆人获取已经购买的订单
     *
     * @auther: Tim_Huo
     * @param: userId
     * @param: page
     * @return: OrderVO
     * @date: 2020/9/29 11:04 上午
     */
    @Override
    public Page<OrderVO> getOrderByUserId(Integer userId, Page<OrderVO> page) {
        Page<OrderVO> result = new Page<>();
        if(userId == null){
            log.error("订单查询业务失败，用户编号未传入");
            return null;
        }else{
            List<OrderVO> ordersByUserId = moocOrderTMapper.getOrdersByUserId(userId,page);
            if(ordersByUserId==null && ordersByUserId.size()==0){
                result.setTotal(0);
                result.setRecords(new ArrayList<>());
                return result;
            }else{
                // 获取订单总数
                EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("order_user",userId);
                Integer counts = moocOrderTMapper.selectCount(entityWrapper);
                // 将结果放入Page
                result.setTotal(counts);
                result.setRecords(ordersByUserId);

                return result;
            }
        }
    }

    /**
     * 根据放映场次 获取所有已经销售的座位编号
     *
     * @auther: Tim_Huo
     * @param: fieldId 场次编号
     * @return: String
     * @date: 2020/9/29 11:04 上午
     */
    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {

        if(fieldId == null) {
            log.error("查询已售座位错误，未传入任何场次编号");
            return "";
        } else {
            String result = moocOrderTMapper.getSoldSeatsByFieldId(fieldId);
            return result;
        }
    }

    /**
     * 根据订单编号获取订单信息
     *
     * @auther: Tim_Huo
     * @param: orderId 订单编号
     * @return: OrderVO
     * @date: 2020/9/30 5:25 下午
     */
    @Override
    public OrderVO getOrderInfoById(String orderId) {
        OrderVO orderInfoById = moocOrderTMapper.getOrderInfoById(orderId);

        return orderInfoById;
    }

    /**
     * 成功订单
     *
     * @auther: Tim_Huo
     * @param: orderId 订单编号
     * @return: boolean
     * @date: 2020/9/30 5:33 下午
     */
    @Override
    public boolean paySuccess(String orderId) {
        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(orderId);
        moocOrderT.setOrderStatus(1);

        Integer integer = moocOrderTMapper.updateById(moocOrderT);
        if(integer>=1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 失败订单
     *
     * @auther: Tim_Huo
     * @param: orderId 订单编号
     * @return: boolean
     * @date: 2020/9/30 5:35 下午
     */
    @Override
    public boolean payFail(String orderId) {
        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(orderId);
        moocOrderT.setOrderStatus(2);

        Integer integer = moocOrderTMapper.updateById(moocOrderT);
        if(integer>=1){
            return true;
        }else{
            return false;
        }
    }


}
