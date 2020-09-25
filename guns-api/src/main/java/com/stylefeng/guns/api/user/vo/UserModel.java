package com.stylefeng.guns.api.user.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Tim·Huo
 * @created: 2020/09/20 14:42
 */
@Data
//注册使用类
public class UserModel implements Serializable {

    private String username;

    private String password;

    private String email;

    private String phone;

    private String address;

}
