package com.invest.ivpush.message.provider.plus;

import com.invest.ivpush.message.provider.exception.MessageException;

public interface SmsMessagePlus extends MessagePlus{
	String SMSMESSAGEPLUS_THIRD_TYPE_YUNPIAN = "yunpian";

	String doSend(String mobile, String content) throws MessageException;
	
}
