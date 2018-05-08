package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.LoanDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDetailDAO extends BaseDAO<LoanDetail> {

    LoanDetail selectByListingId(Integer listingId);
}