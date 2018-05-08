package com.invest.ivuser.biz.manager;

import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.dao.db.LoanOverdueDetailDAO;
import com.invest.ivuser.dao.query.LoanOverdueDetailQuery;
import com.invest.ivuser.model.entity.LoanOverdueDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class LoanOverdueDetailManager extends BaseManager {

    @Autowired
    protected LoanOverdueDetailDAO loanOverdueDetailDAO;

    public List<LoanOverdueDetail> getLoanOverdueDetailList(Date beginTime, Date endTime, Long userId, String username, int code) {
        List<LoanOverdueDetail> result;
        LoanOverdueDetailQuery query = new LoanOverdueDetailQuery();
        query.setDateBegin(beginTime);
        query.setDateEnd(endTime);
        query.setUserId(userId);
        query.setUsername(username);
        query.setOverdueType(code);
        try {
            result = loanOverdueDetailDAO.selectListByQuery(query);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }
}
