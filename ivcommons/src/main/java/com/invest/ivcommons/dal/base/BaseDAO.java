package com.invest.ivcommons.dal.base;

import com.invest.ivcommons.base.entity.BaseEntity;

import java.util.List;

public interface BaseDAO<T extends BaseEntity> {
	
    int deleteByPrimaryKey(Long id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Long id);

    T selectOneBySelective(T record);

    List<T> selectListBySelective(T record);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}