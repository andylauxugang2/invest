package com.zhuobao.ivsyncjob.job.elastic;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivuser.biz.manager.BidAnalysisManager;
import com.invest.ivuser.dao.db.BidAnalysisDAO;
import com.invest.ivuser.dao.db.UserLoanRecordDAO;
import com.invest.ivuser.model.entity.BidAnalysis;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 一天一次
 * 每天更新当前月份的统计数据
 * 与 LoanOverdueDetailAnalysisJob 一起跑
 * {"initHistory":"true"}
 * 如果历史数据为空则初始化
 * TODO 读从库
 * Created by xugang on 2017/9/18.
 */
@Component
public class BidAnalysisJob extends ElasticBaseJob {

    private static final String JOB_PARAM_NAME_INITHISTORY = "initHistory";

    @Autowired
    protected UserLoanRecordDAO userLoanRecordDAO;

    @Autowired
    private BidAnalysisManager bidAnalysisManager;

    @Autowired
    private BidAnalysisDAO bidAnalysisDAO;

    /**
     * 跑一个月的数据 月初跑上个月的数据
     */
    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();

        //初始化历史月份的统计数据
        JSONObject param = super.parseJobParam(shardingContext.getJobParameter());
        String initHistory = param.getString(JOB_PARAM_NAME_INITHISTORY);
        if (StringUtils.isNotBlank(initHistory) && "true".equalsIgnoreCase(initHistory)) {
            initHistory();
        }

        //查询当前月份最新统计数据
        Date begin = DateUtil.getCurrentDatetime();
        //本月月初日期
        begin = DateUtils.truncate(begin, Calendar.MONTH);
        //下月月初日期
        Date end = DateUtil.dateAdd(begin, 1, Calendar.MONTH);
        List<BidAnalysis> bidAnalysisList = bidAnalysisDAO.selectSummaryRecordLoanDetail(begin, end);
        if (CollectionUtils.isEmpty(bidAnalysisList)) {
            logger.info("获取本月投资记录汇总明细为空,from={},end={}", begin, end);
            return;
        }

        //更新本月最新的投标汇总数据
        updateBidAnalysis(bidAnalysisList);
    }

    private void updateBidAnalysis(List<BidAnalysis> bidAnalysisList) {
        bidAnalysisList.stream().forEach(o -> {
            Long userId = o.getUserId();
            String username = o.getUsername();
            String month = o.getMonth();
            try {
                //先删除
                bidAnalysisDAO.deleteByUniqueKey(userId, username, month);
                //再新增
                BidAnalysis bidAnalysis = buildBidAnalysis(o);
                bidAnalysisManager.saveBidAnalysis(bidAnalysis);
                //获取从db返回的id
                o.setId(bidAnalysis.getId());
            } catch (Exception e) {
                logger.error("更新投标汇总数据失败,BidAnalysis=" + JSONObject.toJSONString(o), e);
                //TODO log 重试
            }
        });
    }

    private BidAnalysis buildBidAnalysis(BidAnalysis o) {
        BidAnalysis overdueAnalysis = new BidAnalysis();
        overdueAnalysis.setUserId(o.getUserId());
        overdueAnalysis.setUsername(o.getUsername());
        overdueAnalysis.setMonth(o.getMonth());
        overdueAnalysis.setBidCountTotal(o.getBidCountTotal());
        overdueAnalysis.setBidAmountTotal(o.getBidAmountTotal());
        overdueAnalysis.setBidMonthAvg(o.getBidMonthAvg());
        overdueAnalysis.setBidRateAvg(o.getBidRateAvg());
        overdueAnalysis.setBidAgeAvg(o.getBidAgeAvg());
        overdueAnalysis.setBidLenderCountAvg(o.getBidLenderCountAvg());
        return overdueAnalysis;
    }

    private void initHistory() {
        try {
            //查出所有的汇总数据
            List<BidAnalysis> bidAnalysisList = bidAnalysisDAO.selectSummaryRecordLoanDetail(null, null);
            //只初始化历史数据,不包含本月
            Date now = DateUtil.getCurrentDatetime();
            String month = DateUtil.dateToString(now, DateUtil.DATE_FORMAT_DATE_MONTH_COMMON);
            bidAnalysisList = bidAnalysisList.stream().filter(bidAnalysis -> !bidAnalysis.getMonth().equals(month)).collect(Collectors.toList());
            //更新投标汇总数据
            updateBidAnalysis(bidAnalysisList);
        } catch (Exception e) {
            logger.error("历史数据为空则初始化失败", e);
        }

    }

}
