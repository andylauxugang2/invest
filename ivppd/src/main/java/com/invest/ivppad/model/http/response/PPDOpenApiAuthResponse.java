package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiAuthResponse implements Serializable {

    @JsonProperty("ErrMsg")
    private String errMsg;
    @JsonProperty("OpenID")
    private String openID; //用户的开放平台ID：拍拍贷给予的开发者和授权用户之间的唯一关联ID
    @JsonProperty("AccessToken")
    private String accessToken; //身份令牌：访问网关接口的身份令牌（默认有效期7天）
    @JsonProperty("RefreshToken")
    private String refreshToken; //刷新令牌：通过该令牌可以刷新AccessToken (有效期：90天)
    @JsonProperty("ExpiresIn")
    private int expiresIn; //令牌有效期：身份令牌的有效期，单位秒

    public boolean success() {
        if(this.errMsg == null || "".equals(errMsg.trim())){
            return true;
        }
        return false;
    }
}