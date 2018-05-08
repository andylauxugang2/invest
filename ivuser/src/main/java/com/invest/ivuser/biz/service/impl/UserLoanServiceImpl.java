package com.invest.ivuser.biz.service.impl;

import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivuser.biz.manager.UserLoanManager;
import com.invest.ivuser.biz.service.UserLoanService;
import com.invest.ivuser.common.IVUserErrorEnum;
import com.invest.ivuser.dao.query.UserLoanRecordQuery;
import com.invest.ivuser.model.entity.UserLoanRecord;
import com.invest.ivuser.model.param.UserLoanRecordParam;
import com.invest.ivuser.model.result.UserLoanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class UserLoanServiceImpl implements UserLoanService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserLoanManager userLoanManager;

    @Override
    public UserLoanResult getUserLoanRecords(UserLoanRecordParam param) {
        UserLoanResult result = new UserLoanResult();
        try {
            UserLoanRecordQuery query = new UserLoanRecordQuery();
            query.setUserId(param.getUserId());
            query.setUsername(param.getUsername());
            query.setBidLoanBeginTime(param.getBidLoanBeginTime());
            query.setBidLoanEndTime(param.getBidLoanEndTime());
            query.setPolicyType(param.getPolicyType());

            boolean paged = param.isPaged();
            query.setPaged(paged);
            int pageStart = (param.getPage() - 1) * param.getLimit();
            query.setPageStart(pageStart);
            query.setPageLimit(param.getLimit());
            List<UserLoanRecord> list = userLoanManager.getUserLoanRecordList(query);

            int count = list.size();
            if (paged && count != 0) {
                count = userLoanManager.getUserLoanRecordTotalCount(query);
            }

            result.setUserLoanRecordList(list);
            result.setCount(count);
            return result;
        } catch (Exception e) {
            logger.error("查询PPD用户投资记录失败,username=" + param.getUsername(), e);
            IVUserErrorEnum.QUERY_USER_LOAN_RECORD_ERROR.fillResult(result);
            return result;
        }
    }

    @Override
    public UserLoanResult addUserLoanRecord(UserLoanRecord userLoanRecord) {
        UserLoanResult result = new UserLoanResult();
        try {
            userLoanManager.saveUserLoanRecord(userLoanRecord);
            return result;
        } catch (Exception e) {
            logger.error("添加用户投资记录失败,username=" + userLoanRecord.getUsername(), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }
    }

    @Override
    public UserLoanResult getUserLoanRecordCount(UserLoanRecordParam param) {
        UserLoanResult result = new UserLoanResult();
        try {
            UserLoanRecordQuery query = new UserLoanRecordQuery();
            query.setUserId(param.getUserId());
            query.setUsername(param.getUsername());
            query.setBidLoanBeginTime(param.getBidLoanBeginTime());
            query.setBidLoanEndTime(param.getBidLoanEndTime());

            int count = userLoanManager.getUserLoanRecordTotalCount(query);
            result.setCount(count);
            return result;
        } catch (Exception e) {
            logger.error("查询PPD用户投资记录失败,username=" + param.getUsername(), e);
            IVUserErrorEnum.QUERY_USER_LOAN_RECORD_ERROR.fillResult(result);
            return result;
        }
    }
}
