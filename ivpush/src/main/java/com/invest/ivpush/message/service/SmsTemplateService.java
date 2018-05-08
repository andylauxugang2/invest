package com.invest.ivpush.message.service;

import com.invest.ivpush.message.model.dataobject.Pagination;
import com.invest.ivpush.message.model.entity.SmsTemplate;
import com.invest.ivpush.message.service.exception.PushException;

import java.util.List;
import java.util.Map;

public interface SmsTemplateService {

	public SmsTemplate get(Long id);

	/**
	 * 新添加
	 * 
	 * @param log
	 * @return
	 */
	public void save(SmsTemplate log) throws PushException;
	
	/**
	 * 修改
	 * 
	 * @param log
	 * @return
	 */
	public void update(SmsTemplate log) throws PushException;
	
	/**
	 * 修改
	 * 
	 * @param log
	 * @return
	 */
	public void update(Long id, SmsTemplate log) throws PushException;

	/**
	 * 删除短信记录
	 * 
	 * @param id
	 * @throws PushException
	 */
	public void delete(Long id) throws PushException;
		
	
	/**
	 * 获取模板
	 * 
	 * @param group
	 * @param code
	 * @throws PushException
	 */
	public SmsTemplate getByGroupAndCode(String group, String code) throws PushException;
		
	public String buildContent(String group, String code, Map<String, String> data);
	
	public String buildContent(SmsTemplate smsTemplate, Map<String, String> data);
	
	public boolean isExistByThirdAuthId(Long thirdAuthId);
	
	/**
	 * 获取所有短信模板
	 * 
	 * @throws PushException
	 */
	//public List<SmsTemplate> findAll() throws AppleException;


	public Pagination findByPage(Pagination page, String keyword);

	public void deletes(List<Long> idList) throws PushException;
	
}

