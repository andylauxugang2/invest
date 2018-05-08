package com.invest.ivcommons.validate.simple;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivcommons.validate.Validateware;
import com.invest.ivcommons.validate.model.MobileValidParam;
import com.invest.ivcommons.validate.model.ValidParamErrorEnum;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class MobileValidator implements Validateware<Result, MobileValidParam> {
    private static final String mobilePattern = "(^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$)";

    @Override
    public Result valid(MobileValidParam validParam) {
        Result result = new Result();
        if(validParam.getMobile() == null){
            result.setSuccess(false);
            result.setErrorMsg(String.format(ValidParamErrorEnum.NULL_ERROR.getDesc(), "手机号"));
            return result;
        }
        String mobile = validParam.getMobile();
        // 验证手机号
        Pattern p = Pattern.compile(mobilePattern);
        Matcher m = p.matcher(mobile);
        boolean matched = m.matches();
        if(!matched){
            result.setErrorMsg(String.format(ValidParamErrorEnum.INVALID_ERROR.getDesc(), "手机号"));
        }
        result.setSuccess(matched);
        return result;
    }
}
