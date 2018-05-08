package com.invest.ivcommons.base.param;

import java.io.Serializable;

/**
 * Created by xugang on 2017/7/26.
 */
public class BaseParam implements Serializable {
    private String sourceIp;
    private String operator;
    private Long requestTime;
    private String version;

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
