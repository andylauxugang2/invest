package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.BlackListThird;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListThirdDAO extends BaseDAO<BlackListThird> {
    int deleteByUniqueKey(@Param(value = "type") String type, @Param(value = "value") String value);
}