package com.invest.ivusergateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserRegisterReq {

    private String phone;//手机号
    private String pass;//密码
    private String repass;//确认密码
    private String checkCode;//验证码
    private String referrerMobile;//推荐人手机号
}
