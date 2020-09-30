package com.stylefeng.guns.rest.common;

/**
 * @description: 将id放进线程中
 * @author: Tim_Huo
 * @created: 2020/09/20 21:32
 */
public class CurrentUser {

    /*
    * 将登陆用户绑定到ThreadLoacl中
    *
    * 线程绑定的存储空间
    * */
    private static final InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    public static void saveUserId(String userId) {
        threadLocal.set(userId);
    }

    public static String getCurrentUser() {
        return threadLocal.get();
    }

    //将用户信息放入存储空间
//    public static void setUserInfo(UserInfoModel userInfoModel) {
//        threadLocal.set(userInfoModel);
//    }
//    //将用户信息取出
//    public static UserInfoModel getCurrentUser() {
//        threadLocal.get();
//    }
}
