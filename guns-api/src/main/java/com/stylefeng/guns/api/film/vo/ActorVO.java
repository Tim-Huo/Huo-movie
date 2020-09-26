package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Tim·Huo
 * @created: 2020/09/25 21:12
 */
@Data
public class ActorVO implements Serializable {

    private String imgAddress;

    private String directorName;

    private String roleName;
}
