package com.stylefeng.guns.api.alipay;

import com.stylefeng.guns.api.alipay.vo.AliPayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AliPayResultVO;

public interface AliPayServiceAPI {

    /**
     * 获取订单编号与二维码
     *
     * @auther: Tim_Huo
     * @param: orderId
     * @return: AliPayInfoVO
     * @date: 2020/9/30 4:53 下午
     */
    AliPayInfoVO getQRCode(String orderId);

    /**
     * 获取支付结果
     *
     * @auther: Tim_Huo
     * @param: orderId
     * @return: AliPayResultVO
     * @date: 2020/9/30 4:59 下午
     */
    AliPayResultVO getOrderStatus(String orderId);

}
