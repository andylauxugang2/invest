package com.invest.ivusergateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.NumberUtil;
import com.invest.ivppad.common.*;
import com.invest.ivuser.biz.service.UserPolicyService;
import com.invest.ivuser.common.LoanRiskLevelEnum;
import com.invest.ivuser.common.SexEnum;
import com.invest.ivuser.common.UserPolicyTypeEnum;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.MainPolicy;
import com.invest.ivuser.model.entity.UserMainPolicy;
import com.invest.ivuser.model.entity.UserPolicy;
import com.invest.ivuser.model.entity.ext.UserPolicyDetail;
import com.invest.ivuser.model.param.LoanPolicyParam;
import com.invest.ivuser.model.param.UserPolicyParam;
import com.invest.ivuser.model.result.LoanPolicyResult;
import com.invest.ivuser.model.result.UserPolicyResult;
import com.invest.ivuser.model.vo.UserMainPolicyVO;
import com.invest.ivusergateway.base.BaseController;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.model.request.*;
import com.invest.ivusergateway.model.response.APIResponse;
import com.invest.ivusergateway.model.vo.EnumVO;
import com.invest.ivusergateway.model.vo.LoanPolicyVO;
import com.invest.ivusergateway.model.vo.LoanRiskLevelVO;
import com.invest.ivusergateway.model.vo.UserPolicyDetailVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xugang on 2016/9/6.do best.
 */
@RestController
@RequestMapping(value = "/policy")
public class PolicyController extends BaseController {

    @Resource
    private UserPolicyService userPolicyService;

    @RequestMapping(value = "/getUserMainPolicy", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<UserMainPolicyVO>> getUserMainPolicy(@RequestParam(name = "userId") Long userId,
                                                                 @RequestParam(name = "thirdUserUUID", required = false) String thirdUserUUID) {
        APIResponse<List<UserMainPolicyVO>> result = new APIResponse<>();
        if (userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        List<UserMainPolicyVO> userMainPolicies = userPolicyService.getUserMainPolicies(userId, thirdUserUUID);
        if (CollectionUtils.isEmpty(userMainPolicies)) {
            return APIResponse.createResult(CodeEnum.RESULT_NULL);
        }

        result.setData(userMainPolicies);
        return result;
    }

    @RequestMapping(value = "/getPolicyType", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<EnumVO>> getPolicyType() {
        APIResponse<List<EnumVO>> result = new APIResponse<>();

        List<EnumVO> list = new ArrayList<>();
        for (UserPolicyTypeEnum userPolicyTypeEnum : UserPolicyTypeEnum.values()) {
            EnumVO enumVO = new EnumVO();
            enumVO.setCode(userPolicyTypeEnum.getCode());
            enumVO.setDesc(userPolicyTypeEnum.getDesc());
            list.add(enumVO);
        }
        result.setData(list);
        return result;
    }

    @RequestMapping(value = "/getUserMainPolicyOne", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<UserMainPolicyVO> getUserMainPolicyOne(@RequestParam(name = "userId") Long userId,
                                                              @RequestParam(name = "thirdUserUUID") String thirdUserUUID,
                                                              @RequestParam(name = "mainPolicyId") Long mainPolicyId) {
        APIResponse<UserMainPolicyVO> result = new APIResponse<>();
        if (userId == null || StringUtils.isBlank(thirdUserUUID) || mainPolicyId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserMainPolicyVO userMainPolicy = userPolicyService.getUserMainPolicy(userId, thirdUserUUID, mainPolicyId);
        result.setData(userMainPolicy);
        return result;
    }

    @RequestMapping(value = "/getUserMainPolicies", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<UserMainPolicy>> getUserMainPolicies(@RequestParam(name = "userId") Long userId,
                                                                 @RequestParam(name = "mainPolicyId") Long mainPolicyId) {
        APIResponse<List<UserMainPolicy>> result = new APIResponse<>();
        if (userId == null || mainPolicyId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserMainPolicy userMainPolicy = new UserMainPolicy();
        userMainPolicy.setUserId(userId);
        userMainPolicy.setMainPolicyId(mainPolicyId);
        List<UserMainPolicy> userMainPolicies = userPolicyService.getUserMainPolicies(userMainPolicy);
        result.setData(userMainPolicies);
        return result;
    }

    @RequestMapping(value = "/saveUserMainPolicy", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Long> saveUserMainPolicy(@RequestBody SaveUserMainPolicyReq saveUserMainPolicyReq) {
        APIResponse<Long> result = new APIResponse<>();
        Long userId = saveUserMainPolicyReq.getUserId();
        Long mainPolicyId = saveUserMainPolicyReq.getMainPolicyId();
        String thirdUserUUID = saveUserMainPolicyReq.getThirdUserUUID();
        Integer amountStart = saveUserMainPolicyReq.getAmountStart();
        Integer amountMax = saveUserMainPolicyReq.getAmountMax();
        Integer accountRemain = saveUserMainPolicyReq.getAccountRemain();

        if (userId == null || StringUtils.isBlank(thirdUserUUID) || mainPolicyId == null
                || amountStart == null || amountMax == null || accountRemain == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //前端默认为空传的是0
        if (saveUserMainPolicyReq.getStatus() == null) {
            saveUserMainPolicyReq.setStatus((short) 0);
        }

        UserMainPolicy userMainPolicy = new UserMainPolicy();
        userMainPolicy.setId(saveUserMainPolicyReq.getUserMainPolicyId());
        userMainPolicy.setUserId(userId);
        userMainPolicy.setMainPolicyId(mainPolicyId);
        userMainPolicy.setThirdUserUUID(thirdUserUUID);
        userMainPolicy.setAmountStart(amountStart);
        userMainPolicy.setAmountMax(amountMax);
        userMainPolicy.setAccountRemain(accountRemain);
        userMainPolicy.setStatus(saveUserMainPolicyReq.getStatus());
        //调用rpc服务
        UserMainPolicy savedUserMainPolicy = userPolicyService.saveUserMainPolicy(userMainPolicy);
        if (savedUserMainPolicy.getId() == null) {
            logger.error("保存用户主策略设置失败,userId={},mainPolicyId={},thirdUserUUID={}", userId, mainPolicyId, thirdUserUUID);
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        result.setData(savedUserMainPolicy.getId());
        return result;
    }


    @RequestMapping(value = "/getLoanRiskLevel", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<LoanRiskLevelVO>> getLoanRiskLevel() {
        APIResponse<List<LoanRiskLevelVO>> result = new APIResponse<>();

        List<LoanRiskLevelVO> list = new ArrayList<>();
        for (LoanRiskLevelEnum levelEnum : LoanRiskLevelEnum.values()) {
            LoanRiskLevelVO loanRiskLevelVO = new LoanRiskLevelVO();
            loanRiskLevelVO.setCode(levelEnum.getCode());
            loanRiskLevelVO.setDesc(levelEnum.getDesc());
            list.add(loanRiskLevelVO);
        }
        result.setData(list);
        return result;
    }

    /**
     * 查询用户自定义散标策略
     */
    @RequestMapping(value = "/getUserLoanPolicies", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<UserPolicyDetailVO>> getUserLoanPolicies(@RequestParam(name = "userId") Long userId,
                                                                     @RequestParam(name = "policyType") Short policyType,
                                                                     @RequestParam(name = "thirdUserUUID", required = false) String thirdUserUUID,
                                                                     @RequestParam(name = "riskLevel", required = false) Short riskLevel) {
        APIResponse<List<UserPolicyDetailVO>> result = new APIResponse<>();
        if (userId == null || policyType == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserPolicyParam param = new UserPolicyParam();
        param.setUserId(userId);
        param.setPolicyType(policyType);
        param.setThirdUserUUID(thirdUserUUID);
        param.setRiskLevel(riskLevel);
        UserPolicyResult userPolicyResult = userPolicyService.getUserPolicyDetailList(param);
        if (userPolicyResult.isFailed()) {
            logger.error("查询用户自定义散标策略失败,param={}", JSONObject.toJSONString(param));
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        List<UserPolicyDetail> userPolicyDetailList = userPolicyResult.getUserPolicyDetails();
        if (CollectionUtils.isEmpty(userPolicyDetailList)) {
            result.setData(Lists.newArrayList());
            return result;
        }
        List<UserPolicyDetailVO> userPolicyVOs = new ArrayList<>(userPolicyDetailList.size());
        userPolicyVOs.addAll(userPolicyDetailList.stream().map(this::buildUserPolicyVOs).collect(Collectors.toList()));
        result.setData(userPolicyVOs);
        return result;
    }

    private UserPolicyDetailVO buildUserPolicyVOs(UserPolicyDetail userPolicyDetail) {
        UserPolicyDetailVO vo = new UserPolicyDetailVO();
        //策略id
        vo.setId(userPolicyDetail.getId());
        if (userPolicyDetail.getRiskLevel() != null) {
            vo.setRiskLevelCode(LoanRiskLevelEnum.findByCode(userPolicyDetail.getRiskLevel()).getCode());
            vo.setRiskLevel(LoanRiskLevelEnum.findByCode(userPolicyDetail.getRiskLevel()).getDesc());
        }
        //用户策略主键id
        vo.setUserPolicyId(userPolicyDetail.getUserPolicyId());
        vo.setUsername(userPolicyDetail.getThirdUserUUID());
        vo.setName(userPolicyDetail.getName());
        vo.setBidAmount(userPolicyDetail.getBidAmount());
        vo.setUserPolicyStatus(userPolicyDetail.getUserPolicyStatus());
        vo.setUserPolicyCreateTimeFormat(DateUtil.dateToString(userPolicyDetail.getUserPolicyCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        return vo;
    }


    /**
     * 查询散标策略
     */
    @RequestMapping(value = "/getLoanPolicies", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<LoanPolicyVO>> getLoanPolicies(@RequestParam(name = "userId") Long userId,
                                                           @RequestParam(name = "thirdUserUUID", required = false) String thirdUserUUID,
                                                           @RequestParam(name = "policyType") Short policyType,
                                                           @RequestParam(name = "riskLevel", required = false) Short riskLevel) {
        APIResponse<List<LoanPolicyVO>> result = new APIResponse<>();
        if (userId == null || policyType == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //查询所有散标 (系统/自定义)
        LoanPolicyParam param = new LoanPolicyParam();
        param.setUserId(userId);
        param.setRiskLevel(riskLevel);
        param.setPolicyType(policyType);
        param.setThirdUserUUID(thirdUserUUID);
        param.setStatus(LoanPolicy.STATUS_ON);
        LoanPolicyResult loanPolicyResult = userPolicyService.getLoanPolicies(param);
        if (loanPolicyResult.isFailed()) {
            logger.error("查询散标策略失败,userId={},thirdUserUUID={},riskLevel={}", new Object[]{userId, thirdUserUUID, riskLevel});
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        List<LoanPolicy> loanPolicyList = loanPolicyResult.getLoanPolicies();
        if (CollectionUtils.isEmpty(loanPolicyList)) {
            result.setData(Lists.newArrayList());
            return result;
        }

        List<LoanPolicyVO> loanPolicyVOs = new ArrayList<>(loanPolicyList.size());

        if (policyType == UserPolicyTypeEnum.SYS_LOAN_POLICY.getCode()) {
            //查询用户第三方账号已选择的系统散标策略
            UserPolicyParam userPolicyParam = new UserPolicyParam();
            userPolicyParam.setUserId(userId);
            userPolicyParam.setThirdUserUUID(thirdUserUUID);
            UserPolicyResult userPolicyResult = userPolicyService.getUserPolicies(userPolicyParam);
            List<UserPolicy> userPolicyList = userPolicyResult.getUserPolicies();
            Set<Long> policyIdSet = new HashSet<>();
            userPolicyList.forEach(o -> {
                policyIdSet.add(o.getPolicyId());
            });


            for (LoanPolicy loanPolicy : loanPolicyList) {
                //过滤用户已选择的系统策略
                if (policyIdSet.contains(loanPolicy.getId())) continue;
                loanPolicyVOs.add(buildLoanPolicyVOs(loanPolicy));
            }
        } else if (policyType == UserPolicyTypeEnum.USER_LOAN_POLICY.getCode()) {
            loanPolicyVOs.addAll(loanPolicyList.stream().map(this::buildLoanPolicyVOs).collect(Collectors.toList()));
        }

        result.setData(loanPolicyVOs);
        return result;
    }

    private LoanPolicyVO buildLoanPolicyVOs(LoanPolicy loanPolicy) {
        LoanPolicyVO vo = new LoanPolicyVO();
        vo.setId(loanPolicy.getId()); //策略id
//        vo.setUserPolicyId(loanPolicy.getUserPolicyId()); //用户策略主键id
//        vo.setUsername(loanPolicy.getThirdUserUUID());
        vo.setName(loanPolicy.getName());
        vo.setAmount(buildValueScope(loanPolicy.getAmountBegin(), loanPolicy.getAmountEnd()));
        vo.setMonth(buildValueScope(loanPolicy.getMonthBegin(), loanPolicy.getMonthEnd()));
        vo.setRate(buildValueScope(loanPolicy.getRateBegin(), loanPolicy.getRateEnd()));
        vo.setAge(buildValueScope(loanPolicy.getAgeBegin(), loanPolicy.getAgeEnd()));
        vo.setSex(loanPolicy.getSex() != null ? SexEnum.findByCode(loanPolicy.getSex()).getName() : LoanPolicyVO.VALUE_NO_LIMIT);
        StringBuilder temp;
        //第三方认证展示
        if (loanPolicy.getThirdAuthInfo() > 0) {
            temp = new StringBuilder();
            for (PPDThridAuthValidEnum _enum : PPDThridAuthValidEnum.values()) {
                if (_enum == PPDThridAuthValidEnum.unknown) {
                    continue;
                }
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getThirdAuthInfo(), _enum.getCode()))
                    temp.append(_enum.getDesc()).append(",");
            }
            vo.setThirdAuthInfo(temp.toString().substring(0, temp.length() - 1));
        }

        //学历认证展示
        if (loanPolicy.getCertificate() > 0) {
            temp = new StringBuilder();
            for (PPDCertificateEnum _enum : PPDCertificateEnum.values()) {
                if (_enum == PPDCertificateEnum.unknown) {
                    continue;
                }
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getCertificate(), _enum.getCode()))
                    temp.append(_enum.getDesc()).append(",");
            }
            vo.setCertificate(temp.toString().substring(0, temp.length() - 1));
        }

        //学习形式展示
        if (loanPolicy.getStudyStyle() > 0) {
            temp = new StringBuilder();
            for (PPDStudyStyleEnum _enum : PPDStudyStyleEnum.values()) {
                if (_enum == PPDStudyStyleEnum.unknown) {
                    continue;
                }
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getStudyStyle(), _enum.getCode()))
                    temp.append(_enum.getDesc()).append(",");
            }
            vo.setStudyStyle(temp.toString().substring(0, temp.length() - 1));
        }
        //毕业学校分类
        if (loanPolicy.getGraduateSchoolType() > 0) {
            temp = new StringBuilder();
            for (SchoolTypeEnum _enum : SchoolTypeEnum.values()) {
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getGraduateSchoolType(), _enum.getCode()))
                    temp.append(_enum.getName()).append(",");
            }
            vo.setGraduateSchoolType(temp.toString().substring(0, temp.length() - 1));
        }

        //魔镜等级
        if (loanPolicy.getCreditCode() > 0) {
            temp = new StringBuilder();
            for (PPDCreditCodeEnum _enum : PPDCreditCodeEnum.values()) {
                if (_enum == PPDCreditCodeEnum.UNKNOWN) {
                    continue;
                }
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getCreditCode(), _enum.getCode()))
                    temp.append(_enum.getDesc()).append(",");
            }
            vo.setCreditCode(temp.toString().substring(0, temp.length() - 1));
        }

        vo.setLoanerSuccessCount(buildValueScope(loanPolicy.getLoanerSuccessCountBegin(), loanPolicy.getLoanerSuccessCountEnd()));
        vo.setWasteCount(buildValueScope(loanPolicy.getWasteCountBegin(), loanPolicy.getWasteCountEnd()));
        vo.setNormalCount(buildValueScope(loanPolicy.getNormalCountBegin(), loanPolicy.getNormalCountEnd()));
        vo.setOverdueLessCount(buildValueScope(loanPolicy.getOverdueLessCountBegin(), loanPolicy.getOverdueLessCountEnd()));
        vo.setOverdueMoreCount(buildValueScope(loanPolicy.getOverdueMoreCountBegin(), loanPolicy.getOverdueMoreCountEnd()));
        vo.setTotalPrincipal(buildValueScope(loanPolicy.getTotalPrincipalBegin(), loanPolicy.getTotalPrincipalEnd()));
        vo.setOwingPrincipal(buildValueScope(loanPolicy.getOwingPrincipalBegin(), loanPolicy.getOwingPrincipalEnd()));
        vo.setAmountToReceive(buildValueScope(loanPolicy.getAmountToReceiveBegin(), loanPolicy.getAmountToReceiveEnd()));
        vo.setAmountOwingTotal(buildValueScope(loanPolicy.getAmountOwingTotalBegin(), loanPolicy.getAmountOwingTotalEnd()));
        vo.setLastSuccessBorrowDays(buildValueScope(loanPolicy.getLastSuccessBorrowDaysBegin(), loanPolicy.getLastSuccessBorrowDaysEnd()));
        vo.setRegisterBorrowMonths(buildValueScope(loanPolicy.getRegisterBorrowMonthsBegin(), loanPolicy.getRegisterBorrowMonthsEnd()));
        vo.setOwingHighestDebtRatio(buildValueScope(loanPolicy.getOwingHighestDebtRatioBegin(), loanPolicy.getOwingHighestDebtRatioEnd()));
        vo.setAmtDebtRat(buildValueScope(loanPolicy.getAmtDebtRatBg(), loanPolicy.getAmtDebtRatEd()));

        vo.setValidTime(DateUtil.dateToString(loanPolicy.getValidTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        vo.setCreateTime(DateUtil.dateToString(loanPolicy.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
//        vo.setBidAmount(loanPolicy.getBidAmount());

        //系统标
        if (loanPolicy.getRiskLevel() != null)
            vo.setRiskLevel(LoanRiskLevelEnum.findByCode(loanPolicy.getRiskLevel()).getDesc());
        return vo;
    }

    private String buildValueScope(Integer begin, Integer end) {
        String result = UserPolicyDetailVO.VALUE_NO_LIMIT;
        if (begin != null && end != null) {
            result = begin + "-" + end;
        } else if (begin != null) {
            result = ">=" + begin;
        } else if (end != null) {
            result = "<=" + end;
        }
        return result;
    }

    private String buildValueScope(Double begin, Double end) {
        String pattern = "##.##";
        String result = UserPolicyDetailVO.VALUE_NO_LIMIT;
        if (begin != null && end != null) {
            result = NumberUtil.formatPattern(begin, pattern) + "-" + NumberUtil.formatPattern(end, pattern);
        } else if (begin != null) {
            result = ">=" + NumberUtil.formatPattern(begin, pattern);
        } else if (end != null) {
            result = "<=" + NumberUtil.formatPattern(end, pattern);
        }
        return result;
    }

    /**
     * 查询散标策略详情
     */
    /*@RequestMapping(value = "/getLoanPolicyDetail", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<LoanPolicyVO> getLoanPolicyDetail(@RequestParam(name = "userId") Long userId,
                                                         @RequestParam(name = "policyId") Long policyId) {
        APIResponse<LoanPolicyVO> result = new APIResponse<>();
        if (userId == null || policyId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //TODO 用户是否登录过期

        //查询所有散标 (系统/自定义)
        UserPolicyResult userPolicyResult = userPolicyService.getLoanPolicy(policyId);
        if (userPolicyResult.isFailed()) {
            logger.error("查询散标策略失败,userId={},policyId={}", new Object[]{userId, policyId});
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        LoanPolicy loanPolicy = userPolicyResult.getLoanPolicy();
        if (loanPolicy == null) {
            result.setData(new LoanPolicyVO());
            return result;
        }

        result.setData(buildLoanPolicyVOs(loanPolicy));
        return result;
    }*/

    /**
     * 批量保存 用户选择的散标策略
     */
    @RequestMapping(value = "/addBatchUserLoanPolicy", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Void> addBatchUserLoanPolicy(@RequestBody AddBatchUserLoanPolicyReq req) {
        APIResponse<Void> result = new APIResponse<>();
        Long userId = req.getUserId();
        String thirdUserUUID = req.getThirdUserUUID();
        List<Long> dataIds = req.getDataIds();

        if (userId == null || StringUtils.isBlank(thirdUserUUID) || CollectionUtils.isEmpty(dataIds)) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserPolicyResult userPolicyResult = userPolicyService.addUserPolicyBatch(userId, thirdUserUUID, dataIds);
        if (userPolicyResult.isFailed()) {
            logger.error("保存用户系统散标策略失败,req={}", JSONObject.toJSONString(req));
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userPolicyResult.getErrorMsg());
        }

        return result;
    }

    /**
     * 保存修改用户选择的散标策略
     */
    @RequestMapping(value = "/saveModifyUserLoanPolicy", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Void> saveModifyUserLoanPolicy(@RequestBody SaveModifyUserLoanPolicyReq req) {
        APIResponse<Void> result = new APIResponse<>();
        Long userId = req.getUserId();
        String thirdUserUUID = req.getThirdUserUUID();
        Integer bidAmount = req.getBidAmount();
        Short status = req.getUserPolicyStatus();
        Long userPolicyId = req.getUserLoanPolicyId();
        Long policyId = req.getPolicyId();

        if (userId == null || bidAmount == null || status == null || policyId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        UserPolicy userPolicy = new UserPolicy();
        userPolicy.setId(userPolicyId);
        userPolicy.setUserId(userId);
        userPolicy.setThirdUserUUID(thirdUserUUID);
        userPolicy.setBidAmount(bidAmount);
        userPolicy.setStatus(status);
        userPolicy.setPolicyId(policyId);
        //调用rpc服务
        Result saveResult = userPolicyService.modifyUserPolicy(userPolicy);
        if (saveResult.isFailed()) {
            logger.error("保存用户系统散标策略设置失败,req={}", JSONObject.toJSONString(req));
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), saveResult.getErrorMsg());
        }
        return result;
    }

    /**
     * 批量删除用户选择的散标策略
     */
    @RequestMapping(value = "/removeLoanPolicy", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Void> removeLoanPolicy(@RequestBody RemoveUserLoanPolicyReq req) {
        APIResponse<Void> result = new APIResponse<>();
        Long userId = req.getUserId();
        List<Long> dataIds = req.getUserLoanPolicyIds();
        if (userId == null || CollectionUtils.isEmpty(dataIds)) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        Result delResult = userPolicyService.delLoanPolicy(userId, dataIds);
        if (delResult.isFailed()) {
            logger.error("删除用户自定义散标策略失败,req={}", JSONObject.toJSONString(req));
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), delResult.getErrorMsg());
        }
        return result;
    }

    /**
     * 添加 自定义散标策略
     */
    @RequestMapping(value = "/addLoanPolicy", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Map<String, Object>> addLoanPolicy(@RequestBody AddLoanPolicyReq req) {
        APIResponse<Map<String, Object>> result = new APIResponse<>();
        Long userId = req.getUserId();

        //List<String> thirdUserUUIDList = addLoanPolicyReq.getUserThirdBindInfoList();

        if (userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        LoanPolicy loanPolicy = buildAddLoanPolicy(req);
        LoanPolicyResult loanPolicyResult = userPolicyService.saveLoanPolicy(loanPolicy);
        if (loanPolicyResult.isFailed()) {
            logger.error("添加用户自定义散标策略失败,req={}", JSONObject.toJSONString(req));
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loanPolicyResult.getErrorMsg());
        }

        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("id", loanPolicy.getId());
        resultMap.put("optType", req.getId() == null ? "insert" : "update");
        result.setData(resultMap);
        return result;
    }

    private LoanPolicy buildAddLoanPolicy(AddLoanPolicyReq addUserPolicyReq) {
        LoanPolicy userPolicy = new LoanPolicy();
        userPolicy.setId(addUserPolicyReq.getId());
        userPolicy.setPolicyType(addUserPolicyReq.getPolicyType());
        userPolicy.setName(addUserPolicyReq.getUserPolicyName());
        userPolicy.setUserId(addUserPolicyReq.getUserId());
        userPolicy.setSex(addUserPolicyReq.getSex());
        if (addUserPolicyReq.getAmountBegin() != null) userPolicy.setAmountBegin(addUserPolicyReq.getAmountBegin());
        if (addUserPolicyReq.getAmountEnd() != null) userPolicy.setAmountEnd(addUserPolicyReq.getAmountEnd());
        if (addUserPolicyReq.getRateBegin() != null) userPolicy.setRateBegin(addUserPolicyReq.getRateBegin());
        if (addUserPolicyReq.getRateEnd() != null) userPolicy.setRateEnd(addUserPolicyReq.getRateEnd());
        if (addUserPolicyReq.getMonthBegin() != null) userPolicy.setMonthBegin(addUserPolicyReq.getMonthBegin());
        if (addUserPolicyReq.getMonthEnd() != null) userPolicy.setMonthEnd(addUserPolicyReq.getMonthEnd());
        if (addUserPolicyReq.getAgeBegin() != null) userPolicy.setAgeBegin(addUserPolicyReq.getAgeBegin());
        if (addUserPolicyReq.getAgeEnd() != null) userPolicy.setAgeEnd(addUserPolicyReq.getAgeEnd());
        userPolicy.setStatus(LoanPolicy.STATUS_ON);
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

    /**
     * 查询未添加过该策略的第三方账号 [自定义散标策略]
     */
    @RequestMapping(value = "/getNoSelPolicyThirdUUIDList", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<String>> getNoSelPolicyThirdUUIDList(@RequestParam(name = "userId") Long userId,
                                                                 @RequestParam(name = "policyId") Long policyId) {
        APIResponse<List<String>> result = new APIResponse<>();
        if (userId == null || policyId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        List<String> noSelPolicyThirdUUIDList = userPolicyService.getNoSelPolicyThirdUUIDList(userId, policyId,
                UserPolicyTypeEnum.USER_LOAN_POLICY.getCode(), MainPolicy.MAIN_POLICY_ID_USER_LOAN);
        result.setData(noSelPolicyThirdUUIDList);
        return result;
    }


    /**
     * 挂载 自定义散标策略
     */
    @RequestMapping(value = "/attachUserThirdLoanPolicy", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Void> attachUserThirdLoanPolicy(@RequestBody AddUserThirdLoanPolicyReq req) {
        APIResponse<Void> result = new APIResponse<>();
        Long userId = req.getUserId();
        Long policyId = req.getPolicyId();
        List<String> thirdUUIDList = req.getThirdUUIDs();

        if (userId == null || policyId == null || CollectionUtils.isEmpty(thirdUUIDList)) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserPolicyResult userPolicyResult = userPolicyService.attachUserThirdLoanPolicy(userId, policyId, thirdUUIDList);
        if (userPolicyResult.isFailed()) {
            logger.error("挂载用户自定义散标策略失败,req={}", JSONObject.toJSONString(req));
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userPolicyResult.getErrorMsg());
        }

        return result;
    }

    /**
     * 批量解挂删除用户为第三方账号设置的自定义散标策略
     */
    @RequestMapping(value = "/dettachUserThirdLoanPolicy", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Void> dettachUserThirdLoanPolicy(@RequestBody DettachUserThirdLoanPolicyReq req) {
        APIResponse<Void> result = new APIResponse<>();
        Long userId = req.getUserId();
        List<Long> dataIds = req.getUserLoanPolicyIds();
        if (userId == null || CollectionUtils.isEmpty(dataIds)) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        Result delResult = userPolicyService.dettachUserThirdLoanPolicy(userId, dataIds);
        if (delResult.isFailed()) {
            logger.error("批量解挂删除用户为第三方账号设置的自定义散标策略失败,param={}", JSONObject.toJSONString(req));
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), delResult.getErrorMsg());
        }
        return result;
    }


    /**
     * 保存用户为第三方账号选择的自定义散标策略
     */
    @RequestMapping(value = "/saveUserThirdLoanPolicy", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Void> saveUserThirdLoanPolicy(@RequestBody SaveUserThirdLoanPolicyReq req) {
        APIResponse<Void> result = new APIResponse<>();
        Long userId = req.getUserId();
        Integer bidAmount = req.getBidAmount();
        Short status = req.getUserThirdLoanPolicyStatus();
        Long id = req.getUserThirdLoanPolicyId();

        if (userId == null || bidAmount == null || status == null || id == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        UserPolicy userPolicy = new UserPolicy();
        userPolicy.setId(id);
        userPolicy.setUserId(userId);
        userPolicy.setBidAmount(bidAmount);
        userPolicy.setStatus(status);
        //调用rpc服务
        Result saveResult = userPolicyService.modifyUserPolicy(userPolicy);
        if (saveResult.isFailed()) {
            logger.error("保存用户为第三方账号选择的自定义散标策略失败,param={}", JSONObject.toJSONString(req));
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), saveResult.getErrorMsg());
        }
        return result;
    }
}
