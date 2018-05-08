package com.invest.ivpush.message.service;

import com.invest.ivpush.message.model.dataobject.Pagination;
import com.invest.ivpush.message.model.dataobject.ThirdAuthSo;
import com.invest.ivpush.message.model.entity.ThirdAuth;
import com.invest.ivpush.message.service.exception.PushException;

import java.util.List;

public interface ThirdAuthService {

	public ThirdAuth get(Long id) throws PushException;

	public Long save(ThirdAuth log) throws PushException;
	
	public Long update(ThirdAuth log) throws PushException;
	
	public Long update(Long id, ThirdAuth log) throws PushException;

	public Long delete(Long id) throws PushException;
	
	public List<ThirdAuth> findAll() throws PushException;
		
	//public void freshen();
	
	public Pagination findByPage(Pagination page, ThirdAuthSo so) throws PushException;
	
	public List<ThirdAuth> findListByType(Integer type) throws PushException;
		
}

