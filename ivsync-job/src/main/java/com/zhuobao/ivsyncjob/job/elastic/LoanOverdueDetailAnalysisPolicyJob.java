package com.zhuobao.ivsyncjob.job.elastic;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivuser.common.LoanOverdueTypeEnum;
import com.invest.ivuser.dao.db.BidAnalysisPolicyDAO;
import com.invest.ivuser.model.entity.BidAnalysisPolicy;
import com.zhuobao.ivsyncjob.biz.manager.LoanOverdueManager;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 每天1点执行
 * 12-2 逾期10天 推1个月11-2减去10天 10-23起息时间 这一天投了100个标 查询这一天的还款计划有逾期10天的2个 那么12-2来看 逾期率=2/100(一个username)
 * {"analysisDate":"2017-08-01 00:00:00"}
 * 属于增量更新
 * TODO 读从库
 * Created by xugang on 2017/9/18.
 */
@Component
public class LoanOverdueDetailAnalysisPolicyJob extends ElasticBaseJob {

    private static final String JOB_PARAM_NAME_ANALYSISDATE = "analysisDate";

    @Autowired
    private LoanOverdueManager loanOverdueManager;
    @Autowired
    private BidAnalysisPolicyDAO bidAnalysisPolicyDAO;

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
                //按天统计每个策略投标总数
                analysisPolicyBidLoan(DateUtil.dateAdd(analysisDate, i, Calendar.DATE));
            }
        } else {
            //统计今天每个策略投标总数
            analysisPolicyBidLoan(now);
        }

    }

    private void analysisPolicyBidLoan(Date date) {
        Date end = DateUtils.truncate(date, Calendar.DATE);
        Date begin = DateUtil.dateAdd(end, -1, Calendar.DATE);
        try {
            //查出一天的汇总数据
            List<BidAnalysisPolicy> bidAnalysisPolicyList = bidAnalysisPolicyDAO.selectSummaryRecordLoanDetail(begin, end);
            if (CollectionUtils.isEmpty(bidAnalysisPolicyList)) {
                logger.info("查询一天策略汇总数据为空,begin={},end={}", begin, end);
                return;
            }
            //更新投标汇总数据 如果有update无insert
            updateAnalysisPolicy(bidAnalysisPolicyList);
        } catch (Exception e) {
            logger.error("统计一天的策略投标总数失败,date=" + DateUtil.dateToString(date), e);
        }
    }

    private void updateAnalysisPolicy(List<BidAnalysisPolicy> bidAnalysisPolicyList) {
        bidAnalysisPolicyList.stream().forEach(o -> {
            Long userId = o.getUserId();
            String username = o.getUsername();
            Long policyId = o.getPolicyId();
            //查询表是否存在 unique key = userId + username + policyId
            BidAnalysisPolicy bidAnalysisPolicy = bidAnalysisPolicyDAO.selectOneByUniqueKey(userId, username, policyId);
            if (bidAnalysisPolicy == null) {
                bidAnalysisPolicy = buildBidAnalysisPolicy(o);
                bidAnalysisPolicyDAO.insert(bidAnalysisPolicy);
            } else {
                /*//如果本次更新增量与上次更新同一天,则不许更新
                if (DateUtil.truncate2Day(bidAnalysisPolicy.getUpdateTime()).equals(DateUtil.truncate2Day(now))) {
                    return;
                }*/
                buildBidAnalysisPolicyIncrement(o, bidAnalysisPolicy);
                //每天增量添加
                bidAnalysisPolicyDAO.updateByUniqueKey(bidAnalysisPolicy);
            }

        });
    }

    private void buildBidAnalysisPolicyIncrement(BidAnalysisPolicy o, BidAnalysisPolicy bidAnalysisPolicy) {
        //该天计算增量和历史总值
        bidAnalysisPolicy.setBidAmountTotal(bidAnalysisPolicy.getBidAmountTotal() + o.getBidAmountTotal());
        bidAnalysisPolicy.setBidCountTotal(bidAnalysisPolicy.getBidCountTotal() + o.getBidCountTotal());
        int oldBidCount = bidAnalysisPolicy.getBidCountTotal();
        int newBidCount = o.getBidCountTotal();
        int totalBidAcount = oldBidCount + newBidCount;
        double rateAvg = ((BigDecimal.valueOf(bidAnalysisPolicy.getBidRateAvg()).multiply(BigDecimal.valueOf(oldBidCount))).
                add((BigDecimal.valueOf(o.getBidRateAvg()).multiply(BigDecimal.valueOf(newBidCount))))).
                divide(BigDecimal.valueOf(totalBidAcount), 2, BigDecimal.ROUND_HALF_UP).
                doubleValue();
        bidAnalysisPolicy.setBidRateAvg(rateAvg);
        double monthAvg = ((BigDecimal.valueOf(bidAnalysisPolicy.getBidMonthAvg()).multiply(BigDecimal.valueOf(oldBidCount))).
                add((BigDecimal.valueOf(o.getBidMonthAvg()).multiply(BigDecimal.valueOf(newBidCount))))).
                divide(BigDecimal.valueOf(totalBidAcount), 2, BigDecimal.ROUND_HALF_UP).
                doubleValue();
        bidAnalysisPolicy.setBidMonthAvg(monthAvg);
        double ageAvg = ((BigDecimal.valueOf(bidAnalysisPolicy.getBidAgeAvg()).multiply(BigDecimal.valueOf(oldBidCount))).
                add((BigDecimal.valueOf(o.getBidAgeAvg()).multiply(BigDecimal.valueOf(newBidCount))))).
                divide(BigDecimal.valueOf(totalBidAcount), 2, BigDecimal.ROUND_HALF_UP).
                doubleValue();
        bidAnalysisPolicy.setBidAgeAvg(ageAvg);
        double lenderCountAvg = ((BigDecimal.valueOf(bidAnalysisPolicy.getBidLenderCountAvg()).multiply(BigDecimal.valueOf(oldBidCount))).
                add((BigDecimal.valueOf(o.getBidLenderCountAvg()).multiply(BigDecimal.valueOf(newBidCount))))).
                divide(BigDecimal.valueOf(totalBidAcount), 2, BigDecimal.ROUND_HALF_UP).
                doubleValue();
        bidAnalysisPolicy.setBidLenderCountAvg(lenderCountAvg);
    }

    private BidAnalysisPolicy buildBidAnalysisPolicy(BidAnalysisPolicy o) {
        BidAnalysisPolicy result = new BidAnalysisPolicy();
        result.setUserId(o.getUserId());
        result.setUsername(o.getUsername());
        result.setPolicyId(o.getPolicyId());
        result.setPolicyName(o.getPolicyName());
        result.setPolicyType(o.getPolicyType());
        result.setBidCountTotal(o.getBidCountTotal());
        result.setBidAmountTotal(o.getBidAmountTotal());
        result.setBidRateAvg(o.getBidRateAvg());
        result.setBidMonthAvg(o.getBidMonthAvg());
        result.setBidAgeAvg(o.getBidAgeAvg());
        result.setBidLenderCountAvg(o.getBidLenderCountAvg());
        return result;
    }

    private void handleLoanOverdue(Date date) {
        //逾期10天
        loanOverdueManager.handleLoanOverdueDetailAnalysisPolicy(date, LoanOverdueTypeEnum.overdue_10);
        //逾期30天
        loanOverdueManager.handleLoanOverdueDetailAnalysis(date, LoanOverdueTypeEnum.overdue_30);
        //逾期60天
        loanOverdueManager.handleLoanOverdueDetailAnalysis(date, LoanOverdueTypeEnum.overdue_60);
        //逾期90天
        loanOverdueManager.handleLoanOverdueDetailAnalysis(date, LoanOverdueTypeEnum.overdue_90);
    }
}
