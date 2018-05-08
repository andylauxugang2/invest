package com.invest.ivppad.biz.manager;

import com.invest.ivppad.common.exception.PPDOpenApiInvokeException;
import com.invest.ivppad.model.http.request.PPDOpenApiAuthorizeRequest;
import com.invest.ivppad.model.http.request.PPDOpenApiRefreshTokenRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 授权相关 底层工具
 * Created by xugang on 2017/8/1.
 */
@Component
public class PPDOpenApiAuthManager extends PPDOpenApiManagerBase {
    private static final Logger logger = LoggerFactory.getLogger(PPDOpenApiAuthManager.class);

    //用户授权url
    @Value(value = "${openapi.auth.userAuthUrl}")
    private String userAuthUrl;

    //获取AccessToken url
    @Value(value = "${openapi.auth.holdAccessTokenUrl}")
    private String holdAccessTokenUrl;

    @Value(value = "${openapi.auth.refreshTokenUrl}")
    private String refreshTokenUrl;

    public String getUserAuthUrl(String returnUrl) {
        return String.format(this.userAuthUrl, super.getAppid(), returnUrl);
    }

    public PPDOpenApiAuthResponse getUserAuthInfo(PPDOpenApiAuthorizeRequest request) {
        long start = System.currentTimeMillis();
        String code = request.getCode();
        String appID = request.getAppID();

        Map<String, Object> params = new HashMap<>();
        params.put(PPDOpenApiAuthorizeRequest.PARAM_NAME_CODE, code);
        params.put(PPDOpenApiAuthorizeRequest.PARAM_NAME_APPID, appID);

        try {
            //TODO log aop
            // logger.info("开始调用用户授权接口,请求参数={}", JSON.toJSONString(params));
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiAuthResponse>() {
            };
            ResponseEntity<PPDOpenApiAuthResponse> responseEntity = callRetry(holdAccessTokenUrl, params, HttpMethod.POST, responseType);

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("调用获取授权AccessToken接口[" + holdAccessTokenUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiAuthResponse response = responseEntity.getBody();
            if (!response.success()) {
                logger.error("调用获取授权AccessToken接口[{}]返回失败,msg={}", holdAccessTokenUrl, response.getErrMsg());
                return response;
            }

            logger.info("调用获取授权AccessToken接口成功,AccessToken={},openID={}", response.getAccessToken(), response.getOpenID());

            return response;
        } catch (Exception e) {
            logger.error("调用获取授权AccessToken接口发生异常,code=" + code, e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        } finally {
            logger.error("获取用户授权AccessToken耗时统计:{}", System.currentTimeMillis() - start);
        }
    }

    public PPDOpenApiAuthResponse refreshToken(String username, PPDOpenApiRefreshTokenRequest request) {
        String appID = request.getAppID();
        String openID = request.getOpenID();
        String refreshToken = request.getRefreshToken();

        Map<String, Object> params = new HashMap<>();
        params.put(PPDOpenApiRefreshTokenRequest.PARAM_NAME_APPID, appID);
        params.put(PPDOpenApiRefreshTokenRequest.PARAM_NAME_OPENID, openID);
        params.put(PPDOpenApiRefreshTokenRequest.PARAM_NAME_REFRESHTOKEN, refreshToken);

        try {
            // logger.info("开始调用用户授权接口,请求参数={}", JSON.toJSONString(params));
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiAuthResponse>() {
            };
            ResponseEntity<PPDOpenApiAuthResponse> responseEntity = callRetry(refreshTokenUrl, params, HttpMethod.POST, responseType);

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("刷新授权AccessToken接口[" + refreshTokenUrl + "]状态返回非法 statusCode=" + statusCode + ",username=" + username);
            }

            PPDOpenApiAuthResponse response = responseEntity.getBody();
            if (!response.success()) {
                logger.error("刷新授权AccessToken接口[{}]返回失败,username={},msg={}", refreshTokenUrl, username, response.getErrMsg());
                return response;
            }

            logger.info("刷新授权AccessToken接口成功,username={},AccessToken={},openID={}", username, response.getAccessToken(), response.getOpenID());

            return response;
        } catch (Exception e) {
            logger.error("刷新授权AccessToken接口发生异常,openID=" + openID + ",username=" + username, e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        }
    }
}
