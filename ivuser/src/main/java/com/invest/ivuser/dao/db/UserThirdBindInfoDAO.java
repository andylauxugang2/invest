package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserThirdBindInfoDAO extends BaseDAO<UserThirdBindInfo> {

    List<String> selectAllThirdUUIDList(UserThirdBindInfo param);

    int updateByUserIdAndThirdUserUUID(UserThirdBindInfo userThirdBindInfo);

    int updateByPrimaryKeySelective(UserThirdBindInfo userThirdBindInfo);
}