package com.zhuobao.ivsyncjob.job.elastic;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivppad.biz.manager.PPDOpenApiLoanManager;
import com.invest.ivppad.common.PPDLoanRepayStatusEnum;
import com.invest.ivuser.dao.db.LoanRepaymentDetailDAO;
import com.invest.ivuser.dao.query.LoanRepaymentDetailQuery;
import com.invest.ivuser.model.entity.LoanRepaymentDetail;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 1天1次
 * 每天凌晨1点执行一次
 * 查询还款日<=当前时间 && 状态=等待还款 所有还款计划 并同步接口数据
 * Created by xugang on 2017/9/18.
 */
@Component
public class SyncWaitingLenderRepaymentJob extends SyncLenderRepaymentBaseJob {

    @Autowired
    protected LoanRepaymentDetailDAO loanRepaymentDetailDAO;

    @Autowired
    private PPDOpenApiLoanManager ppdOpenApiLoanManager;

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        super.doExecute(shardingContext);
    }

    @Override
    protected PPDLoanRepayStatusEnum getPPDLoanRepayStatusEnum() {
        return PPDLoanRepayStatusEnum.waiting;
    }

    @Override
    protected List<LoanRepaymentDetail> getLoanRepaymentDetailList() {
        LoanRepaymentDetailQuery query = new LoanRepaymentDetailQuery();
        Date dueDateEnd = DateUtil.getCurrentDatetime();
        List<Integer> repayStatusList = new ArrayList<>();
        repayStatusList.add(PPDLoanRepayStatusEnum.waiting.getCode());
        query.setRepayStatusList(repayStatusList);
        query.setDueDateEnd(dueDateEnd);
        List<LoanRepaymentDetail> loanRepaymentDetailList = loanRepaymentDetailDAO.selectListByQuery(query);
        return loanRepaymentDetailList;
    }
}
