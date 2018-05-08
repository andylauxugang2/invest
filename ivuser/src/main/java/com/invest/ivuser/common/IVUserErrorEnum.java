package com.invest.ivuser.common;

import com.invest.ivcommons.base.result.Result;

/**
 * Created by xugang on 2016/11/1.
 */
public enum IVUserErrorEnum {

    SEND_SMS_CHECK_CODE_ERROR("IVUSER001", ErrorMsg.SEND_SMS_CHECK_CODE_ERROR),
    GET_SMS_CHECK_CODE_ERROR("IVUSER002", ErrorMsg.GET_SMS_CHECK_CODE_ERROR),
    CHECK_SMS_CHECK_CODE_ERROR("IVUSER003", ErrorMsg.CHECK_SMS_CHECK_CODE_ERROR),
    REGISTER_MOBILE_EXISTS_ERROR("IVUSER004", ErrorMsg.REGISTER_MOBILE_EXISTS_ERROR),

    QUERY_USER_LOAN_RECORD_ERROR("IVUSER005", ErrorMsg.QUERY_USER_LOAN_RECORD_ERROR),
    CHECK_SMS_CHECK_CODE_EXPIRED_ERROR("IVUSER006", ErrorMsg.CHECK_SMS_CHECK_CODE_EXPIRED_ERROR),
    USER_NOT_EXISTS_ERROR("IVUSER007", ErrorMsg.USER_NOT_EXISTS_ERROR),
    USER_THIRD_BIND_INFO_NOT_EXISTS_ERROR("IVUSER008", ErrorMsg.USER_THIRD_BIND_INFO_NOT_EXISTS_ERROR),
    USER_LOGIN_USERNAME_NOT_EXISTS_ERROR("IVUSER009", ErrorMsg.USER_LOGIN_USERNAME_NOT_EXISTS_ERROR),
    LOAN_RISK_LEVEL_NOT_EXISTS_ERROR("IVUSER010", ErrorMsg.LOAN_RISK_LEVEL_NOT_EXISTS_ERROR),
    USER_POLICY_EXISTS_ERROR("IVUSER011", ErrorMsg.USER_POLICY_EXISTS_ERROR),
    DELETE_ERROR("IVUSER012", ErrorMsg.DELETE_ERROR),
    SAVE_ERROR("IVUSER013", ErrorMsg.SAVE_ERROR),
    MODIFY_ERROR("IVUSER014", ErrorMsg.MODIFY_ERROR),
    USER_MAIN_POLICY_CLOSED_ERROR("IVUSER015", ErrorMsg.USER_MAIN_POLICY_CLOSED_ERROR),
    REFUND_PWD_PHONE_ERROR("IVUSER016", ErrorMsg.REFUND_PWD_PHONE_ERROR),
    RESET_OLD_PWD_ERROR("IVUSER017", ErrorMsg.RESET_OLD_PWD_ERROR),
    QUERY_USER_MESSAGE_ERROR("IVUSER018", ErrorMsg.QUERY_USER_MESSAGE_ERROR),
    QUERY_USER_ACCOUNT_IS_NULL_ERROR("IVUSER019", ErrorMsg.QUERY_USER_ACCOUNT_IS_NULL_ERROR),
    QUERY_USER_ACCOUNT_ERROR("IVUSER020", ErrorMsg.QUERY_USER_ACCOUNT_ERROR),
    UPDATE_USER_ACCOUNT_ERROR("IVUSER021", ErrorMsg.UPDATE_USER_ACCOUNT_ERROR),
    USER_TOKEN_NOT_EXISTS_ERROR("IVUSER022", ErrorMsg.USER_TOKEN_NOT_EXISTS_ERROR),
    AUTO_LOGIN_ERROR("IVUSER023", ErrorMsg.AUTO_LOGIN_ERROR);

    private String errorCode;
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    IVUserErrorEnum(String errorCode, String errorMsg) {
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
        public static final String SEND_SMS_CHECK_CODE_ERROR = "发送短信验证码失败";
        public static final String GET_SMS_CHECK_CODE_ERROR = "获取短信验证码失败";
        public static final String CHECK_SMS_CHECK_CODE_ERROR = "短信验证码输入有误,请重新输入";
        public static final String REGISTER_MOBILE_EXISTS_ERROR = "注册失败,该手机号已被占用,请更换手机号";
        public static final String REFUND_PWD_PHONE_ERROR = "找回失败,该手机号不存在,请更换手机号";
        public static final String QUERY_USER_LOAN_RECORD_ERROR = "查询投资记录异常,请稍后重试";
        public static final String QUERY_USER_MESSAGE_ERROR = "查询消息异常,请稍后重试";
        public static final String CHECK_SMS_CHECK_CODE_EXPIRED_ERROR = "短信验证码过期,请重新获取";
        public static final String USER_NOT_EXISTS_ERROR = "用户不存在";
        public static final String USER_THIRD_BIND_INFO_NOT_EXISTS_ERROR = "您还没有绑定拍拍贷账号,去用户授权吧亲";
        public static final String USER_LOGIN_USERNAME_NOT_EXISTS_ERROR = "亲,用户名或密码错误,请重新登录";
        public static final String LOAN_RISK_LEVEL_NOT_EXISTS_ERROR = "输入的风险等级不存在";

        public static final String USER_POLICY_EXISTS_ERROR = "很抱歉,您设置的策略名已存在,请更换策略";

        public static final String DELETE_ERROR = "很抱歉,删除失败%s";
        public static final String SAVE_ERROR = "很抱歉,保存失败%s";
        public static final String MODIFY_ERROR = "很抱歉,修改失败%s";

        public static final String USER_MAIN_POLICY_CLOSED_ERROR = "操作失败,请先开启相关主策略[%s]";
        public static final String RESET_OLD_PWD_ERROR = "重置失败,原密码输入有误";

        public static final String QUERY_USER_ACCOUNT_IS_NULL_ERROR = "查询用户账户信息异常,账户不存在,请稍后重试";
        public static final String QUERY_USER_ACCOUNT_ERROR = "查询用户账户信息异常,请稍后重试";
        public static final String UPDATE_USER_ACCOUNT_ERROR = "更新账户失败,请稍后重试";
        public static final String USER_TOKEN_NOT_EXISTS_ERROR = "token不存在";
        public static final String AUTO_LOGIN_ERROR = "自动登录错误";

    }
}
