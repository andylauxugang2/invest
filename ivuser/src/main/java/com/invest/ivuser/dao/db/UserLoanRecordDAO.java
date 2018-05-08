package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.dao.query.UserLoanRecordQuery;
import com.invest.ivuser.model.entity.UserLoanRecord;
import com.invest.ivuser.model.entity.ext.LoanTotalCount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoanRecordDAO extends BaseDAO<UserLoanRecord> {

    List<UserLoanRecord> selectListByQuery(UserLoanRecordQuery query);

    int selectCountByQuery(UserLoanRecordQuery query);

    List<UserLoanRecord> selectSingleListByQuery(UserLoanRecordQuery query);

    List<Integer> selectGroupLoanIdByQuery(UserLoanRecordQuery query);

    int updateListByLoanId(UserLoanRecord userLoanRecord);

    /** 统计相关sql */
    List<LoanTotalCount> selectLoanTotalCount(UserLoanRecordQuery query);
}