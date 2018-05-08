package com.invest.ivppad.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivppad.base.property.PropertyObject;
import com.invest.ivppad.base.property.ValueTypeEnum;
import com.invest.ivppad.common.exception.PPDOpenApiInvokeException;
import com.invest.ivppad.datacache.PPDAccessTokenDataCache;
import com.invest.ivppad.model.PPDUserAccessToken;
import com.invest.ivppad.model.http.request.PPDOpenApiQueryUserNameRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiQueryUserBalanceResponse;
import com.invest.ivppad.model.http.response.PPDOpenApiQueryUserNameResponse;
import com.invest.ivppad.model.protobuf.PPDUserAccessTokenProtos;
import com.invest.ivppad.util.KeyVersionUtils;
import com.invest.ivppad.util.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 授权相关 底层工具
 * Created by xugang on 2017/8/1.
 */
@Component
public class PPDOpenApiUserManager extends PPDOpenApiManagerBase {
    private static final Logger logger = LoggerFactory.getLogger(PPDOpenApiUserManager.class);

    //根据OpenID查询用户信息 url
    @Value(value = "${openapi.user.queryUserNameByOpenIDUrl}")
    private String queryUserNameByOpenIDUrl;

    //查询用户账户余额 url
    @Value(value = "${openapi.user.balance.QueryBalanceUrl}")
    private String queryBalanceUrl;

    @Autowired
    protected PPDAccessTokenDataCache ppdAccessTokenDataCache;

    /**
     * 查询 ppd 用户名
     *
     * @param request
     * @return
     */
    public PPDOpenApiQueryUserNameResponse getUserName(PPDOpenApiQueryUserNameRequest request) {
        StopWatch stopWatch = new StopWatch("根据OpenID查询用户信息耗时统计");
        String openID = request.getOpenID();

        try {
            HttpHeaders headers = getRequestHeadersCommon(null, new PropertyObject(PPDOpenApiQueryUserNameRequest.PARAM_NAME_OPENID, openID, ValueTypeEnum.String));
            //TODO log aop
            // logger.info("开始调用用户授权接口,请求参数={}", JSON.toJSONString(params));
            stopWatch.start("根据OpenID查询用户信息");
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiQueryUserNameResponse>() {
            };
            JSONObject jsonCredentials = new JSONObject();
            jsonCredentials.put(PPDOpenApiQueryUserNameRequest.PARAM_NAME_OPENID, openID);
            ResponseEntity<PPDOpenApiQueryUserNameResponse> responseEntity = callRetry(queryUserNameByOpenIDUrl, jsonCredentials.toString(), HttpMethod.POST, headers, responseType);
            stopWatch.stop();

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("根据OpenID查询用户信息接口[" + queryUserNameByOpenIDUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiQueryUserNameResponse response = responseEntity.getBody();
            if (!response.success()) {
                logger.error("根据OpenID查询用户信息接口[{}]返回失败,msg={}", queryUserNameByOpenIDUrl, response.getReturnMessage());
                return response;
            }

            //解密数据
            response.setUserName(decryptData(response.getUserName()));

            logger.debug("根据OpenID查询用户信息接口成功,openID={},username={}", openID, response.getUserName());

            return response;
        } catch (Exception e) {
            logger.error("根据OpenID查询用户信息接口发生异常,openID=" + openID, e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        } finally {
            logger.info("[manger]{}", stopWatch.prettyPrint());
        }
    }

    public PPDOpenApiQueryUserBalanceResponse getUserBalance(String username) {
        try {
            //获取token
            PPDUserAccessToken ppdUserAccessToken = ppdAccessTokenDataCache.get(username);
            if (ppdUserAccessToken == null) {
                throw new IllegalStateException("查询用户token为空");
            }

            String token = ppdUserAccessToken.getAccessToken();

            HttpHeaders headers = getRequestHeadersCommon(token);
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiQueryUserBalanceResponse>() {
            };
            ResponseEntity<PPDOpenApiQueryUserBalanceResponse> responseEntity;
            try {
                responseEntity = callRetry(queryBalanceUrl, null, HttpMethod.POST, headers, responseType);
            } catch (Exception e) {
                logger.error("查询用户账户余额接口发生异常,username=" + username + ",token=" + token, e);
                throw new PPDOpenApiInvokeException(e);
            }

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("查询用户账户余额接口[" + queryBalanceUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiQueryUserBalanceResponse response = responseEntity.getBody();
            if (!response.success()) {
                logger.error("查询用户账户余额接口[{}]返回失败,msg={}", queryBalanceUrl, response.getReturnMessage());
                return response;
            }

            logger.debug("查询用户账户余额接口接口成功,username={},response={}", username, response);
            return response;
        } catch (Exception e) {
            logger.error("查询用户余额发生异常,username=" + username + ",msg=" + e.getMessage());
            throw new PPDOpenApiInvokeException(e.getMessage());
        }


    }

    /**
     * 保存ppd username 和 accessToken
     * 目前使用同步保存
     *
     * @param userName
     * @param userAccessToken
     */
    public void saveUserNameAccessToken(String userName, PPDUserAccessToken userAccessToken) {
        String key = KeyVersionUtils.rediskeyPpdAccessTokenV(RedisKeyUtils.keyUserNameAccessToken(userName));

        boolean result;
        try {
            PPDUserAccessTokenProtos.PPDUserAccessToken token = PPDUserAccessTokenProtos.PPDUserAccessToken.newBuilder()
                    .setAccessToken(userAccessToken.getAccessToken())
                    .setExpiresIn(userAccessToken.getExpiresIn())
                    .setRefreshToken(userAccessToken.getRefreshToken())
                    .build();

            result = ppdCacheClientHA.String().setexBit(key, userAccessToken.getExpiresIn(), token.toByteArray()); //初始7天redis有效期
            //获取key对应的value，若缓存中没有，则调用LocalCache的load方法，从数据源中加载，并缓存
            ppdAccessTokenDataCache.load(userName);
            logger.info("保存用户accessToken完成,userName={},result={}", userName, result);
        } catch (Exception e) {
            logger.error("序列化对象失败,key=" + key + ",userName=" + userName, e);
        }
    }

    /**
     * 删除redis token
     */
    public boolean delUserNameAccessToken(String userName) {
        String key = KeyVersionUtils.rediskeyPpdAccessTokenV(RedisKeyUtils.keyUserNameAccessToken(userName));

        boolean result;
        try {
            result = ppdCacheClientHA.Key().del(key);
            logger.info("删除用户accessToken完成,userName={},result={}", userName, result);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
