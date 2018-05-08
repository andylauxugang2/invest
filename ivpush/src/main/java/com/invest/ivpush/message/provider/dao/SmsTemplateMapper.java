package com.invest.ivpush.message.provider.dao;

import com.invest.ivpush.message.model.dataobject.Pagination;
import com.invest.ivpush.message.model.entity.SmsTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsTemplateMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(SmsTemplate record);

    int insertSelective(SmsTemplate record);

    SmsTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SmsTemplate record);

    int updateByPrimaryKey(SmsTemplate record);

    List<SmsTemplate> selectByPage(@Param("page") Pagination page, @Param("keyword") String keyword);

    SmsTemplate selectByGroupAndCode(@Param("group") String group, @Param("code") String code);
}