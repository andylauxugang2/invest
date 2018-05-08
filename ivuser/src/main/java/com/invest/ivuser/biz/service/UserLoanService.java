package com.invest.ivuser.biz.service;

import com.invest.ivuser.model.entity.UserLoanRecord;
import com.invest.ivuser.model.param.UserLoanRecordParam;
import com.invest.ivuser.model.result.UserLoanResult;

/**
 * Created by xugang on 2017/7/28.
 */
public interface UserLoanService {

    /**
     * 查询ppd用户 投资记录
     */
    UserLoanResult getUserLoanRecords(UserLoanRecordParam param);

    /**
     * 添加用户投资记录
     */
    UserLoanResult addUserLoanRecord(UserLoanRecord userLoanRecord);

    UserLoanResult getUserLoanRecordCount(UserLoanRecordParam param);
}
