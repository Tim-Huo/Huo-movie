package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.UserInfoModel;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.api.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description:
 * @author: huowencheng
 * @created: 2020/09/17 07:53
 */
@Component
@Service(interfaceClass = UserAPI.class)
public class UserServiceImpl implements UserAPI{

    @Autowired
    private MoocUserTMapper moocUserTMapper;

    /**
     * 登陆
     *
     * @auther: huowencheng
     * @param: username, password
     * @return:
     * @date: 2020/9/21 2020/9/21
     */
    @Override
    public int login(String username, String password) {

        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(username);

        //根据用户查询
        MoocUserT result = moocUserTMapper.selectOne(moocUserT);

        //用传进来的密码 与 数据库的密码做匹配
        if(result != null && result.getUuid() > 0) {
            String md5Password = MD5Util.encrypt(password);
            if(result.getUserPwd().equals(md5Password)) {
                return result.getUuid();
            }
        }

        return 0;
    }

    /**
     * 注册
     *
     * @auther: huowencheng
     * @param: UserModel
     * @return: boolean
     * @date: 2020/9/21 2020/9/21
     */
    @Override
    public boolean register(UserModel userModel) {

        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setAddress(userModel.getAddress());
        moocUserT.setUserPhone(userModel.getPhone());

        // 数据加密 【MD5混淆加密 + 盐值】-> shiro加密
        String md5Password = MD5Util.encrypt(userModel.getPassword());
        moocUserT.setUserPwd(md5Password);

        Integer integer = moocUserTMapper.insert(moocUserT);
        if(integer > 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 用户验证
     *
     * @auther: huowencheng
     * @param: username
     * @return: boolean
     * @date: 2020/9/21 2020/9/21
     */
    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<MoocUserT> entityWrapper = new EntityWrapper<>();
        //匹配username
        entityWrapper.eq("user_name", username);
        Integer result = moocUserTMapper.selectCount(entityWrapper);
        if(result != null && result > 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 根据id查询用户信息
     *
     * @auther: huowencheng
     * @param: uuid
     * @return: UserInfoModel
     * @date: 2020/9/21 2020/9/21
     */
    @Override
    public UserInfoModel getUserInfo(int uuid) {
        MoocUserT moocUserT = moocUserTMapper.selectById(uuid);
        UserInfoModel userInfoModel = do2UserInfo(moocUserT);

        return userInfoModel;
    }

    /**
     * 修改用户信息
     *
     * @auther: huowencheng
     * @param: UserInfoModel
     * @return: UserInfoModel
     * @date: 2020/9/21 2020/9/21
     */
    @Override
    public UserInfoModel udpateUserInfo(UserInfoModel userInfoModel) {
        //对象类型转换
//        BeanUtils.copyProperties(userInfoModel, moocUserT);
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setNickName(userInfoModel.getNickname());
        moocUserT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        moocUserT.setBirthday(userInfoModel.getBirthday());
        moocUserT.setBiography(userInfoModel.getBiography());
        moocUserT.setBeginTime(null);
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setUserPhone(userInfoModel.getPhone());
        moocUserT.setUserSex(userInfoModel.getSex());
        moocUserT.setUpdateTime(new Date());

        Integer integer = moocUserTMapper.updateById(moocUserT);
        //判断修改后，影响的行数
        if (integer > 0) {
            UserInfoModel userInfo = getUserInfo(moocUserT.getUuid());
            return userInfo;
        } else {
            return userInfoModel;
        }
    }

    private UserInfoModel do2UserInfo(MoocUserT moocUserT) {
        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setUuid(moocUserT.getUuid());
        userInfoModel.setHeadAddress(moocUserT.getHeadUrl());
        userInfoModel.setPhone(moocUserT.getUserPhone());
        userInfoModel.setUpdateTime(moocUserT.getUpdateTime().getTime());
        userInfoModel.setEmail(moocUserT.getEmail());
        userInfoModel.setUsername(moocUserT.getUserName());
        userInfoModel.setNickname(moocUserT.getNickName());
        userInfoModel.setLifeState("" + moocUserT.getLifeState());
        userInfoModel.setBirthday(moocUserT.getBirthday());
        userInfoModel.setAddress(moocUserT.getAddress());
        userInfoModel.setSex(moocUserT.getUserSex());
        userInfoModel.setBeginTime(moocUserT.getBeginTime().getTime());
        userInfoModel.setBiography(moocUserT.getBiography());

        return userInfoModel;
    }



}
