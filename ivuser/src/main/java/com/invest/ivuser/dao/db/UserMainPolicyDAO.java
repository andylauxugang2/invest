package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.UserMainPolicy;
import com.invest.ivuser.model.vo.UserMainPolicyVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMainPolicyDAO extends BaseDAO<UserMainPolicy> {

    List<UserMainPolicyVO> selectUserMainPolicy(UserMainPolicy userMainPolicy);

    UserMainPolicyVO selectUserMainPolicyByUniqueKey(@Param(value = "userId") Long userId,
                                                     @Param(value = "thirdUserUUID") String thirdUserUUID,
                                                     @Param(value = "mainPolicyId") Long mainPolicyId);

    List<UserMainPolicyVO> selectUserMainPolicyDetailList(@Param(value = "userId") Long userId,
                                                          @Param(value = "thirdUserUUID") String thirdUserUUID,
                                                          @Param(value = "mainPolicyId") Long mainPolicyId,
                                                          @Param(value = "status") Short status);

    void deleteBySelective(@Param(value = "userId") Long userId,
                           @Param(value = "thirdUserUUID") String thirdUserUUID,
                           @Param(value = "mainPolicyId") Long mainPolicyId);
}