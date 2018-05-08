package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.User;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserResult extends Result {
    private static final long serialVersionUID = -4619288839107627161L;

    private String token;
    private Long userId;

    private User user;
    private List<User> users;

    private String smsCheckCode; //短信验证码

    private String headImgOrgBase64;
    private String headImgBase64;
}
