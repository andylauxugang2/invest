package com.invest.ivusergateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserLoginReq {

    private String username;//手机号/mail/ppdusername
    private String pass;//密码
    private String checkCode;//验证码
}
