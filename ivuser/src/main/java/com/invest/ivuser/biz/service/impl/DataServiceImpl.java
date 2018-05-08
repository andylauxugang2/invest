package com.invest.ivuser.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivuser.biz.manager.*;
import com.invest.ivuser.biz.service.DataService;
import com.invest.ivuser.common.LoanOverdueTypeEnum;
import com.invest.ivuser.common.UserPolicyTypeEnum;
import com.invest.ivuser.dao.query.UserLoanRecordQuery;
import com.invest.ivuser.model.entity.*;
import com.invest.ivuser.model.result.DataResult;
import com.invest.ivuser.model.vo.LoanRepaymentDetailVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.invest.ivuser.common.LoanOverdueTypeEnum.findByCode;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class DataServiceImpl implements DataService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BidAnalysisManager bidAnalysisManager;

    @Autowired
    private LoanDetailManager loanDetailManager;

    @Autowired
    private UserLoanManager userLoanManager;

    @Autowired
    private LoanPolicyManager loanPolicyManager;

    @Autowired
    private LoanRepaymentDetailManager loanRepaymentDetailManager;

    @Autowired
    private LoanOverdueDetailManager loanOverdueDetailManager;

    @Override
    public DataResult getOverdueAnalysisById(Long id) {
        DataResult result = new DataResult();
        try {
            BidAnalysis bidAnalysis = bidAnalysisManager.getBidAnalysisById(id);
            result.setBidAnalysis(bidAnalysis);
        } catch (Exception e) {
            logger.error("查询逾期分析数据失败,id=" + id, e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public DataResult getOverdueAnalysisList(BidAnalysis bidAnalysis) {
        DataResult result = new DataResult();
        try {
            List<BidAnalysis> list = bidAnalysisManager.getBidAnalysisList(bidAnalysis);
            result.setBidAnalysisList(list);
        } catch (Exception e) {
            logger.error("查询逾期分析数据失败,bidAnalysis=" + JSONObject.toJSONString(bidAnalysis), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public DataResult getOverdueAnalysisDetailList(Long analysisId, Short overdueType) {
        DataResult result = new DataResult();
        LoanOverdueTypeEnum overdueTypeEnum = findByCode(overdueType);
        if (overdueTypeEnum == null) {
            logger.error("查询overdueTypeEnum为空,overdueType=" + overdueType);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }
        //查询统计记录
        BidAnalysis bidAnalysis = bidAnalysisManager.getBidAnalysisById(analysisId);
        if (bidAnalysis == null) {
            logger.error("查询BidAnalysis记录为空,id=" + analysisId);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }

        //TODO 保存查询快照 下次直接查

        Date month = DateUtil.stringToDate(bidAnalysis.getMonth(), DateUtil.DATE_FORMAT_DATE_MONTH_COMMON); //2017-10-01 00:00:00
        Date beginTime = month;
        Date endTime = DateUtil.dateAdd(beginTime, 1, Calendar.MONTH);
        Long userId = bidAnalysis.getUserId();
        String username = bidAnalysis.getUsername();

        try {
            List<LoanOverdueDetail> list = loanOverdueDetailManager.getLoanOverdueDetailList(beginTime, endTime, userId, username, overdueTypeEnum.getCode());
            if (CollectionUtils.isEmpty(list)) {
                return result;
            }

            List<LoanRepaymentDetailVO> resultList = new ArrayList<>();
            //数据不多 挨条组装
            list.stream().forEach(o -> resultList.add(buildLoanRepaymentDetailVO(o)));
            result.setLoanRepaymentDetailVOList(resultList);
        } catch (Exception e) {
            logger.error("查询逾期分析详情失败,userId=" + userId + ",username=" + username, e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    private LoanRepaymentDetailVO buildLoanRepaymentDetailVO(LoanOverdueDetail loanOverdueDetail) {
        LoanRepaymentDetailVO vo = new LoanRepaymentDetailVO();
        Integer listingId = loanOverdueDetail.getListingId();
        Long userId = loanOverdueDetail.getUserId();
        String username = loanOverdueDetail.getUsername();
        //组装所属用户基本信息
        vo.setUserId(userId);
        vo.setUsername(username);
        //组装标信息
        LoanDetail loanDetail = loanDetailManager.getLoanDetailByListingId(listingId);
        if (loanDetail != null) {
            vo.setListingId(listingId);
            vo.setAmount(loanDetail.getAmount());
            vo.setMonth(loanDetail.getMonths());
            vo.setRate(loanDetail.getRate());
            vo.setCreditCode(loanDetail.getCreditCode());
            //查询投标记录
            UserLoanRecordQuery userLoanRecordQuery = new UserLoanRecordQuery();
            userLoanRecordQuery.setUserId(userId);
            userLoanRecordQuery.setUsername(username);
            userLoanRecordQuery.setLoanId(listingId);
            List<UserLoanRecord> userLoanRecordList = userLoanManager.getUserLoanRecordList(userLoanRecordQuery);
            if (CollectionUtils.isNotEmpty(userLoanRecordList)) {
                //组装投标信息
                UserLoanRecord userLoanRecord = userLoanRecordList.get(0);
                vo.setBidAmount(userLoanRecord.getParticipationAmount());
                vo.setBidTime(DateUtil.dateToString(userLoanRecord.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
                //组装策略信息
                Long policyId = userLoanRecord.getPolicyId();
                LoanPolicy loanPolicy = loanPolicyManager.getLoanPolicyById(policyId);
                if (loanPolicy != null) {
                    vo.setPolicyId(policyId);
                    vo.setPolicyName(loanPolicy.getName());
                    vo.setPolicyType(UserPolicyTypeEnum.findByCode(loanPolicy.getPolicyType()).getDesc());
                }
            }
        }

        //查询还款计划
        LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailManager.getLoanRepaymentDetailById(loanOverdueDetail.getRepaymentDetailId());
        //组装还款计划-逾期信息
        vo.setOverdueDays(loanRepaymentDetail.getOverdueDays());
        vo.setRepay(BigDecimal.valueOf(loanRepaymentDetail.getRepayPrincipal()).add(BigDecimal.valueOf(loanRepaymentDetail.getRepayInterest())).doubleValue());
        vo.setOwing(BigDecimal.valueOf(loanRepaymentDetail.getOwingPrincipal()).add(BigDecimal.valueOf(loanRepaymentDetail.getOwingInterest())).doubleValue());
        vo.setOwingOverdue(loanRepaymentDetail.getOwingOverdue());
        return vo;
    }
}
