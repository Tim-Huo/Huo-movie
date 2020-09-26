package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 详细信息
 * @author: Tim·Huo
 * @created: 2020/09/25 21:07
 */
@Data
public class FilmDescVO implements Serializable {

    private String biography;

    private String filmId;
}
