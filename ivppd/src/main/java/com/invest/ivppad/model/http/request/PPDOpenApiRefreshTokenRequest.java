package com.invest.ivppad.model.http.request;

import lombok.Data;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiRefreshTokenRequest extends PPDOpenApiBaseRequest {
    public static final String PARAM_NAME_APPID = "AppID";
    public static final String PARAM_NAME_OPENID = "OpenID";
    public static final String PARAM_NAME_REFRESHTOKEN = "RefreshToken";

    private String appID;
    private String openID;
    private String refreshToken;

}
