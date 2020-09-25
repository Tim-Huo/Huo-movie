package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 热片
 * @author: Tim·Huo
 * @created: 2020/09/23 16:53
 */

@Data
public class FilmVO implements Serializable {

    private int filmNum;

    private List<FilmInfo> filmInfo;

    private int TotalPage;

    private int nowPage;
}
