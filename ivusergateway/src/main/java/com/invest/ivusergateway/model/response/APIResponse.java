package com.invest.ivusergateway.model.response;

import com.invest.ivusergateway.common.constants.CodeEnum;

import java.io.Serializable;

/**
 * Created by xugang on 2017/7/28.
 */
public class APIResponse<T> implements Serializable {

    private static final long serialVersionUID = -7091333524679518934L;

    private String version = "1.0";

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误描述信息
     */
    private String msg;

    /**
     * vo 对象
     */
    private T data;

    private int count;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static <T> APIResponse<T> createResult(CodeEnum codeEnum) {

        return createResult(codeEnum.getCode(), codeEnum.getMsg());
    }

    public static <T> APIResponse<T> createResult(CodeEnum codeEnum, String message) {

        return createResult(codeEnum.getCode(), message);
    }

    public static <T> APIResponse<T> createResult(int status, String message) {

        APIResponse<T> result = new APIResponse<T>();
        result.setData(null);
        result.setCode(status);
        result.setMsg(message);

        return result;
    }

    public static <T> APIResponse<T> success(T data) {
        APIResponse<T> result = new APIResponse<T>();
        result.setData(data);
        result.setCode(CodeEnum.SUCCESS.getCode());
        result.setMsg(CodeEnum.SUCCESS.getMsg());
        return result;
    }

    public static APIResponse success() {
        APIResponse result = new APIResponse();
        result.setData(null);
        result.setCode(CodeEnum.SUCCESS.getCode());
        result.setMsg(CodeEnum.SUCCESS.getMsg());
        return result;
    }
}
