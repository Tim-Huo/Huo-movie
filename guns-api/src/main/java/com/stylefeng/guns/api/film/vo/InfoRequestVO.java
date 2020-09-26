package com.stylefeng.guns.api.film.vo;

import lombok.Data;

/**
 * @author: Tim·Huo
 * @created: 2020/09/25 22:36
 */
@Data
public class InfoRequestVO {

    private String biography;

    private ActorRequestVO actors;

    private ImgVO imgs;

    private String filmId;
}
