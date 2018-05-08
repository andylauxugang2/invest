package com.invest.ivuser.biz.manager;

import com.invest.ivuser.dao.db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xugang on 2017/7/29.
 */
public abstract class BaseManager {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected CheckCodeDAO checkCodeDAO;

    @Autowired
    protected UserDAO userDAO;

    @Autowired
    protected UserAccountDAO userAccountDAO;

    @Autowired
    protected LoanPolicyDAO loanPolicyDAO;

    @Autowired
    protected UserLoanRecordDAO userLoanRecordDAO;

    @Autowired
    protected UserThirdBindInfoDAO userThirdBindInfoDAO;

    @Autowired
    protected MainPolicyDAO mainPolicyDAO;

    @Autowired
    protected UserMainPolicyDAO userMainPolicyDAO;

    @Autowired
    protected UserPolicyDAO userPolicyDAO;

    @Autowired
    protected BizOptLogDAO bizOptLogDAO;

    @Autowired
    protected NotifyMessageDAO notifyMessageDAO;

    @Autowired
    protected UserNotifyMessageDAO userNotifyMessageDAO;

    @Autowired
    protected LoanDetailDAO loanDetailDAO;

    @Autowired
    protected BidAnalysisDAO bidAnalysisDAO;

    @Autowired
    protected LoanRepaymentDetailDAO loanRepaymentDetailDAO;

    @Autowired
    protected BlackListThirdDAO blackListThirdDAO;


}
