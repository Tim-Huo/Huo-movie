package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 年代
 * @author: Tim·Huo
 * @created: 2020/09/24 14:44
 */
@Data
public class YearVO implements Serializable {

    private String yearId;

    private String yearName;

    private boolean isActive;
}
