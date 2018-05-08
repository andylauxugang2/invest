package com.invest.ivpush.message.provider.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import com.invest.ivpush.message.model.entity.SmsLog;
import com.invest.ivpush.message.provider.dao.SmsLogMapper;
import com.invest.ivpush.message.provider.service.SmsLogService;
import com.invest.ivpush.message.service.exception.PushException;
import org.springframework.stereotype.Service;

/**
 * @author xusm
 * 
 */
@Service("smsLogService")
public class SmsLogServiceImpl implements SmsLogService {

	@Resource
	private SmsLogMapper smsLogMapper;

	@Override
	public SmsLog get(Long id) {
		return smsLogMapper.selectByPrimaryKey(id);
	}

	/**
	 * 新添加
	 * 
	 * @param log
	 * @return
	 */
	@Override
	public void save(SmsLog log) throws PushException {
		log.setCreateTime(new Date());
		log.setIsDelete(false);
		smsLogMapper.insert(log);
	}
	
	/**
	 * 修改
	 * 
	 * @param log
	 * @return
	 */
	@Override
	public void update(SmsLog log) throws PushException {
		log.setUpdateTime(new Date());
		smsLogMapper.updateByPrimaryKey(log);
	}

	/**
	 * 删除短信记录
	 * 
	 * @param id
	 * @throws PushException
	 */
	@Override
	public void delete(Long id) throws PushException {
		SmsLog log = get(id);
		log.setState((short)3);
		smsLogMapper.updateByPrimaryKey(log);
	}
	
}
