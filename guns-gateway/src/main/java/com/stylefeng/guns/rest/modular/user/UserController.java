package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.UserInfoModel;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 用户模块
 * @author: Tim·Huo
 * @created: 2020/09/21 21:33
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;

    /**
     * 注册接口
     *
     * @auther: Tim·Huo
     * @param: userModer
     * @return: ResponseVO
     * @date: 2020/9/21 2020/9/21
     */
    @PostMapping("register")
    public ResponseVO register(UserModel userModel) {

        if(userModel.getUsername() == null || userModel.getUsername().trim().length() == 0) {
            return ResponseVO.serviceFail("用户名不能为空");
        }
        if(userModel.getPassword() == null || userModel.getPassword().trim().length() == 0) {
            return ResponseVO.serviceFail("密码不能为空");
        }

        //注册
        boolean isSuccess = userAPI.register(userModel);
        if(isSuccess) {
            return ResponseVO.success("注册成功");
        } else {
            return ResponseVO.serviceFail("注册失败");
        }
    }

    /**
     * 用户验证(检查用户是否存在）
     *
     * @auther: Tim·Huo
     * @param: username
     * @return: ResponseVO
     * @date: 2020/9/22 2020/9/22
     */
    @PostMapping("check")
    public ResponseVO check(String username) {
        if(username != null && username.trim().length() > 0) {
            //返回true，表示用户名可用
            boolean notExists = userAPI.checkUsername(username);
            if(notExists) {
                return ResponseVO.success("用户名不存在");
            } else {
                return ResponseVO.serviceFail("用户名已存在");
            }
        } else {
            return ResponseVO.serviceFail("用户名不能为空");
        }
    }

    /**
     * 登出
     * 
     * @auther: Tim·Huo
     * @return: ResponseVO
     * @date: 2020/9/22 2020/9/22
     */
    @GetMapping("logout")
    public ResponseVO logout() {

        /*
        * 分析：
        *   1、前端存储JWT 【有效期：7天】 问题：JWT刷新【如：7天之内改了密码】
        *   2、服务器端存储用户信息（30分钟），30分钟没有操作，表示过期
        *   3、使用JWT里的UserId为key，查找活跃用户，超过30分中都视为过期
        * 退出：
        *   1、前端删除掉Jwt
        *   2、后端服务器删除活跃用户缓存（例如：redis）
        * 此实现：
        *   1、前端删除掉JWT
        * */
        return ResponseVO.success("用户退出成功");
    }

    /**
     * 获取用户信息
     *
     * @auther: Tim·Huo
     * @param:
     * @return: ResponseVO
     * @date: 2020/9/22 2020/9/22
     */
    @GetMapping("getUserInfo")
    public ResponseVO getUserInfo() {

        //获取当前用户
        String userId = CurrentUser.getCurrentUser();
        if(userId != null && userId.trim().length() > 0) {
            int uuid = Integer.parseInt(userId);
            UserInfoModel userInfo = userAPI.getUserInfo(uuid);
            if(userInfo != null) {
                return ResponseVO.success(userInfo);
            } else {
                return ResponseVO.serviceFail("用户信息查询失败");
            }
        } else {
            return ResponseVO.serviceFail("用户未登陆");
        }
    }

    /**
     *  修改用户信息
     *
     * @auther: Tim·Huo
     * @param: UserInfoModel
     * @return: ResponseVO
     * @date: 2020/9/22 2020/9/22
     */
    @PostMapping("updateUserInfo")
    public ResponseVO updateUserInfo(UserInfoModel userInfoModel) {

        //获取当前用户
        String userId = CurrentUser.getCurrentUser();
        if(userId != null && userId.trim().length() > 0) {
            int uuid = Integer.parseInt(userId);
            if(uuid != userInfoModel.getUuid()) {
                return ResponseVO.serviceFail("请修改您个人的用户信息");
            }

            UserInfoModel userInfo = userAPI.udpateUserInfo(userInfoModel);
            if(userInfo != null) {
                return ResponseVO.success(userInfo);
            } else {
                return ResponseVO.serviceFail("用户信息修改失败");
            }
        } else {
            return ResponseVO.serviceFail("用户未登陆");
        }
    }

}
