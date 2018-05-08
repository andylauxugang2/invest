package com.invest.ivusergateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.MoneyUtil;
import com.invest.ivppad.common.PPDConponStatusEnum;
import com.invest.ivuser.biz.service.UserLoanService;
import com.invest.ivuser.common.UserPolicyTypeEnum;
import com.invest.ivuser.model.entity.UserLoanRecord;
import com.invest.ivuser.model.param.UserLoanRecordParam;
import com.invest.ivuser.model.result.UserLoanResult;
import com.invest.ivusergateway.base.BaseController;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.model.response.APIResponse;
import com.invest.ivusergateway.model.vo.UserLoanRecordVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/invest")
public class InvestController extends BaseController {

    @Resource
    private UserLoanService userLoanService;

    /**
     * 投资记录查询
     */
    @RequestMapping(value = "/getInvestRecord", method = RequestMethod.GET, produces = {"application/json"})
    public APIResponse<List<UserLoanRecordVO>> getInvestRecord(@RequestParam(name = "userId") Long userId,
                                                               @RequestParam(name = "thirdUserUUID", required = false) String thirdUserUUID,
                                                               @RequestParam(name = "policyType", required = false) Short policyType,
                                                               @RequestParam(name = "bidLoanBeginTime", required = false) String bidLoanBeginTime,
                                                               @RequestParam(name = "bidLoanEndTime", required = false) String bidLoanEndTime,
                                                               @RequestParam(name = "page") Integer page,
                                                               @RequestParam(name = "limit") Integer limit,
                                                               HttpServletRequest request) throws Exception {
        APIResponse<List<UserLoanRecordVO>> result = new APIResponse<>();
        if (userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserLoanRecordParam param = new UserLoanRecordParam();
        param.setUserId(userId);
        param.setUsername(thirdUserUUID);
        param.setPolicyType(policyType);
        param.setBidLoanBeginTime(DateUtil.stringToDate(bidLoanBeginTime, DateUtil.DATE_FORMAT_DATETIME_COMMON));
        param.setBidLoanEndTime(DateUtil.stringToDate(bidLoanEndTime, DateUtil.DATE_FORMAT_DATETIME_COMMON));
        param.setPolicyType(policyType);
        param.setPaged(true);
        param.setPage(page);
        param.setLimit(limit);
        UserLoanResult userLoanResult = userLoanService.getUserLoanRecords(param);
        if (userLoanResult.isFailed()) {
            logger.error("查询用户投资记录失败,param={}", JSONObject.toJSONString(param));
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        List<UserLoanRecord> userLoanRecordList = userLoanResult.getUserLoanRecordList();
        if (CollectionUtils.isEmpty(userLoanRecordList)) {
            result.setData(Lists.newArrayList());
            return result;
        }
        List<UserLoanRecordVO> userLoanRecordVOs = new ArrayList<>(userLoanRecordList.size());
        userLoanRecordVOs.addAll(userLoanRecordList.stream().map(this::buildUserLoanRecordVO).collect(Collectors.toList()));
        result.setData(userLoanRecordVOs);
        result.setCount(userLoanResult.getCount());
        return result;
    }

    private UserLoanRecordVO buildUserLoanRecordVO(UserLoanRecord userLoanRecord) {
        UserLoanRecordVO vo = new UserLoanRecordVO();
        vo.setId(userLoanRecord.getId());
        vo.setUsername(userLoanRecord.getUsername());
        vo.setName(userLoanRecord.getPolicyName());
        vo.setAmount(MoneyUtil.formatMoneyRMB(userLoanRecord.getAmount() * 100));
        vo.setCouponStatus(PPDConponStatusEnum.findByCode(userLoanRecord.getCouponStatus()).getDesc());
        vo.setLoanId(userLoanRecord.getLoanId());
        vo.setPolicyId(userLoanRecord.getPolicyId());
        vo.setPolicyType(UserPolicyTypeEnum.findByCode(userLoanRecord.getPolicyType()).getDesc());
        vo.setCreateTime(DateUtil.dateToString(userLoanRecord.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        return vo;
    }

}
