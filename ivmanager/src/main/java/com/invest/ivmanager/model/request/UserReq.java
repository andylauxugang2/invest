package com.invest.ivmanager.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/10/25.do best.
 */
@Data
public class UserReq {
    //查询参数
    private Long userId;
    private String mobile;

    //一键登录参数
    private String username;
    private String pass;
}
