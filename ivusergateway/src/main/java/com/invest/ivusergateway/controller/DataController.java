package com.invest.ivusergateway.controller;

import com.google.common.collect.Lists;
import com.invest.ivcommons.util.format.MoneyUtil;
import com.invest.ivcommons.util.format.NumberUtil;
import com.invest.ivuser.biz.service.DataService;
import com.invest.ivuser.model.entity.BidAnalysis;
import com.invest.ivuser.model.result.DataResult;
import com.invest.ivuser.model.vo.LoanRepaymentDetailVO;
import com.invest.ivusergateway.base.BaseController;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.model.response.APIResponse;
import com.invest.ivusergateway.model.vo.OverdueAnalysisDetailVO;
import com.invest.ivusergateway.model.vo.OverdueAnalysisVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/data")
public class DataController extends BaseController {

    @Resource
    private DataService dataService;

    @RequestMapping(value = "/getOverdueAnalysis", method = RequestMethod.GET, produces = {"application/json"})
    public APIResponse<List<OverdueAnalysisVO>> getUserNotifyMessage(@RequestParam(name = "userId") Long userId,
                                                                     @RequestParam(name = "username", required = false) String username,
                                                                     HttpServletRequest request) throws Exception {
        APIResponse<List<OverdueAnalysisVO>> result = new APIResponse<>();
        if (userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        BidAnalysis param = new BidAnalysis();
        param.setUserId(userId);
        param.setUsername(username);

        DataResult dataResult = dataService.getOverdueAnalysisList(param);
        if (dataResult.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED, dataResult.getErrorMsg());
        }

        List<BidAnalysis> bidAnalysisList = dataResult.getBidAnalysisList();
        if (CollectionUtils.isEmpty(bidAnalysisList)) {
            result.setData(Lists.newArrayList());
            return result;
        }

        List<OverdueAnalysisVO> overdueAnalysisVOs = new ArrayList<>(bidAnalysisList.size());
        overdueAnalysisVOs.addAll(bidAnalysisList.stream().map(this::buildOverdueAnalysisVO).collect(Collectors.toList()));
        overdueAnalysisVOs.add(buildOverdueAnalysisTotalVO(bidAnalysisList));
        result.setData(overdueAnalysisVOs);
        return result;
    }

    private OverdueAnalysisVO buildOverdueAnalysisTotalVO(List<BidAnalysis> bidAnalysisList) {
        OverdueAnalysisVO vo = new OverdueAnalysisVO();
        vo.setUserId(bidAnalysisList.get(0).getUserId());
        vo.setMonth("总计");
        int size = bidAnalysisList.size();
        int bidAmountTotal = 0;
        int bidCountTotal = 0;
        double rateTotal = 0;
        double monthTotal = 0;

        int overdue10Days = 0;
        int overdue10Total = 0;
        int overdue30Days = 0;
        int overdue30Total = 0;
        int overdue60Days = 0;
        int overdue60Total = 0;
        int overdue90Days = 0;
        int overdue90Total = 0;
        Set<String> usernames = new HashSet<>();
        for (BidAnalysis analysis : bidAnalysisList) {
            bidAmountTotal += analysis.getBidAmountTotal();
            bidCountTotal += analysis.getBidCountTotal();
            rateTotal += analysis.getBidRateAvg();
            monthTotal += analysis.getBidMonthAvg();
            overdue10Days += analysis.getOverdue10Days();
            overdue10Total += analysis.getOverdue10Total();
            overdue30Days += analysis.getOverdue30Days();
            overdue30Total += analysis.getOverdue30Total();
            overdue60Days += analysis.getOverdue60Days();
            overdue60Total += analysis.getOverdue60Total();
            overdue90Days += analysis.getOverdue90Days();
            overdue90Total += analysis.getOverdue90Total();
            usernames.add(analysis.getUsername());
        }
        StringBuilder username = new StringBuilder();
        for(String uname : usernames){
            username.append(uname).append(StringUtils.SPACE);
        }
        vo.setUsername(username.toString());
        vo.setBidAmountTotal(MoneyUtil.formatMoneyRMB(bidAmountTotal * 100));
        vo.setBidCountTotal(String.valueOf(bidCountTotal));
        vo.setBidRateAvg(NumberUtil.formatPattern(BigDecimal.valueOf(rateTotal).divide(BigDecimal.valueOf(size), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), "##.##"));
        vo.setBidMonthAvg(NumberUtil.formatPattern(BigDecimal.valueOf(monthTotal).divide(BigDecimal.valueOf(size), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), "##.##"));

        if (overdue10Total != 0) {
            vo.setOverdue10Days(String.valueOf(overdue10Days));
            vo.setOverdue10Total(String.valueOf(overdue10Total));
            vo.setOverdue10DaysRate(NumberUtil.formatDecimalToPercentage(BigDecimal.valueOf(overdue10Days).divide(BigDecimal.valueOf(overdue10Total), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), 2));
        }
        if (overdue30Total != 0) {
            vo.setOverdue30Days(String.valueOf(overdue30Days));
            vo.setOverdue30Total(String.valueOf(overdue30Total));
            vo.setOverdue30DaysRate(NumberUtil.formatDecimalToPercentage(BigDecimal.valueOf(overdue30Days).divide(BigDecimal.valueOf(overdue30Total), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), 2));
        }

        if (overdue60Total != 0) {
            vo.setOverdue60Days(String.valueOf(overdue60Days));
            vo.setOverdue60Total(String.valueOf(overdue60Total));
            vo.setOverdue60DaysRate(NumberUtil.formatDecimalToPercentage(BigDecimal.valueOf(overdue60Days).divide(BigDecimal.valueOf(overdue60Total), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), 2));
        }
        if (overdue90Total != 0) {
            vo.setOverdue90Days(String.valueOf(overdue90Days));
            vo.setOverdue90Total(String.valueOf(overdue90Total));
            vo.setOverdue90DaysRate(NumberUtil.formatDecimalToPercentage(BigDecimal.valueOf(overdue90Days).divide(BigDecimal.valueOf(overdue90Total), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), 2));
        }

        return vo;

    }

    private OverdueAnalysisVO buildOverdueAnalysisVO(BidAnalysis bidAnalysis) {
        OverdueAnalysisVO vo = new OverdueAnalysisVO();
        Integer bidCountTotal = bidAnalysis.getBidCountTotal();
        vo.setId(bidAnalysis.getId());
        vo.setUsername(bidAnalysis.getUsername());
        vo.setMonth(bidAnalysis.getMonth());
        vo.setBidAmountTotal(MoneyUtil.formatMoneyRMB(bidAnalysis.getBidAmountTotal() * 100));
        vo.setBidCountTotal(bidCountTotal.toString());
        vo.setBidRateAvg(NumberUtil.formatPattern(bidAnalysis.getBidRateAvg(), "##.##"));
        vo.setBidMonthAvg(NumberUtil.formatPattern(bidAnalysis.getBidMonthAvg(), "##.##"));
        vo.setBidAgeAvg(NumberUtil.formatPattern(bidAnalysis.getBidAgeAvg(), "##.#"));
        vo.setBidLenderCountAvg(NumberUtil.formatPattern(bidAnalysis.getBidLenderCountAvg(), "##.#"));
        if (bidAnalysis.getOverdue10Total() != 0) {
            vo.setOverdue10Days(bidAnalysis.getOverdue10Days().toString());
            vo.setOverdue10Total(bidAnalysis.getOverdue10Total().toString());
            vo.setOverdue10DaysRate(NumberUtil.formatDecimalToPercentage(BigDecimal.valueOf(bidAnalysis.getOverdue10Days()).divide(BigDecimal.valueOf(bidAnalysis.getOverdue10Total()), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), 2));
        }
        if (bidAnalysis.getOverdue30Total() != 0) {
            vo.setOverdue30Days(bidAnalysis.getOverdue30Days().toString());
            vo.setOverdue30Total(bidAnalysis.getOverdue30Total().toString());
            vo.setOverdue30DaysRate(NumberUtil.formatDecimalToPercentage(BigDecimal.valueOf(bidAnalysis.getOverdue30Days()).divide(BigDecimal.valueOf(bidAnalysis.getOverdue30Total()), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), 2));
        }
        if (bidAnalysis.getOverdue60Total() != 0) {
            vo.setOverdue60Days(bidAnalysis.getOverdue60Days().toString());
            vo.setOverdue60Total(bidAnalysis.getOverdue60Total().toString());
            vo.setOverdue60DaysRate(NumberUtil.formatDecimalToPercentage(BigDecimal.valueOf(bidAnalysis.getOverdue60Days()).divide(BigDecimal.valueOf(bidAnalysis.getOverdue60Total()), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), 2));
        }
        if (bidAnalysis.getOverdue90Total() != 0) {
            vo.setOverdue90Days(bidAnalysis.getOverdue90Days().toString());
            vo.setOverdue90Total(bidAnalysis.getOverdue90Total().toString());
            vo.setOverdue90DaysRate(NumberUtil.formatDecimalToPercentage(BigDecimal.valueOf(bidAnalysis.getOverdue90Days()).divide(BigDecimal.valueOf(bidAnalysis.getOverdue90Total()), 4, BigDecimal.ROUND_HALF_UP).doubleValue(), 2));
        }

        return vo;
    }

    @RequestMapping(value = "/getOverdueAnalysisDetail", method = RequestMethod.GET, produces = {"application/json"})
    public APIResponse<List<OverdueAnalysisDetailVO>> getOverdueAnalysisDetail(@RequestParam(name = "analysisId") Long analysisId,
                                                                               @RequestParam(name = "overdueType") Short overdueType,
                                                                               HttpServletRequest request) throws Exception {
        APIResponse<List<OverdueAnalysisDetailVO>> result = new APIResponse<>();
        if (analysisId == null || overdueType == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        DataResult dataResult = dataService.getOverdueAnalysisDetailList(analysisId, overdueType);
        if (dataResult.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED, dataResult.getErrorMsg());
        }

        List<LoanRepaymentDetailVO> loanRepaymentDetailVOs = dataResult.getLoanRepaymentDetailVOList();
        if (CollectionUtils.isEmpty(loanRepaymentDetailVOs)) {
            result.setData(Lists.newArrayList());
            return result;
        }

        List<OverdueAnalysisDetailVO> overdueAnalysisDetailVOs = new ArrayList<>(loanRepaymentDetailVOs.size());
        overdueAnalysisDetailVOs.addAll(loanRepaymentDetailVOs.stream().map(this::buildOverdueAnalysisDetailVO).collect(Collectors.toList()));
        result.setData(overdueAnalysisDetailVOs);
        return result;
    }

    private OverdueAnalysisDetailVO buildOverdueAnalysisDetailVO(LoanRepaymentDetailVO loanRepaymentDetailVO) {
        OverdueAnalysisDetailVO vo = new OverdueAnalysisDetailVO();
        vo.setUserId(loanRepaymentDetailVO.getUserId());
        vo.setUsername(loanRepaymentDetailVO.getUsername());
        if(loanRepaymentDetailVO != null && loanRepaymentDetailVO.getAmount() != null) vo.setAmount(MoneyUtil.formatMoneyRMB(loanRepaymentDetailVO.getAmount() * 100));
        if(loanRepaymentDetailVO != null && loanRepaymentDetailVO.getBidAmount() != null) vo.setBidAmount(MoneyUtil.formatMoneyRMB(loanRepaymentDetailVO.getBidAmount() * 100));
        vo.setBidTime(loanRepaymentDetailVO.getBidTime());
        vo.setCreditCode(loanRepaymentDetailVO.getCreditCode());
        vo.setListingId(loanRepaymentDetailVO.getListingId());
        vo.setMonth(loanRepaymentDetailVO.getMonth().toString());
        vo.setRate(loanRepaymentDetailVO.getRate().toString());
        vo.setOverdueDays(loanRepaymentDetailVO.getOverdueDays().toString());
        vo.setOwing(MoneyUtil.formatMoneyRMB((int) (loanRepaymentDetailVO.getOwing().doubleValue() * 100)));
        vo.setOwingOverdue(MoneyUtil.formatMoneyRMB((int) (loanRepaymentDetailVO.getOwingOverdue().doubleValue() * 100)));
        vo.setRepay(MoneyUtil.formatMoneyRMB((int) (loanRepaymentDetailVO.getRepay().doubleValue() * 100)));
        vo.setPolicyId(loanRepaymentDetailVO.getPolicyId());
        vo.setPolicyName(loanRepaymentDetailVO.getPolicyName());
        return vo;
    }

}
