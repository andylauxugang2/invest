package com.invest.ivpush.message.service;

import com.invest.ivpush.message.service.exception.PushException;

public interface SmsCaptchaService {

	/**
	 * 验证码验证
	 */
	public void validateCaptcha(String group, String code, String mobile, String captcha) throws PushException;

	/**
	 * 发送验证码
	 */
	public String sendCaptcha(String group, String code, String mobile) throws PushException;

}
