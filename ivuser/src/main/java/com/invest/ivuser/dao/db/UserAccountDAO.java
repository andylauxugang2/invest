package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.UserAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountDAO extends BaseDAO<UserAccount> {

    UserAccount selectOneByUserId(Long userId);

    void updateBalance(@Param("userId") Long userId, @Param("bidAmountBalance") Integer bidAmountBalance);
}