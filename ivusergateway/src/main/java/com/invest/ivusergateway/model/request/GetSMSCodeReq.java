package com.invest.ivusergateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class GetSMSCodeReq {

    private String phone;//手机号
    private String imageCheckCode;//图形验证码
}
