package com.invest.ivppad.model.http.request;

import lombok.Data;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiAuthorizeRequest extends PPDOpenApiBaseRequest {
    public static final String PARAM_NAME_CODE = "code";
    public static final String PARAM_NAME_APPID = "AppID";

    private String code;
    private String appID;

}
