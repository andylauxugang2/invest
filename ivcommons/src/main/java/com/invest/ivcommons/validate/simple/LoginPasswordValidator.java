package com.invest.ivcommons.validate.simple;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivcommons.validate.Validateware;
import com.invest.ivcommons.validate.model.MobileValidParam;
import com.invest.ivcommons.validate.model.ValidParam;
import com.invest.ivcommons.validate.model.ValidParamErrorEnum;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class LoginPasswordValidator implements Validateware<Result, ValidParam> {
    private static final String passwordPattern = "(^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$)";

    @Override
    public Result valid(ValidParam validParam) {
        Result result = new Result();
        if(validParam.getMatchedString() == null){
            result.setSuccess(false);
            result.setErrorMsg(String.format(ValidParamErrorEnum.NULL_ERROR.getDesc(), "密码"));
            return result;
        }
        String password = validParam.getMatchedString();
        // 验证手机号
        Pattern p = Pattern.compile(passwordPattern);
        Matcher m = p.matcher(password);
        boolean matched = m.matches();
        if(!matched){
            result.setErrorMsg(ValidParamErrorEnum.LOGIN_PASSWORD_FORMAT_ERROR.getDesc());
        }
        result.setSuccess(matched);
        return result;
    }
}
