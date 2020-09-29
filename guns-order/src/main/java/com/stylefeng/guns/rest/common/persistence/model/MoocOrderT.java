package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author jiangzh
 * @since 2018-09-20
 */
@Data
@TableName("mooc_order_t")
public class MoocOrderT extends Model<MoocOrderT> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    @TableField("UUID")
    private String uuid;
    /**
     * 影院编号
     */
    @TableField("cinema_id")
    private Integer cinemaId;
    /**
     * 放映场次编号
     */
    @TableField("field_id")
    private Integer fieldId;
    /**
     * 电影编号
     */
    @TableField("film_id")
    private Integer filmId;
    /**
     * 已售座位编号
     */
    @TableField("seats_ids")
    private String seatsIds;
    /**
     * 已售座位名称
     */
    @TableField("seats_name")
    private String seatsName;
    /**
     * 影片售价
     */
    @TableField("film_price")
    private Double filmPrice;
    /**
     * 订单总金额
     */
    @TableField("order_price")
    private Double orderPrice;
    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;
    /**
     * 下单人
     */
    @TableField("order_user")
    private Integer orderUser;
    /**
     * 0-待支付,1-已支付,2-已关闭
     */
    @TableField("order_status")
    private Integer orderStatus;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
