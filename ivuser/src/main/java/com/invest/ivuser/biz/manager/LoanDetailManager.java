package com.invest.ivuser.biz.manager;

import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.LoanDetail;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class LoanDetailManager extends BaseManager {


    public List<LoanDetail> getLoanDetailList(LoanDetail loanDetail) {
        List<LoanDetail> result;
        try {
            result = loanDetailDAO.selectListBySelective(loanDetail);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    public void saveLoanDetail(LoanDetail loanDetail) {
        try {
            int line = loanDetailDAO.insert(loanDetail);
            if (line != 1) {
                throw new IVDAOException("插入loanDetail数据库返回行数不为1");
            }
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public LoanDetail getLoanDetailById(Long id) {
        LoanDetail result;
        try {
            result = loanDetailDAO.selectByPrimaryKey(id);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    public LoanDetail getLoanDetailByListingId(Integer listingId) {
        LoanDetail result;
        try {
            result = loanDetailDAO.selectByListingId(listingId);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }
}
