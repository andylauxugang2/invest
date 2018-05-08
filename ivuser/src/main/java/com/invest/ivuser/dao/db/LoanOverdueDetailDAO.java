package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.dao.query.LoanOverdueDetailQuery;
import com.invest.ivuser.model.entity.LoanOverdueDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanOverdueDetailDAO extends BaseDAO<LoanOverdueDetail> {

    int batchInsert(List<LoanOverdueDetail> records);

    List<LoanOverdueDetail> selectListByQuery(LoanOverdueDetailQuery loanOverdueDetailQuery);
}