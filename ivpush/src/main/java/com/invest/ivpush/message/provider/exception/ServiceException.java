package com.invest.ivpush.message.provider.exception;

/**
 * Created by xugang on 2017/7/27.
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Class clazz, String captchaNoExist, String mobile, String captcha) {

    }

    public ServiceException(Class clazz, String captchaInterval, String mobile) {

    }
}
