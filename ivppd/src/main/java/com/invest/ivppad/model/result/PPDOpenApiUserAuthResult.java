package com.invest.ivppad.model.result;

import lombok.Data;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiUserAuthResult extends PPDOpenApiResult {
    private String accessToken; //身份令牌
    private String openID; //与appid关联的 用于获取用户信息的 唯一标识
    private String userName; //ppd用户名
}
