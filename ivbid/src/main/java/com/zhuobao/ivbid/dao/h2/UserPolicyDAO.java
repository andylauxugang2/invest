package com.zhuobao.ivbid.dao.h2;

import com.zhuobao.ivbid.dao.h2.query.UserPolicyQuery;
import com.zhuobao.ivbid.model.entity.UserPolicy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("h2UserPolicyDAO")
public interface UserPolicyDAO {

    int deleteByPrimaryKey(Long id);

    int insert(UserPolicy record);

    int insertSelective(UserPolicy record);

    UserPolicy selectByPrimaryKey(Long id);

    List<UserPolicy> selectListBySelective(UserPolicy record);

    UserPolicy selectOneByUniqueKey(@Param("userId") Long userId, @Param("username") String username, @Param("policyId") Long policyId);

    int updateByUniqueKeySelective(UserPolicy record);

    int updateByPrimaryKey(UserPolicy record);

    List<UserPolicy> selectListByQuery(UserPolicyQuery query);

    void deleteByUniqueKey(@Param("userId") Long userId, @Param("username") String username, @Param("policyId") Long policyId);

    void updateByUniqueKey(@Param("userId") Long userId, @Param("username") String username, @Param("policyId") Long policyId, @Param("bidAmount") Integer bidAmount);
}