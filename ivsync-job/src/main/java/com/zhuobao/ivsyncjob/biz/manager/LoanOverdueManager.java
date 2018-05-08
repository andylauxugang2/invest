package com.zhuobao.ivsyncjob.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivuser.common.LoanOverdueTypeEnum;
import com.invest.ivppad.common.PPDLoanRepayStatusEnum;
import com.invest.ivuser.dao.db.*;
import com.invest.ivuser.dao.query.LoanRepaymentDetailQuery;
import com.invest.ivuser.dao.query.UserLoanRecordQuery;
import com.invest.ivuser.model.entity.BidAnalysis;
import com.invest.ivuser.model.entity.BidAnalysisPolicy;
import com.invest.ivuser.model.entity.LoanOverdueDetail;
import com.invest.ivuser.model.entity.LoanRepaymentDetail;
import com.invest.ivuser.model.entity.ext.LoanTotalCount;
import com.invest.ivuser.model.entity.ext.OverdueTotalCount;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xugang on 2017/8/6.
 */
@Service
public class LoanOverdueManager {
    private static Logger logger = LoggerFactory.getLogger(LoanOverdueManager.class);

    @Autowired
    protected LoanOverdueDetailDAO loanOverdueDetailDAO;
    @Autowired
    protected UserLoanRecordDAO userLoanRecordDAO;
    @Autowired
    protected BidAnalysisDAO bidAnalysisDAO;
    @Autowired
    protected BidAnalysisPolicyDAO bidAnalysisPolicyDAO;
    @Autowired
    private LoanRepaymentDetailDAO loanRepaymentDetailDAO;

    /**
     * 处理逾期分析数据
     *
     * @param date
     */
    public void handleLoanOverdueDetailAnalysis(Date date, LoanOverdueTypeEnum ppdLoanOverdueTypeEnum) {
        int overdueDays = ppdLoanOverdueTypeEnum.getValue();
        //获取逾期10天 起止时间:[2017-10-23 00:00:00,2017-10-24 00:00:00)
        Date begin = DateUtils.truncate(date, Calendar.DATE);
        begin = DateUtil.dateAdd(begin, -(30 + overdueDays), Calendar.DATE);
        Date end = DateUtil.dateAdd(begin, 1, Calendar.DATE);

        //获取今天对应之前的逾期n天日期的投标总数
        UserLoanRecordQuery query = new UserLoanRecordQuery();
        query.setBidLoanBeginTime(begin);
        query.setBidLoanEndTime(end);
        List<LoanTotalCount> userLoanTotalAmountList = userLoanRecordDAO.selectLoanTotalCount(query);
        if (CollectionUtils.isEmpty(userLoanTotalAmountList)) {
            logger.info("获取用户投标总数为空,overdueDays={},begin={},end={}", overdueDays, begin, end);
            return;
        }

        String month = DateUtil.dateToString(begin, DateUtil.DATE_FORMAT_DATE_MONTH_COMMON);

        //更新逾期n天的 逾期汇总表 该天投标总数
        userLoanTotalAmountList.stream().forEach(o -> {
            Long userId = o.getUserId();
            String username = o.getUsername();

            BidAnalysis bidAnalysis = new BidAnalysis();
            bidAnalysis.setUserId(userId);
            bidAnalysis.setUsername(username);
            bidAnalysis.setMonth(month);
            //以下都是增量
            switch (ppdLoanOverdueTypeEnum) {
                case overdue_10:
                    bidAnalysis.setOverdue10Total(o.getBidCount());
                    break;
                case overdue_30:
                    bidAnalysis.setOverdue30Total(o.getBidCount());
                    break;
                case overdue_60:
                    bidAnalysis.setOverdue60Total(o.getBidCount());
                    break;
                case overdue_90:
                    bidAnalysis.setOverdue90Total(o.getBidCount());
                    break;
            }

            try {
                bidAnalysisDAO.updateByUniqueKey(bidAnalysis);
            } catch (Exception e) {
                logger.error("更新逾期" + overdueDays + "天的逾期汇总表中投标总数失败,bidAnalysis=" + JSONObject.toJSONString(bidAnalysis), e);
                //TODO 重试
            }
        });

        //查询该天内的所有还款计划有逾期n天的个数
        LoanRepaymentDetailQuery repaymentDetailQuery = new LoanRepaymentDetailQuery();
        List<Integer> repayStatusList = new ArrayList<>();
        repayStatusList.add(PPDLoanRepayStatusEnum.overdue.getCode());
        repayStatusList.add(PPDLoanRepayStatusEnum.waiting.getCode());
        repaymentDetailQuery.setRepayStatusList(repayStatusList);
        //12-6号看逾期10天往前推40天就是10-27投的标 应还款日是12-6往前推10天
        Date begin0 = DateUtils.truncate(date, Calendar.DATE);
        begin0 = DateUtil.dateAdd(begin0, -overdueDays, Calendar.DATE);
        Date end0 = DateUtil.dateAdd(begin0, 1, Calendar.DATE);
        repaymentDetailQuery.setDueDateBegin(begin0);
        repaymentDetailQuery.setDueDateEnd(end0);
        repaymentDetailQuery.setOverdueDays(overdueDays);
        List<OverdueTotalCount> overdueTotalCountList = loanRepaymentDetailDAO.selectOverdueTotalCount(repaymentDetailQuery);
        if (CollectionUtils.isEmpty(overdueTotalCountList)) {
            logger.info("查询还款计划有逾期{}天结果为空,repaymentDetailQuery={}", overdueDays, JSONObject.toJSONString(repaymentDetailQuery));
            return;
        }

        final Date startInterest = begin;
        //更新逾期n天的 逾期汇总表 该天逾期n天个数
        overdueTotalCountList.stream().forEach(o -> {
            Long userId = o.getUserId();
            String username = o.getUsername();
            BidAnalysis bidAnalysis = new BidAnalysis();
            bidAnalysis.setUserId(userId);
            bidAnalysis.setUsername(username);
            bidAnalysis.setMonth(month);
            //以下都是增量
            switch (ppdLoanOverdueTypeEnum) {
                case overdue_10:
                    bidAnalysis.setOverdue10Days(o.getOverdueCount());
                    break;
                case overdue_30:
                    bidAnalysis.setOverdue30Days(o.getOverdueCount());
                    break;
                case overdue_60:
                    bidAnalysis.setOverdue60Days(o.getOverdueCount());
                    break;
                case overdue_90:
                    bidAnalysis.setOverdue90Days(o.getOverdueCount());
                    break;
                default:
                    throw new RuntimeException("overdueDays param error");
            }
            try {
                int line = bidAnalysisDAO.updateByUniqueKey(bidAnalysis);
                //插入逾期详情表
                if (line > 0) {
                    repaymentDetailQuery.setUserId(userId);
                    repaymentDetailQuery.setUsername(username);
                    List<LoanRepaymentDetail> loanRepaymentDetailList = loanRepaymentDetailDAO.selectOverdueDetail(repaymentDetailQuery);
                    List<LoanOverdueDetail> LoanOverdueDetailList = buildLoanOverdueDetailList(loanRepaymentDetailList, ppdLoanOverdueTypeEnum, startInterest);
                    loanOverdueDetailDAO.batchInsert(LoanOverdueDetailList);
                }
            } catch (Exception e) {
                logger.error("更新逾期" + overdueDays + "天的逾期汇总表中逾期总数失败,bidAnalysis=" + JSONObject.toJSONString(bidAnalysis), e);
                //TODO 重试
            }
        });

        logger.info("逾期数据处理成功,overdueDays={},begin={}", overdueDays, DateUtil.dateToString(begin, DateUtil.DATE_FORMAT_DATETIME_COMMON));
    }

    private List<LoanOverdueDetail> buildLoanOverdueDetailList(List<LoanRepaymentDetail> loanRepaymentDetailList, LoanOverdueTypeEnum overdueTypeEnum, Date startInterest) {
        List<LoanOverdueDetail> result = new ArrayList<>();
        for (LoanRepaymentDetail detail : loanRepaymentDetailList) {
            LoanOverdueDetail overdueDetail = new LoanOverdueDetail();
            overdueDetail.setUserId(detail.getUserId());
            overdueDetail.setUsername(detail.getUsername());
            overdueDetail.setListingId(detail.getListingId());
            overdueDetail.setOverdueType(overdueTypeEnum.getCode());
            overdueDetail.setRepaymentDetailId(detail.getId());
            overdueDetail.setStartInterestDate(startInterest);
            result.add(overdueDetail);
        }
        return result;
    }

    /**
     * 处理逾期分析 按策略维度
     * @param date
     * @param overdueTypeEnum
     */
    public void handleLoanOverdueDetailAnalysisPolicy(Date date, LoanOverdueTypeEnum overdueTypeEnum) {
        /*int overdueDays = overdueTypeEnum.getValue();
        //获取逾期10天 起止时间:[2017-10-23 00:00:00,2017-10-24 00:00:00)
        Date begin = DateUtils.truncate(date, Calendar.DATE);
        begin = DateUtil.dateAdd(begin, -(30 + overdueDays), Calendar.DATE);
        Date end = DateUtil.dateAdd(begin, 1, Calendar.DATE);

        //获取今天对应之前的逾期n天日期的投标总数 策略维度
        UserLoanRecordQuery query = new UserLoanRecordQuery();
        query.setBidLoanBeginTime(begin);
        query.setBidLoanEndTime(end);
        List<LoanTotalCount> userLoanTotalAmountList = userLoanRecordDAO.selectLoanTotalCount(query);
        if (CollectionUtils.isEmpty(userLoanTotalAmountList)) {
            logger.info("获取用户投标总数为空,overdueDays={},begin={},end={}", overdueDays, begin, end);
            return;
        }

        String month = DateUtil.dateToString(begin, DateUtil.DATE_FORMAT_DATE_MONTH_COMMON);

        //更新逾期n天的 逾期汇总表 该天投标总数
        userLoanTotalAmountList.stream().forEach(o -> {
            Long userId = o.getUserId();
            String username = o.getUsername();

            BidAnalysisPolicy bidAnalysisPolicy = new BidAnalysisPolicy();
            bidAnalysisPolicy.setUserId(userId);
            bidAnalysisPolicy.setUsername(username);
            bidAnalysisPolicy.setPolicyId(month);
            //以下都是增量
            switch (ppdLoanOverdueTypeEnum) {
                case overdue_10:
                    bidAnalysis.setOverdue10Total(o.getBidCount());
                    break;
                case overdue_30:
                    bidAnalysis.setOverdue30Total(o.getBidCount());
                    break;
                case overdue_60:
                    bidAnalysis.setOverdue60Total(o.getBidCount());
                    break;
                case overdue_90:
                    bidAnalysis.setOverdue90Total(o.getBidCount());
                    break;
            }

            try {
                bidAnalysisDAO.updateByUniqueKey(bidAnalysis);
            } catch (Exception e) {
                logger.error("更新逾期" + overdueDays + "天的逾期汇总表中投标总数失败,bidAnalysis=" + JSONObject.toJSONString(bidAnalysis), e);
                //TODO 重试
            }
        });

        //查询该天内的所有还款计划有逾期n天的个数
        LoanRepaymentDetailQuery repaymentDetailQuery = new LoanRepaymentDetailQuery();
        List<Integer> repayStatusList = new ArrayList<>();
        repayStatusList.add(PPDLoanRepayStatusEnum.overdue.getCode());
        repayStatusList.add(PPDLoanRepayStatusEnum.waiting.getCode());
        repaymentDetailQuery.setRepayStatusList(repayStatusList);
        //12-6号看逾期10天往前推40天就是10-27投的标 应还款日是12-6往前推10天
        Date begin0 = DateUtils.truncate(date, Calendar.DATE);
        begin0 = DateUtil.dateAdd(begin0, -overdueDays, Calendar.DATE);
        Date end0 = DateUtil.dateAdd(begin0, 1, Calendar.DATE);
        repaymentDetailQuery.setDueDateBegin(begin0);
        repaymentDetailQuery.setDueDateEnd(end0);
        repaymentDetailQuery.setOverdueDays(overdueDays);
        List<OverdueTotalCount> overdueTotalCountList = loanRepaymentDetailDAO.selectOverdueTotalCount(repaymentDetailQuery);
        if (CollectionUtils.isEmpty(overdueTotalCountList)) {
            logger.info("查询还款计划有逾期{}天结果为空,repaymentDetailQuery={}", overdueDays, JSONObject.toJSONString(repaymentDetailQuery));
            return;
        }

        final Date startInterest = begin;
        //更新逾期n天的 逾期汇总表 该天逾期n天个数
        overdueTotalCountList.stream().forEach(o -> {
            Long userId = o.getUserId();
            String username = o.getUsername();
            BidAnalysis bidAnalysis = new BidAnalysis();
            bidAnalysis.setUserId(userId);
            bidAnalysis.setUsername(username);
            bidAnalysis.setMonth(month);
            //以下都是增量
            switch (ppdLoanOverdueTypeEnum) {
                case overdue_10:
                    bidAnalysis.setOverdue10Days(o.getOverdueCount());
                    break;
                case overdue_30:
                    bidAnalysis.setOverdue30Days(o.getOverdueCount());
                    break;
                case overdue_60:
                    bidAnalysis.setOverdue60Days(o.getOverdueCount());
                    break;
                case overdue_90:
                    bidAnalysis.setOverdue90Days(o.getOverdueCount());
                    break;
                default:
                    throw new RuntimeException("overdueDays param error");
            }
            try {
                int line = bidAnalysisDAO.updateByUniqueKey(bidAnalysis);
                //插入逾期详情表
                if (line > 0) {
                    repaymentDetailQuery.setUserId(userId);
                    repaymentDetailQuery.setUsername(username);
                    List<LoanRepaymentDetail> loanRepaymentDetailList = loanRepaymentDetailDAO.selectOverdueDetail(repaymentDetailQuery);
                    List<LoanOverdueDetail> LoanOverdueDetailList = buildLoanOverdueDetailList(loanRepaymentDetailList, ppdLoanOverdueTypeEnum, startInterest);
                    loanOverdueDetailDAO.batchInsert(LoanOverdueDetailList);
                }
            } catch (Exception e) {
                logger.error("更新逾期" + overdueDays + "天的逾期汇总表中逾期总数失败,bidAnalysis=" + JSONObject.toJSONString(bidAnalysis), e);
                //TODO 重试
            }
        });

        logger.info("逾期数据处理成功,overdueDays={},begin={}", overdueDays, DateUtil.dateToString(begin, DateUtil.DATE_FORMAT_DATETIME_COMMON));*/
    }
}
