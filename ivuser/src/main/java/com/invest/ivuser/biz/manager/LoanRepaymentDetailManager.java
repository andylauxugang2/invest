package com.invest.ivuser.biz.manager;

import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.LoanRepaymentDetail;
import com.invest.ivuser.model.entity.ext.LoanRepaymentOverdueDetail;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class LoanRepaymentDetailManager extends BaseManager {


    public List<LoanRepaymentDetail> getLoanRepaymentDetailList(LoanRepaymentDetail loanRepaymentDetail) {
        List<LoanRepaymentDetail> result;
        try {
            result = loanRepaymentDetailDAO.selectListBySelective(loanRepaymentDetail);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    public void saveLoanRepaymentDetail(LoanRepaymentDetail loanRepaymentDetail) {
        try {
            int line = loanRepaymentDetailDAO.insert(loanRepaymentDetail);
            if (line != 1) {
                throw new IVDAOException("插入loanRepaymentDetail数据库返回行数不为1");
            }
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public void saveLoanRepaymentDetail(List<LoanRepaymentDetail> loanRepaymentDetailList) {
        try {
            loanRepaymentDetailDAO.batchInsert(loanRepaymentDetailList);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public LoanRepaymentDetail getLoanRepaymentDetailById(Long id) {
        LoanRepaymentDetail result;
        try {
            result = loanRepaymentDetailDAO.selectByPrimaryKey(id);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }
}
