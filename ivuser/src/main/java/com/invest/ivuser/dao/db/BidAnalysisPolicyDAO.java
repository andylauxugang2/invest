package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.BidAnalysisPolicy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BidAnalysisPolicyDAO extends BaseDAO<BidAnalysisPolicy> {

    List<BidAnalysisPolicy> selectSummaryRecordLoanDetail(@Param(value = "createTimeBegin") Date createTimeBegin,
                                                          @Param(value = "createTimeEnd") Date createTimeEnd);

    void deleteByUniqueKey(@Param(value = "userId") Long userId,
                           @Param(value = "username") String username,
                           @Param(value = "policyId") Long policyId);

    BidAnalysisPolicy selectOneByUniqueKey(@Param(value = "userId") Long userId,
                                           @Param(value = "username") String username,
                                           @Param(value = "policyId") Long policyId);

    int updateByUniqueKey(BidAnalysisPolicy bidAnalysisPolicy);
}