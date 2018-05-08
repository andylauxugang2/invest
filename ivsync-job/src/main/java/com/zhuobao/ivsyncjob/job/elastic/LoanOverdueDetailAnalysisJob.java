package com.zhuobao.ivsyncjob.job.elastic;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivuser.common.LoanOverdueTypeEnum;
import com.zhuobao.ivsyncjob.biz.manager.LoanOverdueManager;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 每天1点执行
 * 12-2 逾期10天 推1个月11-2减去10天 10-23起息时间 这一天投了100个标 查询这一天的还款计划有逾期10天的2个 那么12-2来看 逾期率=2/100(一个username)
 * {"analysisDate":"2017-08-01 00:00:00"}
 * 属于增量更新
 * TODO 读从库
 * Created by xugang on 2017/9/18.
 */
@Component
public class LoanOverdueDetailAnalysisJob extends ElasticBaseJob {

    private static final String JOB_PARAM_NAME_ANALYSISDATE = "analysisDate";

    @Autowired
    private LoanOverdueManager loanOverdueManager;

    /**
     * 截取今天日期,往前推逾期n天的投标+标的还款计划 更新逾期分析总表
     */
    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();

        Date now = DateUtil.getCurrentDatetime();
        JSONObject param = super.parseJobParam(shardingContext.getJobParameter());
        String analysisDateStr = param.getString(JOB_PARAM_NAME_ANALYSISDATE);
        if (StringUtils.isNotBlank(analysisDateStr)) {
            Date analysisDate = DateUtil.stringToDate(analysisDateStr, DateUtil.DATE_FORMAT_DATETIME_COMMON);
            //计算analysisDate到now的时间 按天循环执行
            int days = DateUtil.dayBetween(analysisDate, now);
            if (days < 0) {
                logger.error("计算analysisDate到now相隔天数出错,analysisDateStr=" + analysisDateStr + ",days=" + days);
                return;
            }
            for (int i = 0; i <= days; i++) {
                handleLoanOverdue(DateUtil.dateAdd(analysisDate, i, Calendar.DATE));
            }
        } else {
            handleLoanOverdue(now);
        }

    }

    private void handleLoanOverdue(Date date) {
        //逾期10天
        loanOverdueManager.handleLoanOverdueDetailAnalysis(date, LoanOverdueTypeEnum.overdue_10);
        //逾期30天
        loanOverdueManager.handleLoanOverdueDetailAnalysis(date, LoanOverdueTypeEnum.overdue_30);
        //逾期60天
        loanOverdueManager.handleLoanOverdueDetailAnalysis(date, LoanOverdueTypeEnum.overdue_60);
        //逾期90天
        loanOverdueManager.handleLoanOverdueDetailAnalysis(date, LoanOverdueTypeEnum.overdue_90);
    }

    public static void main(String[] args) {
        String analysisDateStr = "2017-12-01 00:00:00";
        Date analysisDate = DateUtil.stringToDate(analysisDateStr, DateUtil.DATE_FORMAT_DATETIME_COMMON);
        //计算analysisDate到now的时间 按天循环执行
        int days = DateUtil.dayBetween(analysisDate, DateUtil.getCurrentDatetime());
        for (int i = 0; i <= days; i++) {
            System.out.println(DateUtil.dateToString(DateUtil.dateAdd(analysisDate, i, Calendar.DATE), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        }
    }
}
