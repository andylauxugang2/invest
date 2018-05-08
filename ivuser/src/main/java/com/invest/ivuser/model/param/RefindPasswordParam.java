package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class RefindPasswordParam extends UserBaseParam {
    private String mobile;//手机号
    private String password;//密码
    private String confirmPassword;//确认密码
    private String checkCode;//验证码
}
