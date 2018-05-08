package com.invest.ivmanager.biz.service.impl;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivmanager.biz.service.LoanPolicyService;
import com.invest.ivmanager.model.ListRange;
import com.invest.ivmanager.model.request.LoanPolicyReq;
import com.invest.ivmanager.model.request.SaveLoanPolicyReq;
import com.invest.ivmanager.model.vo.LoanPolicyVO;
import com.invest.ivppad.common.PPDBinExpConstants;
import com.invest.ivppad.common.SchoolTypeEnum;
import com.invest.ivuser.biz.service.UserPolicyService;
import com.invest.ivuser.common.UserPolicyTypeEnum;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.param.LoanPolicyParam;
import com.invest.ivuser.model.result.LoanPolicyResult;
import com.invest.ivuser.model.result.UserPolicyResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 2017/10/13.do best.
 */
@Service("loanPolicyService")
public class LoanPolicyServiceImpl implements LoanPolicyService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserPolicyService userPolicyService;

    @Override
    public ListRange getLoanPolicies(LoanPolicyReq loanPolicyReq) {
        ListRange listRange = new ListRange();
        //查询所有散标 (系统/自定义)
        LoanPolicyParam param = new LoanPolicyParam();
        param.setUserId(loanPolicyReq.getUserId());
        param.setPolicyType(loanPolicyReq.getPolicyType());
        param.setIsDelete(false);
        param.setStatus(loanPolicyReq.getStatus());
        LoanPolicyResult loanPolicyResult = userPolicyService.getLoanPolicies(param);
        if (loanPolicyResult.isFailed()) {
            logger.error("查询散标策略失败");
            throw new RuntimeException(loanPolicyResult.getErrorMsg());
        }

        List<LoanPolicy> loanPolicyList = loanPolicyResult.getLoanPolicies();
        if (CollectionUtils.isEmpty(loanPolicyList)) {
            return listRange;
        }

        listRange.setData(loanPolicyList);
        listRange.setTotalSize(loanPolicyList.size());

        return listRange;
    }

    @Override
    public LoanPolicyVO findLoanPolicyDetailById(Long policyId) {
        UserPolicyResult userPolicyResult = userPolicyService.getLoanPolicy(policyId);
        if (userPolicyResult.isFailed()) {
            throw new RuntimeException(userPolicyResult.getErrorMsg());
        }
        return buildLoanPolicyVO(userPolicyResult.getLoanPolicy());
    }

    private LoanPolicyVO buildLoanPolicyVO(LoanPolicy loanPolicy) {
        LoanPolicyVO vo = new LoanPolicyVO();
        BeanUtils.copyProperties(loanPolicy, vo);
        if (loanPolicy.getGraduateSchoolType() > 0) {
            List<Boolean> flags = new ArrayList<>();
            for (SchoolTypeEnum _enum : SchoolTypeEnum.values()) {
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getGraduateSchoolType(), _enum.getCode())) {
                    flags.add(true);
                } else {
                    flags.add(false);
                }
            }
            vo.setGraduateSchoolTypeFlagList(flags);
        }
        return vo;
    }

    @Override
    public void dropLoanPolicyById(Long policyId) {
        //TODO 操作人ID 使用aop获取登录id 非前端传入
        Result result = userPolicyService.delLoanPolicy(null, policyId);
        if (result.isFailed()) {
            throw new RuntimeException(result.getErrorMsg());
        }
    }

    @Override
    public void saveLoanPolicy(SaveLoanPolicyReq req) {
        //调用rpc服务
        LoanPolicy loanPolicy = buildAddLoanPolicy(req);
        LoanPolicyResult loanPolicyResult = userPolicyService.saveLoanPolicy(loanPolicy);
        if (loanPolicyResult.isFailed()) {
            throw new RuntimeException(loanPolicyResult.getErrorMsg());
        }
    }

    @Override
    public void updateLoanPolicyStatusByIds(Long userId, List<Long> ids, Short status) {
        if (userId == null || CollectionUtils.isEmpty(ids) || status == null) {
            throw new RuntimeException("参数错误");
        }
        List<Long> failedIds = new ArrayList<>();
        LoanPolicy param = new LoanPolicy();
        ids.stream().forEach(id -> {
            param.setId(id);
            param.setStatus(status);
            //只是不展示,不会删除H2缓存策略
            Result result = userPolicyService.modifyLoanPolicy(param);
            if (result.isFailed()) {
                failedIds.add(id);
            }
        });
        if (CollectionUtils.isNotEmpty(failedIds)) {
            throw new RuntimeException("更新有失败行,失败策略编号:" + failedIds);
        }
    }

    private LoanPolicy buildAddLoanPolicy(SaveLoanPolicyReq addUserPolicyReq) {
        LoanPolicy userPolicy = new LoanPolicy();

        Long id = addUserPolicyReq.getId();
        if (id == null) { //新增系统策略
            userPolicy.setUserId(1L);
            userPolicy.setPolicyType(UserPolicyTypeEnum.SYS_LOAN_POLICY.getCode());
        } else { //修改系统策略 or 自定义
            userPolicy.setUserId(addUserPolicyReq.getUserId());
            userPolicy.setPolicyType(UserPolicyTypeEnum.findByCode(addUserPolicyReq.getPolicyType()).getCode());
            if (addUserPolicyReq.getPolicyType() == UserPolicyTypeEnum.SYS_LOAN_POLICY.getCode()) {
                userPolicy.setUserId(null);
            }
        }

        userPolicy.setId(id);

        userPolicy.setName(addUserPolicyReq.getUserPolicyName());
        userPolicy.setSex(addUserPolicyReq.getSex());
        if (addUserPolicyReq.getAmountBegin() != null) userPolicy.setAmountBegin(addUserPolicyReq.getAmountBegin());
        if (addUserPolicyReq.getAmountEnd() != null) userPolicy.setAmountEnd(addUserPolicyReq.getAmountEnd());
        if (addUserPolicyReq.getRateBegin() != null) userPolicy.setRateBegin(addUserPolicyReq.getRateBegin());
        if (addUserPolicyReq.getRateEnd() != null) userPolicy.setRateEnd(addUserPolicyReq.getRateEnd());
        if (addUserPolicyReq.getMonthBegin() != null) userPolicy.setMonthBegin(addUserPolicyReq.getMonthBegin());
        if (addUserPolicyReq.getMonthEnd() != null) userPolicy.setMonthEnd(addUserPolicyReq.getMonthEnd());
        if (addUserPolicyReq.getAgeBegin() != null) userPolicy.setAgeBegin(addUserPolicyReq.getAgeBegin());
        if (addUserPolicyReq.getAgeEnd() != null) userPolicy.setAgeEnd(addUserPolicyReq.getAgeEnd());
        userPolicy.setStatus(LoanPolicy.STATUS_OFF);
        //轮询打标
        List<Long> creditCode = addUserPolicyReq.getCreditCode();
        if (CollectionUtils.isNotEmpty(creditCode)) {
            creditCode.stream().forEach(flag -> userPolicy.setCreditCode(userPolicy.getCreditCode() | flag));
        }
        List<Long> certificate = addUserPolicyReq.getCertificate();
        if (CollectionUtils.isNotEmpty(certificate)) {
            certificate.stream().forEach(flag -> userPolicy.setCertificate(userPolicy.getCertificate() | flag));
        }
        List<Long> studyStyle = addUserPolicyReq.getStudyStyle();
        if (CollectionUtils.isNotEmpty(studyStyle)) {
            studyStyle.stream().forEach(flag -> userPolicy.setStudyStyle(userPolicy.getStudyStyle() | flag));
        }
        List<Long> graduateSchoolType = addUserPolicyReq.getGraduateSchoolType();
        if (CollectionUtils.isNotEmpty(graduateSchoolType)) {
            graduateSchoolType.stream().forEach(flag -> userPolicy.setGraduateSchoolType(userPolicy.getGraduateSchoolType() | flag));
        }
        List<Long> thirdAuthInfo = addUserPolicyReq.getThirdAuthInfo();
        if (CollectionUtils.isNotEmpty(thirdAuthInfo)) {
            thirdAuthInfo.stream().forEach(flag -> userPolicy.setThirdAuthInfo(userPolicy.getThirdAuthInfo() | flag));
        }
        if (addUserPolicyReq.getLoanerSuccessCountBegin() != null)
            userPolicy.setLoanerSuccessCountBegin(addUserPolicyReq.getLoanerSuccessCountBegin());
        if (addUserPolicyReq.getLoanerSuccessCountEnd() != null)
            userPolicy.setLoanerSuccessCountEnd(addUserPolicyReq.getLoanerSuccessCountEnd());
        if (addUserPolicyReq.getWasteCountBegin() != null)
            userPolicy.setWasteCountBegin(addUserPolicyReq.getWasteCountBegin());
        if (addUserPolicyReq.getWasteCountEnd() != null)
            userPolicy.setWasteCountEnd(addUserPolicyReq.getWasteCountEnd());
        if (addUserPolicyReq.getNormalCountBegin() != null)
            userPolicy.setNormalCountBegin(addUserPolicyReq.getNormalCountBegin());
        if (addUserPolicyReq.getNormalCountEnd() != null)
            userPolicy.setNormalCountEnd(addUserPolicyReq.getNormalCountEnd());
        if (addUserPolicyReq.getOverdueLessCountBegin() != null)
            userPolicy.setOverdueLessCountBegin(addUserPolicyReq.getOverdueLessCountBegin());
        if (addUserPolicyReq.getOverdueLessCountEnd() != null)
            userPolicy.setOverdueLessCountEnd(addUserPolicyReq.getOverdueLessCountEnd());
        if (addUserPolicyReq.getOverdueMoreCountBegin() != null)
            userPolicy.setOverdueMoreCountBegin(addUserPolicyReq.getOverdueMoreCountBegin());
        if (addUserPolicyReq.getOverdueMoreCountEnd() != null)
            userPolicy.setOverdueMoreCountEnd(addUserPolicyReq.getOverdueMoreCountEnd());
        if (addUserPolicyReq.getTotalPrincipalBegin() != null)
            userPolicy.setTotalPrincipalBegin(addUserPolicyReq.getTotalPrincipalBegin());
        if (addUserPolicyReq.getTotalPrincipalEnd() != null)
            userPolicy.setTotalPrincipalEnd(addUserPolicyReq.getTotalPrincipalEnd());
        if (addUserPolicyReq.getOwingPrincipalBegin() != null)
            userPolicy.setOwingPrincipalBegin(addUserPolicyReq.getOwingPrincipalBegin());
        if (addUserPolicyReq.getOwingPrincipalEnd() != null)
            userPolicy.setOwingPrincipalEnd(addUserPolicyReq.getOwingPrincipalEnd());
        if (addUserPolicyReq.getAmountToReceiveBegin() != null)
            userPolicy.setAmountToReceiveBegin(addUserPolicyReq.getAmountToReceiveBegin());
        if (addUserPolicyReq.getAmountToReceiveEnd() != null)
            userPolicy.setAmountToReceiveEnd(addUserPolicyReq.getAmountToReceiveEnd());

        if (addUserPolicyReq.getAmountOwingTotalBegin() != null)
            userPolicy.setAmountOwingTotalBegin(addUserPolicyReq.getAmountOwingTotalBegin());
        if (addUserPolicyReq.getAmountOwingTotalEnd() != null)
            userPolicy.setAmountOwingTotalEnd(addUserPolicyReq.getAmountOwingTotalEnd());

        if (addUserPolicyReq.getLastSuccessBorrowDaysBegin() != null)
            userPolicy.setLastSuccessBorrowDaysBegin(addUserPolicyReq.getLastSuccessBorrowDaysBegin());
        if (addUserPolicyReq.getLastSuccessBorrowDaysEnd() != null)
            userPolicy.setLastSuccessBorrowDaysEnd(addUserPolicyReq.getLastSuccessBorrowDaysEnd());

        if (addUserPolicyReq.getRegisterBorrowMonthsBegin() != null)
            userPolicy.setRegisterBorrowMonthsBegin(addUserPolicyReq.getRegisterBorrowMonthsBegin());
        if (addUserPolicyReq.getRegisterBorrowMonthsEnd() != null)
            userPolicy.setRegisterBorrowMonthsEnd(addUserPolicyReq.getRegisterBorrowMonthsEnd());

        if (addUserPolicyReq.getOwingHighestDebtRatioBegin() != null)
            userPolicy.setOwingHighestDebtRatioBegin(addUserPolicyReq.getOwingHighestDebtRatioBegin());
        if (addUserPolicyReq.getOwingHighestDebtRatioEnd() != null)
            userPolicy.setOwingHighestDebtRatioEnd(addUserPolicyReq.getOwingHighestDebtRatioEnd());
        if (addUserPolicyReq.getAmtDebtRatBg() != null)
            userPolicy.setAmtDebtRatBg(addUserPolicyReq.getAmtDebtRatBg());
        if (addUserPolicyReq.getAmtDebtRatEd() != null)
            userPolicy.setAmtDebtRatEd(addUserPolicyReq.getAmtDebtRatEd());
        return userPolicy;
    }
}
