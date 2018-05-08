package com.invest.ivuser.biz.service;

import com.invest.ivuser.model.entity.User;
import com.invest.ivuser.model.param.*;
import com.invest.ivuser.model.result.UserResult;

import java.util.Map;

/**
 * Created by xugang on 2017/7/28.
 */
public interface UserService {

    UserResult verifyUniWasLoggedin(String token, Map<String, String> params);

    /**
     * 发送短信验证码
     *
     * @param checkCodeParam
     * @return
     */
    UserResult sendSmsCheckCode(CheckCodeParam checkCodeParam);

    /**
     * 用户注册
     *
     * @param registerParam
     * @return
     */
    UserResult register(RegisterParam registerParam);

    /**
     * 用户登录
     *
     * @param loginParam
     * @return
     */
    UserResult login(LoginParam loginParam);

    /**
     * 用户退出
     *
     * @param logoutParam
     * @return
     */
    UserResult logout(LogoutParam logoutParam);

    /**
     * 重置密码
     *
     * @param resetPasswordParam
     * @return
     */
    UserResult resetPassword(ResetPasswordParam resetPasswordParam);

    /**
     * 忘记密码 找回
     *
     * @param refindPasswordParam
     * @return
     */
    UserResult refindPassword(RefindPasswordParam refindPasswordParam);

    /**
     * 获取用户基本信息
     * @param userId
     * @return
     */
    UserResult getUserBaseInfo(Long userId);

    /**
     * 获取用户基本信息
     * @param mobile
     * @return
     */
    UserResult getUserBaseInfo(String mobile);

    /**
     * 修改用户个人信息
     */
    UserResult modifyUser(User user);

    /**
     * 修改用户头像
     * @param changeHeadImgParam
     * @return
     */
    UserResult changeHeadImg(ChangeHeadImgParam changeHeadImgParam);

    /**
     * 自动登录 key 30分钟过期
     * @param appid
     * @param mobile
     */
    UserResult getLoginKey(String appid, String mobile);

    /**
     * 自动登录
     * @param loginKey
     */
    UserResult autoLogin(String loginKey);
}
