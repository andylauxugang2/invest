package com.invest.ivcommons.base.result;

import java.io.Serializable;

/**
 * Created by xugang on 16/11/1.
 */
public class Result implements Serializable {
    private boolean success = true;

    /**
     * 错误码和错误描述由各业务方定义
     */
    private String errorCode;
    private String errorMsg;

    public Result() {
        success = true;
    }

    public void setErrorInfo(String errorCode, String errorMsg) {
        setErrorCode(errorCode);
        setErrorMsg(errorMsg);
    }

    public void setErrorCode(String errorCode) {
        this.setSuccess(false);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailed() {
        return !success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void format(String arg1) {
        this.setErrorMsg(String.format(this.getErrorMsg(), arg1));
        this.setErrorMsg(String.format(this.getErrorMsg(), arg1));
    }

    public void format(String... arguments) {
        this.setErrorMsg(String.format(this.getErrorMsg(), arguments));
        this.setErrorMsg(String.format(this.getErrorMsg(), arguments));
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
