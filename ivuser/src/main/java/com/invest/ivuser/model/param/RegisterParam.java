package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class RegisterParam extends UserBaseParam {
    private static final long serialVersionUID = -4031096751604419687L;

    private String mobile;//手机号
    private String password;//密码
    private String confirmPassword;//确认密码
    private String checkCode;//验证码
    private String referrerMobile;//推荐人手机号

    private String clientType;
}
