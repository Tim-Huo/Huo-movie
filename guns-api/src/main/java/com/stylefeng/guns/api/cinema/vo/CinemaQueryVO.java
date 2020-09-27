package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询实体
 *
 * @auther: Tim·Huo
 * @date: 2020/9/26 5:49 下午
 */
@Data
public class CinemaQueryVO implements Serializable {

    /**
     * 影院编号
     */
    private Integer brandId=99;
    /**
     * 影厅类型
     */
    private Integer districtId=99;
    /**
     * 行政区编号
     */
    private Integer hallType=99;
    /**
     * 每页条数
     */
    private Integer pageSize=12;
    /**
     * 当前页数
     */
    private Integer nowPage=1;

}
