package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AliPayInfoVO implements Serializable {

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 图片地址
     */
    private String QRCodeAddress;

}
