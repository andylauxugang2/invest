package com.invest.ivusergateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserResetPwdReq {

    private Long userId;
    private String password;//原密码
    private String newPassword;//密码
}
