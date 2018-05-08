package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class LoginParam extends UserBaseParam {
    private String username;//手机号
    private String password;//密码
    private String checkCode;//验证码
}
