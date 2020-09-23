package com.stylefeng.guns.api.user.user;

import com.stylefeng.guns.api.user.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.user.vo.UserModel;

public interface UserAPI {

    int login(String username, String password);

    //注册
    boolean register(UserModel userModel);

    //检查账号
    boolean checkUsername(String username);

    //根据id查询，可显示打的字段
    UserInfoModel getUserInfo(int uuid);

    UserInfoModel udpateUserInfo(UserInfoModel userInfoModel);
}
