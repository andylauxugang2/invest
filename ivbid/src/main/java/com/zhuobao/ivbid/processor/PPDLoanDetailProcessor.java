package com.zhuobao.ivbid.processor;

import com.invest.ivcommons.util.format.DateFormatUtil;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail;
import com.invest.ivuser.biz.manager.LoanDetailManager;
import com.invest.ivuser.dao.db.UserLoanRecordDAO;
import com.invest.ivuser.model.entity.LoanDetail;
import com.invest.ivuser.model.entity.UserLoanRecord;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * mq consumer 调用
 * 用到service,manager
 * Created by xugang on 2017/8/9.
 */
@Service
public class PPDLoanDetailProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PPDLoanDetailProcessor.class);

    @Autowired
    protected UserLoanRecordDAO userLoanRecordDAO;

    @Autowired
    private LoanDetailManager loanDetailManager;

    public void process(List<LoanListingDetail> loanListingDetailList) {
        if (CollectionUtils.isEmpty(loanListingDetailList)) {
            logger.error("要保存的散标详情列表参数为空");
            return;
        }
        loanListingDetailList.stream().forEach(o -> handleListingDetail(o));
    }

    private void handleListingDetail(LoanListingDetail loanListingDetail) {
        try {
            LoanDetail loanDetail = new LoanDetail();
            //BeanUtils.copyProperties只拷贝不为null的属性
            //BeanUtils.copyProperties(loanListingDetail, loanDetail);
            loanDetail.setListingId(loanListingDetail.getListingId());
            loanDetail.setAmount(loanListingDetail.getAmount());
            loanDetail.setMonths(loanListingDetail.getMonths());
            loanDetail.setRate(loanListingDetail.getCurrentRate());
            loanDetail.setLenderCount(loanListingDetail.getLenderCount());
            loanDetail.setCreditCode(loanListingDetail.getCreditCode());
            loanDetail.setBorrowName(loanListingDetail.getBorrowName());
            loanDetail.setGender(loanListingDetail.getGender());
            loanDetail.setAge(loanListingDetail.getAge());
            loanDetail.setEducationDegree(loanListingDetail.getEducationDegree());
            loanDetail.setGraduateSchool(loanListingDetail.getGraduateSchool());
            loanDetail.setStudyStyle(loanListingDetail.getStudyStyle());
            loanDetail.setSuccessCount(loanListingDetail.getSuccessCount());
            loanDetail.setWasteCount(loanListingDetail.getWasteCount());
            loanDetail.setCancelCount(loanListingDetail.getCancelCount());
            loanDetail.setFailedCount(loanListingDetail.getFailedCount());
            loanDetail.setNormalCount(loanListingDetail.getNormalCount());
            loanDetail.setOverdueLessCount(loanListingDetail.getOverdueLessCount());
            loanDetail.setOverdueMoreCount(loanListingDetail.getOverdueMoreCount());
            loanDetail.setOwingAmount(loanListingDetail.getOwingAmount());
            loanDetail.setOwingPrincipal(loanListingDetail.getOwingPrincipal());
            loanDetail.setHighestDebt(loanListingDetail.getHighestDebt());
            loanDetail.setHighestPrincipal(loanListingDetail.getHighestPrincipal());
            loanDetail.setTotalPrincipal(loanListingDetail.getTotalPrincipal());
            loanDetail.setAmountToReceive(loanListingDetail.getAmountToReceive());
            loanDetail.setThirdAuthFlag(loanListingDetail.getThirdAuthFlag());

            loanDetail.setFistBidTime(DateFormatUtil.convertStr2Date(loanListingDetail.getFistBidTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHMILLIS));
            loanDetail.setLastBidTime(DateFormatUtil.convertStr2Date(loanListingDetail.getLastBidTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHMILLIS));
            loanDetail.setFirstSuccessBorrowTime(DateFormatUtil.convertStr2Date(loanListingDetail.getFirstSuccessBorrowTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND));
            loanDetail.setLastSuccessBorrowTime(DateFormatUtil.convertStr2Date(loanListingDetail.getLastSuccessBorrowTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND));
            loanDetail.setRegisterTime(DateFormatUtil.convertStr2Date(loanListingDetail.getRegisterTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND));
            loanDetailManager.saveLoanDetail(loanDetail);

            //修改投资列表下载状态
            UserLoanRecord userLoanRecord = new UserLoanRecord();
            userLoanRecord.setLoanId(loanDetail.getListingId());
            userLoanRecord.setDownDetailFlag(UserLoanRecord.DOWNDETAILFLAG_YES);
            userLoanRecordDAO.updateListByLoanId(userLoanRecord);
        } catch (Exception e) {
            logger.error("保存标的详情发生异常,listingId=" + loanListingDetail.getListingId(), e);
        }
    }
}
