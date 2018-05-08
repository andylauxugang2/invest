package com.invest.ivcommons.security.domain;

import com.invest.ivcommons.base.entity.BaseEntity;

/**
 * Created by xugang on 2017/7/30.
 */
public class ChannelUrl extends BaseEntity {

    private static final long serialVersionUID = 3728311098902049624L;

    private String channel;
    private String urlCode; //安全起见 暴露urlCode
    private String url;
    private Boolean needTokenAuth;
    private Boolean expired;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUrlCode() {
        return urlCode;
    }

    public void setUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isNeedTokenAuth() {
        return needTokenAuth;
    }

    public void setNeedTokenAuth(Boolean needTokenAuth) {
        this.needTokenAuth = needTokenAuth;
    }

    public Boolean isExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
}
