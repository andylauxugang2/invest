package com.invest.ivpaygateway.common.constants;

/**
 * Created by xugang on 2017/7/28.
 */
public enum CodeEnum {

    SUCCESS("操作成功", 0),
    FAILED("系统内部异常,请稍后再试", -1),
    PARAM_ERROR("参数不正确", 2),
    INTERNET_FAIL("网络异常", 3),
    MAXCOUNT("达到最大次数", 4),
    REPEAT("请不要重复操作", 5),
    SESSION_EXPIRED("登录过期，请重新登录", 5000),
    URL_FAIL("无访问权限", 6),
    REPEAT_REGIST("该手机号码已被注册", 5001),
    MOBILE_NOT_EXISTS("该手机号码不存在", 4001),
    LOGIN_USERNAME_NOTEXIST("亲,用户名或密码错误,请重新登录", 5002),
    PASSWORD_INCORRECT("手机号密码不匹配，请重新输入！", 5003),
    SMSCODE_NOTCORRECT("验证码输入错误，请重新输入", 5004),
    MOBILE_NOTREGIST("该账号尚未注册，请先注册", 5005),
    CANNOT_REPEAT_VERIFY("请勿重新验证", 5006),
    PLEASE_USE_REG_MOBILE("请使用注册手机号认证", 5007),
    PLEASE_USE_CORRECT_MOBILE("请输入正确的手机号", 5008),
    PLEASE_TYPE_MOBILE("请输入手机号", 5009),
    RESULT_NULL("很抱歉,未查询到任何记录", 5011),
    CODE_INVALID("验证码已过期，请重新发送", 5010),
    IMAGE_CODE_ERROR("图形验证码错误，请重新输入", 5011);

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
