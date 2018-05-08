package com.invest.ivppdgateway.common;

/**
 * Created by xugang on 2017/7/28.
 */
public enum CodeEnum {

    SUCCESS("操作成功", 0),
    FAILED("操作失败", -1),
    SESSION_EXPIRED("登录过期，请重新登录", 5000),
    PARAM_ERROR("参数不正确", 2);

    private String msg;

    private Integer code;

    CodeEnum(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
