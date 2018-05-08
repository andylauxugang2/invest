package com.invest.ivppad.common;

import com.invest.ivcommons.base.result.Result;

/**
 * Created by xugang on 2016/11/1.
 */
public enum IVPPDErrorEnum {

    CAN_NOT_GET_ACCOUNT_BALANCE_ERROR("IVPPD001", ErrorMsg.CAN_NOT_GET_ACCOUNT_BALANCE_ERROR),
    CAN_NOT_GET_ACCESS_TOKEN_ERROR("IVPPD002", ErrorMsg.CAN_NOT_GET_ACCESS_TOKEN_ERROR);

    private String errorCode;
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    IVPPDErrorEnum(String errorCode, String errorMsg) {
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
        public static final String CAN_NOT_GET_ACCOUNT_BALANCE_ERROR = "很抱歉,暂未获取到该账户现金余额";
        public static final String CAN_NOT_GET_ACCESS_TOKEN_ERROR = "很抱歉,您用户可能过期,请尝试重新登录";

    }
}
