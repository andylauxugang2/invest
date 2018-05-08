package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.BidAnalysis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BidAnalysisDAO extends BaseDAO<BidAnalysis> {

    List<BidAnalysis> selectSummaryRecordLoanDetail(@Param(value = "createTimeBegin") Date createTimeBegin,
                                                    @Param(value = "createTimeEnd") Date createTimeEnd);

    void deleteByUniqueKey(@Param(value = "userId") Long userId,
                           @Param(value = "username") String username,
                           @Param(value = "month") String month);

    int updateByUniqueKey(BidAnalysis bidAnalysis);
}