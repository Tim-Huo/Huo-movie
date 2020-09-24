package com.stylefeng.guns.api.user.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 片源
 * @author: Tim·Huo
 * @created: 2020/09/24 14:43
 */
@Data
public class SourceVO implements Serializable {

    private String sourceId;

    private String sourceName;

    private boolean isActive;
}
