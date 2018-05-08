package com.invest.ivcommons.session.model;

/**
 * Created by xugang on 2017/7/29.
 */
public class UserSession {
    private String token;
    private Long uid;
    private String mobile;
    public String getToken() {
        return token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
