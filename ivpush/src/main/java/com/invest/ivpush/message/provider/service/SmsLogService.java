package com.invest.ivpush.message.provider.service;

import com.invest.ivpush.message.model.entity.SmsLog;
import com.invest.ivpush.message.service.exception.PushException;

public interface SmsLogService {

	public SmsLog get(Long id);

	/**
	 * 新添加
	 * 
	 * @param log
	 * @return
	 */
	public void save(SmsLog log) throws PushException;
	
	/**
	 * 修改
	 * 
	 * @param log
	 * @return
	 */
	public void update(SmsLog log) throws PushException;

	/**
	 * 删除短信记录
	 * 
	 * @param id
	 * @throws PushException
	 */
	public void delete(Long id) throws PushException;

}

