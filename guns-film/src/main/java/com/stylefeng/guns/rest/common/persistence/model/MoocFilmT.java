package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 影片主表
 * </p>
 *
 * @author Tim﹒Huo
 * @since 2020-09-23
 */
@Data
@TableName("mooc_film_t")
public class MoocFilmT extends Model<MoocFilmT> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    @TableId(value = "UUID", type = IdType.AUTO)
    private Integer uuid;
    /**
     * 影片名称
     */
    @TableField("film_name")
    private String filmName;
    /**
     * 片源类型: 0-2D,1-3D,2-3DIMAX,4-无
     */
    @TableField("film_type")
    private Integer filmType;
    /**
     * 影片主图地址
     */
    @TableField("img_address")
    private String imgAddress;
    /**
     * 影片评分
     */
    @TableField("film_score")
    private String filmScore;
    /**
     * 影片预售数量
     */
    @TableField("film_preSaleNum")
    private Integer filmPresalenum;
    /**
     * 影片票房：每日更新，以万为单位
     */
    @TableField("film_box_office")
    private Integer filmBoxOffice;
    /**
     * 影片片源，参照片源字典表
     */
    @TableField("film_source")
    private Integer filmSource;
    /**
     * 影片分类，参照分类表,多个分类以#分割
     */
    @TableField("film_cats")
    private String filmCats;
    /**
     * 影片区域，参照区域表
     */
    @TableField("film_area")
    private Integer filmArea;
    /**
     * 影片上映年代，参照年代表
     */
    @TableField("film_date")
    private Integer filmDate;
    /**
     * 影片上映时间
     */
    @TableField("film_time")
    private Date filmTime;
    /**
     * 影片状态,1-正在热映，2-即将上映，3-经典影片
     */
    @TableField("film_status")
    private Integer filmStatus;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
