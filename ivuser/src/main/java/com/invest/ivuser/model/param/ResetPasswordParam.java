package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class ResetPasswordParam extends UserBaseParam {
    private String oldPassword;//旧密码
    private String newPassword;//新密码密码
}
