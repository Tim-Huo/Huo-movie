package com.stylefeng.guns.api.user;


import lombok.Data;

import java.io.Serializable;

/**
 * @description: 查询用户类
 * @author: huowencheng
 * @created: 2020/09/20 14:59
 */
@Data
public class UserInfoModel implements Serializable {

    private Integer uuid;

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private int sex;

    private String birthday;

    private String lifeState;

    private String biography;

    private String address;

    private String headAddress;

    private long beginTime;

    private long updateTime;
}