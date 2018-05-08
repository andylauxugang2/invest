package com.invest.ivppad.biz.service.ppdopenapi.impl;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivppad.biz.manager.PPDOpenApiAuthManager;
import com.invest.ivppad.biz.manager.PPDOpenApiUserManager;
import com.invest.ivppad.biz.service.ppdopenapi.AuthService;
import com.invest.ivppad.common.exception.PPDOpenApiInvokeException;
import com.invest.ivppad.model.PPDUserAccessToken;
import com.invest.ivppad.model.http.request.PPDOpenApiAuthorizeRequest;
import com.invest.ivppad.model.http.request.PPDOpenApiQueryUserNameRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiAuthResponse;
import com.invest.ivppad.model.http.response.PPDOpenApiQueryUserNameResponse;
import com.invest.ivppad.model.result.PPDOpenApiUserAuthResult;
import com.invest.ivuser.biz.service.BlackListService;
import com.invest.ivuser.biz.service.UserThirdService;
import com.invest.ivuser.model.entity.BlackListThird;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * Created by xugang on 2017/8/1.
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private PPDOpenApiAuthManager openApiAuthManager;

    @Autowired
    private PPDOpenApiUserManager openApiUserManager;

    @Autowired
    private UserThirdService userThirdService;

    @Resource
    protected BlackListService blackListService;

    @Override
    public String getUserAuthUrl(String returnUrl) {
        String url = openApiAuthManager.getUserAuthUrl(returnUrl);
        logger.info("获取用户授权url成功,url={}", url);
        return url;
    }

    @Override
    public PPDOpenApiUserAuthResult authorize(String code, Long userId) {
        PPDOpenApiUserAuthResult result = new PPDOpenApiUserAuthResult();
        PPDOpenApiAuthorizeRequest request = new PPDOpenApiAuthorizeRequest();
        request.setCode(code);
        request.setAppID(openApiAuthManager.getAppid());

        String userName = null;
        try {
            //获取令牌
            PPDOpenApiAuthResponse response = openApiAuthManager.getUserAuthInfo(request);
            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                logger.error("获取用户授权AccessToken失败,code={},response={}", code, response);
                return result;
            }
            String accessToken = response.getAccessToken();
            result.setAccessToken(accessToken);
            result.setOpenID(response.getOpenID());

            //根据token openID 获取ppd用户信息
            PPDOpenApiQueryUserNameRequest userNameRequest = new PPDOpenApiQueryUserNameRequest();
            userNameRequest.setOpenID(response.getOpenID());
            PPDOpenApiQueryUserNameResponse userNameResponse = openApiUserManager.getUserName(userNameRequest);
            if (!userNameResponse.success()) {
                logger.error("获取PPD-UserName失败,openID={},response={}", response.getOpenID(), userNameResponse);
                return result;
            }
            userName = userNameResponse.getUserName();
            result.setUserName(userName);

            //保存username和accessToken
            PPDUserAccessToken tokenParam = new PPDUserAccessToken();
            tokenParam.setAccessToken(accessToken);
            tokenParam.setExpiresIn(response.getExpiresIn());
            tokenParam.setRefreshToken(response.getRefreshToken());

            openApiUserManager.saveUserNameAccessToken(userName, tokenParam);

            //保存系统用户和ppd用户名关系
            UserThirdBindInfo userThirdBindInfo = new UserThirdBindInfo();
            userThirdBindInfo.setUserId(userId);
            userThirdBindInfo.setThirdUserUUID(userName);
            userThirdBindInfo.setExpiredTime(DateUtil.dateAdd(userThirdBindInfo.getCreateTime(), response.getExpiresIn(), Calendar.SECOND));
            userThirdBindInfo.setAccessToken(accessToken);
            userThirdBindInfo.setRefreshToken(response.getRefreshToken());
            userThirdBindInfo.setOpenID(response.getOpenID());
            userThirdService.addUserThirdBindInfo(userThirdBindInfo);

            //出狱黑名单
            Result deleteResult = blackListService.removeBlackList(BlackListThird.TYPE_THIRD_USERNAME, userName);
            logger.info("授权出狱黑名单完毕,userName={},msg={}", userName, deleteResult.getErrorMsg());
        } catch (PPDOpenApiInvokeException e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("用户授权URL回调处理失败,userName=" + userName, e);
        }

        return result;
    }

    @Override
    public boolean delPPDAccessToken(String username) {
        return openApiUserManager.delUserNameAccessToken(username);
    }
}
