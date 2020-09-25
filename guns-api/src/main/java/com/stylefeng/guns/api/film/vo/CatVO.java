package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 影片
 * @author: Tim·Huo
 * @created: 2020/09/24 14:40
 */
@Data
public class CatVO implements Serializable {

    private String catId;

    private String catName;

    private boolean isActive;
}
