package com.invest.ivppad.biz.service.ppdopenapi;

import com.invest.ivppad.model.result.PPDOpenApiUserAuthResult;

/**
 * 授权相关service
 * 用户授权:
 *      为保证用户数据的安全与隐私，如果开发者应用需要获取用户隐私数据，必须要获得用户的授权,即获取访问用户数据的授权令牌 Access Token。
 *      如需要,可以引导用户完成使用拍拍贷帐号“登录授权”的流程。
 *      使用拍拍贷帐号“登录授权”的流程,采用国际通用的OAuth2.0标准协议作为用户身份验证与授权协议，支持网站、M站授权。
 *          拼接授权url -> 引导用户登录拍拍贷授权 -> 获取code -> 获取access_token
 *          授权码code作为换取AccessToken的票据,每次用户授权带上的授权码code将不一样，授权码code只能使用一次
 *
 * Created by xugang on 2017/8/1.
 */
public interface AuthService {

    /**
     * 获取用户授权URL
     * @param returnUrl 回调地址
     * @return
     */
    String getUserAuthUrl(String returnUrl);

    /**
     * 用户授权URL回调处理
     * 根据api回调获取的授权码进一步获取AccessToken
     * 保存AccessToken
     * 根据accessToken 获取 用户隐私信息并保存到本地
     * @param code
     * @return
     */
    PPDOpenApiUserAuthResult authorize(String code, Long userId);

    boolean delPPDAccessToken(String username);
}
