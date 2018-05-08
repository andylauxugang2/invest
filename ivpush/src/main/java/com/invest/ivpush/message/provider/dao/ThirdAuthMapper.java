package com.invest.ivpush.message.provider.dao;

import com.invest.ivpush.message.model.dataobject.Pagination;
import com.invest.ivpush.message.model.dataobject.ThirdAuthSo;
import com.invest.ivpush.message.model.entity.ThirdAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdAuthMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(ThirdAuth record);

    int insertSelective(ThirdAuth record);

    ThirdAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ThirdAuth record);

    int updateByPrimaryKey(ThirdAuth record);

    List<ThirdAuth> selectByAll();

    List<ThirdAuth> selectByPage(@Param("page") Pagination page, @Param("so") ThirdAuthSo so);

    List<ThirdAuth> selectByType(@Param("type") Integer type);
}