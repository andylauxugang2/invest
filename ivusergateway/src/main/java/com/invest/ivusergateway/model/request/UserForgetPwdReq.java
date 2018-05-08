package com.invest.ivusergateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserForgetPwdReq {

    private String phone;//手机号
    private String pass;//密码
    private String repass;//确认密码
    private String checkCode;//验证码
}
