package com.invest.ivpush.message.provider.service;

import com.invest.ivpush.message.provider.plus.MessagePlus;

import java.util.List;

public interface MessagePlusService {

	public MessagePlus genrate(Long id);
	
	public List<MessagePlus> genrate(List<Long> ids);
	
}

