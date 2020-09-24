package com.stylefeng.guns.api.user.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 电影信息
 * @author: Tim·Huo
 * @created: 2020/09/23 16:55
 */
@Data
public class FilmInfo implements Serializable {

    private String filmId;

    private int filmType;

    private String imgAddress;

    private String filmName;

    private String filmScore;

    private int expectNum;      //人数

    private String showTime;

    private int boxNum;

    private String score;       //分数
}
