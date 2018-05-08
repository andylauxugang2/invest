package com.invest.ivppad.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xugang on 2017/8/2.
 */
@Data
public class PPDUserAccessToken implements Serializable {
    private String accessToken;
    private int expiresIn; //令牌有效期：身份令牌的有效期，单位秒
    private String refreshToken; //刷新令牌：通过该令牌可以刷新AccessToken (有效期：90天)
}
