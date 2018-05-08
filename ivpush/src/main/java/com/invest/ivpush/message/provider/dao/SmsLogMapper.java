package com.invest.ivpush.message.provider.dao;

import com.invest.ivpush.message.model.entity.SmsLog;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsLogMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(SmsLog record);

    int insertSelective(SmsLog record);

    SmsLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SmsLog record);

    int updateByPrimaryKey(SmsLog record);
}