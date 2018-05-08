package com.invest.ivpay.common;

import com.invest.ivcommons.base.result.Result;

/**
 * Created by xugang on 2016/11/1.
 */
public enum IVPayErrorEnum {

    PAY_UNLINE_HANDLE_ERROR("IVPAY001", ErrorMsg.PAY_UNLINE_HANDLE_ERROR),
    QUERY_USER_ORDER_ERROR("IVPAY002", ErrorMsg.QUERY_USER_ORDER_ERROR),
    QUERY_USER_ORDER_UNIQUE_ERROR("IVPAY003", ErrorMsg.QUERY_USER_ORDER_UNIQUE_ERROR),
    QUERY_USER_ORDER_NULL_ERROR("IVPAY004", ErrorMsg.QUERY_USER_ORDER_NULL_ERROR),
    UPDATE_USER_ORDER_ERROR("IVPAY005", ErrorMsg.UPDATE_USER_ORDER_ERROR);

    private String errorCode;
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    IVPayErrorEnum(String errorCode, String errorMsg) {
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
        public static final String PAY_UNLINE_HANDLE_ERROR = "非常抱歉，线下支付结果处理失败，请联系客服人员";
        public static final String QUERY_USER_ORDER_ERROR = "非常抱歉，订单查询失败，请稍后重试";
        public static final String QUERY_USER_ORDER_UNIQUE_ERROR = "非常抱歉，重复订单号，请联系客服人员";
        public static final String QUERY_USER_ORDER_NULL_ERROR = "非常抱歉，订单不存在，请联系客服人员";
        public static final String UPDATE_USER_ORDER_ERROR = "非常抱歉，订单状态操作失败，请联系客服人员";

    }
}
