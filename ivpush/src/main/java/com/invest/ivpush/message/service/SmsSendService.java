package com.invest.ivpush.message.service;

import com.invest.ivpush.message.service.exception.PushException;

import java.util.Map;

public interface SmsSendService {

	//直接发送
	public void send(String group, String code, String mobile, String content) throws PushException;

	//通过查询模板发送
	public void send(String group, String code, String mobile, Map<String, String> data) throws PushException;

}
