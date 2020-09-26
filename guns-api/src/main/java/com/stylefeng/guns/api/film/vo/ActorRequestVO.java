package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Tim·Huo
 * @created: 2020/09/25 22:32
 */
@Data
public class ActorRequestVO {

    private ActorVO director;

    private List<ActorVO> actors;
}
