package com.invest.ivppad.model.http.request;

import lombok.Data;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiQueryUserNameRequest extends PPDOpenApiBaseRequest {
    public static final String PARAM_NAME_OPENID = "OpenID";

    private String openID; //授权成功后返回结果中获取

}
