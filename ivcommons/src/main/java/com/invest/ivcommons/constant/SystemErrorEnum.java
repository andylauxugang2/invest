package com.invest.ivcommons.constant;

import com.invest.ivcommons.base.result.Result;

/**
 * Created by xugang on 2016/11/1.
 */
public enum SystemErrorEnum {

    PARAM_IS_INVALID("SYSTEM001", ErrorMsg.PARAM_IS_INVALID_ERROR),
    SYSTEM_INNER_ERROR("SYSTEM002", ErrorMsg.SYSTEM_INNER_ERROR);

    private String errorCode;
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    SystemErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isEqual(Result rs) {
        return this.getErrorCode().equals(rs.getErrorCode());
    }

    public void fillResult(Result rs) {
        rs.setErrorCode(getErrorCode());
        rs.setErrorMsg(getErrorMsg());
    }

    public static class ErrorMsg {
        public static final String PARAM_IS_INVALID_ERROR = "%s参数不合法";
        public static final String SYSTEM_INNER_ERROR = "很抱歉,系统异常,请您稍后再试";

    }
}
