package com.invest.ivppdgateway.controller;

import com.invest.ivppad.biz.service.ppdopenapi.LoanService;
import com.invest.ivppad.model.http.response.*;
import com.invest.ivppad.model.param.*;
import com.invest.ivppad.model.result.PPDOpenApiLoanResult;
import com.invest.ivppdgateway.base.BaseController;
import com.invest.ivppdgateway.common.CodeEnum;
import com.invest.ivppdgateway.model.request.*;
import com.invest.ivppdgateway.model.response.APIResponse;
import com.invest.ivuser.model.entity.UserLoanRecord;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/ppd/loan")
public class LoanController extends BaseController {

    @Resource
    private LoanService loanService;

    /**
     * 获取散标可投标列表
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<List<PPDOpenApiLoanListResponse.LoanInfo>> list(@RequestBody GetLoanListReq getLoanListReq,
                                                                       HttpServletRequest request, HttpServletResponse response) {
        APIResponse<List<PPDOpenApiLoanListResponse.LoanInfo>> result = new APIResponse<>();
        String userName = getLoanListReq.getUserName();
        PPDOpenApiGetLoanListParam param = new PPDOpenApiGetLoanListParam();
        param.setUserName(userName);
        param.setPageIndex(getLoanListReq.getPageIndex());
        Date startTime = getLoanListReq.getStartDateTime();
        if (startTime != null) {
            param.setStartDateTime(new DateTime(startTime.getTime()));
        }
        PPDOpenApiLoanResult loanResult = loanService.getLoanList(param);
        if (loanResult.isFailed()) {
            logger.error("获取散标可投标列表失败,userName={},error={}", userName, loanResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loanResult.getErrorMsg());
        }
        List<PPDOpenApiLoanListResponse.LoanInfo> loanInfoList = loanResult.getLoanInfoList();
        int size = 0;
        if (CollectionUtils.isNotEmpty(loanInfoList)) size = loanInfoList.size();
        logger.info("获取散标可投标列表成功,size={},username={}", size, userName);
        result.setData(loanInfoList);
        return result;
    }


    /**
     * 批量获取散标详情
     */
    @RequestMapping(value = "/batchDetailList", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<List<PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail>> batchDetailList(@RequestBody GetLoanListDetailBatchReq getLoanListDetailBatchReq,
                                                                                                         HttpServletRequest request, HttpServletResponse response) {
        APIResponse<List<PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail>> result = new APIResponse<>();
        List<Integer> listingIds = getLoanListDetailBatchReq.getListingIds();
        if (CollectionUtils.isEmpty(listingIds)) {
            logger.error("批量获取散标详情失败,参数listids为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        Long userId = getLoanListDetailBatchReq.getUserId();
        if (userId == null) {
            logger.error("批量获取散标详情失败,参数userId为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        String userName = getLoanListDetailBatchReq.getUserName();
        PPDOpenApiGetLoanListingDetailBatchParam param = new PPDOpenApiGetLoanListingDetailBatchParam();
        param.setUserName(userName);
        param.setListingIds(listingIds);

        PPDOpenApiLoanResult loanResult = loanService.getLoanListingDetailBatch(param);
        if (loanResult.isFailed()) {
            logger.error("批量获取散标详情失败,userName={},error={}", userName, loanResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loanResult.getErrorMsg());
        }
        List<PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail> loanListingDetailList = loanResult.getLoanListingDetailList();
        int size = 0;
        if (CollectionUtils.isNotEmpty(loanListingDetailList)) size = loanListingDetailList.size();
        logger.info("批量获取散标详情成功,size={},username={}", size, userName);
        result.setData(loanListingDetailList);
        return result;
    }

    /**
     * 批量查询散标投资状态
     */
    @RequestMapping(value = "/batchStatusList", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<List<PPDOpenApiLoanListingStatusBatchResponse.LoanListingStatus>> batchStatusList(@RequestBody GetLoanListStatusBatchReq getLoanListStatusBatchReq,
                                                                                                         HttpServletRequest request, HttpServletResponse response) {
        APIResponse<List<PPDOpenApiLoanListingStatusBatchResponse.LoanListingStatus>> result = new APIResponse<>();
        List<Integer> listingIds = getLoanListStatusBatchReq.getListingIds();
        if (CollectionUtils.isEmpty(listingIds)) {
            logger.error("批量查询散标投资状态失败,参数listids为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        String userName = getLoanListStatusBatchReq.getUserName();
        PPDOpenApiGetLoanListingStatusBatchParam param = new PPDOpenApiGetLoanListingStatusBatchParam();
        param.setUserName(userName);
        param.setListingIds(listingIds);

        PPDOpenApiLoanResult loanResult = loanService.getLoanListingStatusBatch(param);
        if (loanResult.isFailed()) {
            logger.error("批量查询散标投资状态失败,userName={},error={}", userName, loanResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loanResult.getErrorMsg());
        }
        List<PPDOpenApiLoanListingStatusBatchResponse.LoanListingStatus> loanListingStatusList = loanResult.getLoanListingStatusList();
        int size = 0;
        if (CollectionUtils.isNotEmpty(loanListingStatusList)) size = loanListingStatusList.size();
        logger.info("批量查询散标投资状态成功,size={},username={}", size, userName);
        result.setData(loanListingStatusList);
        return result;
    }

    /**
     * 获取用户投标记录
     */
    @RequestMapping(value = "/userLoanBidList", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<PPDOpenApiLoanUserBidListResponse> userLoanBidList(@RequestBody GetUserLoanBidListReq getUserLoanBidListReq,
                                                                          HttpServletRequest request, HttpServletResponse response) {
        String userName = getUserLoanBidListReq.getUserName();
        if (StringUtils.isEmpty(userName)) {
            logger.error("获取用户投标记录失败,参数userName=为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        APIResponse<PPDOpenApiLoanUserBidListResponse> result = new APIResponse<>();
        PPDOpenApiGetUserLoanBidListParam param = new PPDOpenApiGetUserLoanBidListParam();
        param.setUserName(userName);
        param.setListingId(getUserLoanBidListReq.getListingId());
        param.setStartTime(getUserLoanBidListReq.getStartTime());
        param.setEndTime(getUserLoanBidListReq.getEndTime());
        param.setPageIndex(getUserLoanBidListReq.getPageIndex());
        param.setPageSize(getUserLoanBidListReq.getPageSize());
        PPDOpenApiLoanResult loanResult = loanService.getUserLoanBidList(param);
        if (loanResult.isFailed()) {
            logger.error("获取用户投标记录失败,userName={},error={}", userName, loanResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loanResult.getErrorMsg());
        }
        PPDOpenApiLoanUserBidListResponse userLoanBidList = loanResult.getUserLoanBidList();
        result.setData(userLoanBidList);
        return result;
    }

    /**
     * 批量获取散标投资记录
     */
    @RequestMapping(value = "/loanBidRecordList", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<List<PPDOpenApiLoanListingBidRecordBatchResponse.ListingBidDetail>> loanBidRecordList(@RequestBody GetLoanBidRecordBatchReq getLoanBidRecordBatchReq,
                                                                                                             HttpServletRequest request, HttpServletResponse response) {
        String userName = getLoanBidRecordBatchReq.getUserName();
        if (StringUtils.isEmpty(userName)) {
            logger.error("批量获取散标投资记录失败,参数userName=为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        APIResponse<List<PPDOpenApiLoanListingBidRecordBatchResponse.ListingBidDetail>> result = new APIResponse<>();
        PPDOpenApiGetLoanListingBidRecordBatchParam param = new PPDOpenApiGetLoanListingBidRecordBatchParam();
        param.setUserName(userName);
        param.setListingIds(getLoanBidRecordBatchReq.getListingIds());
        PPDOpenApiLoanResult loanResult = loanService.getLoanListingBidRecordList(param);
        if (loanResult.isFailed()) {
            logger.error("批量获取散标投资记录失败,userName={},error={}", userName, loanResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loanResult.getErrorMsg());
        }
        List<PPDOpenApiLoanListingBidRecordBatchResponse.ListingBidDetail> listingBidDetails = loanResult.getLoanListingBidRecordList();
        result.setData(listingBidDetails);
        return result;
    }

    /**
     * 获取用户投资列表的还款情况 逾期情况 等
     * 可根据还款期数查询 指定期数的还款明细
     */
    @RequestMapping(value = "/userLoanRepayment", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<List<PPDOpenApiLoanLenderRepaymentResponse.LoanPaymentDetail>> userLoanRepayment(@RequestBody GetUserLoanRepaymentReq getUserLoanRepaymentReq,
                                                                                                        HttpServletRequest request, HttpServletResponse response) {
        String userName = getUserLoanRepaymentReq.getUserName();
        if (StringUtils.isEmpty(userName)) {
            logger.error("获取用户投资列表的还款情况失败,参数userName=为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        Integer listingId = getUserLoanRepaymentReq.getListingId();
        if (listingId == null) {
            logger.error("获取用户投资列表的还款情况失败,参数listingId为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        APIResponse<List<PPDOpenApiLoanLenderRepaymentResponse.LoanPaymentDetail>> result = new APIResponse<>();

        PPDOpenApiGetUserLoanRepaymentParam param = new PPDOpenApiGetUserLoanRepaymentParam();
        param.setUserName(userName);
        param.setListingId(listingId);
        String periods = getUserLoanRepaymentReq.getPeriods();
        if (StringUtils.isNoneEmpty(periods)) param.setPeriods(periods);

        PPDOpenApiLoanResult loanResult = loanService.getUserLoanRepaymentDetail(param);
        if (loanResult.isFailed()) {
            logger.error("获取用户投资列表的还款情况失败,userName={},listingId={},error={}", userName, listingId, loanResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loanResult.getErrorMsg());
        }
        List<PPDOpenApiLoanLenderRepaymentResponse.LoanPaymentDetail> userLoanPaymentDetailList = loanResult.getUserLoanPaymentDetailList();
        result.setData(userLoanPaymentDetailList);
        return result;
    }

    /**
     * 投标
     */
    @RequestMapping(value = "/gotoBidLoan", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<UserLoanRecord> gotoBidLoan(@RequestBody UserBidLoanReq userBidLoanReq,
                                                   HttpServletRequest request, HttpServletResponse response) {
        String userName = userBidLoanReq.getUserName();
        if (StringUtils.isEmpty(userName)) {
            logger.error("投标失败,参数userName=为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        Integer listingId = userBidLoanReq.getListingId();
        if (listingId == null) {
            logger.error("投标失败,参数listingId为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        APIResponse<UserLoanRecord> result = new APIResponse<>();

        PPDOpenApiBidLoanParam param = new PPDOpenApiBidLoanParam();
        param.setUserName(userName);
        param.setListingId(listingId);
        param.setAmount(userBidLoanReq.getAmount());
        param.setUseCoupon(String.valueOf(userBidLoanReq.getUseCoupon())); //如果空对象,则转为null 传给openapi接口 标识无优惠券
        PPDOpenApiLoanResult loanResult = loanService.bidLoan(param);

        if (loanResult.isFailed()) {
            logger.error("投标失败,userName={},listingId={},amount={},error={}", userName, listingId, userBidLoanReq.getAmount(), loanResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loanResult.getErrorMsg());
        }

        UserLoanRecord userLoanRecord = loanResult.getUserLoanRecord();
        result.setData(userLoanRecord);
        return result;
    }
}
