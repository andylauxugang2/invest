package com.invest.ivusergateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserVO {
    private String mobile;
    private String nick;
    private String headUrl; //头像url
    private String realName; //实名
    private String sex; //0男1女
    private Long userId; //用户ID
    private String userToken; //token
}
