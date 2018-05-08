package com.zhuobao.ivbid.datacache;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivuser.biz.manager.UserPolicyManager;
import com.invest.ivuser.biz.service.UserPolicyService;
import com.invest.ivuser.common.UserPolicyStatusEnum;
import com.invest.ivuser.model.entity.ext.UserPolicyDetail;
import com.invest.ivuser.model.param.UserPolicyParam;
import com.invest.ivuser.model.result.UserPolicyResult;
import com.zhuobao.ivbid.dao.h2.UserPolicyDAO;
import com.zhuobao.ivbid.dao.h2.query.UserPolicyQuery;
import com.zhuobao.ivbid.model.entity.UserPolicy;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务启动加载mysql 用户策略到本地H2数据库
 * Created by xugang on 17/1/8.
 */
@Component
public class UserPolicyDataCache implements InitializingBean {

    public static Logger logger = LoggerFactory.getLogger(UserPolicyDataCache.class);

    @Resource(name = "h2UserPolicyDAO")
    protected UserPolicyDAO h2UserPolicyDAO;

    @Autowired
    private UserPolicyService userPolicyService;

    @Autowired
    protected UserPolicyManager userPolicyManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.doLoad();
        logger.debug("此时内存数据:{}", JSONObject.toJSONString(this.getAllUserPolicyList()));
    }

    public void doLoad() throws Exception {
        try {
            //散标策略加载
            doLoadUserLoanPolicy();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    private void doLoadUserLoanPolicy() {
        UserPolicyResult userPolicyResult = getUserLoanPoliciesFromDB();
        if (userPolicyResult.isFailed()) {
            logger.error("加载散标策略失败,msg=" + userPolicyResult.getErrorMsg());
            return;
        }
        List<UserPolicyDetail> userPolicyDetailList = userPolicyResult.getUserPolicyDetails();
        if (CollectionUtils.isEmpty(userPolicyDetailList)) {
            logger.warn("无散标策略可加载");
            return;
        }
        //将持久化数据加载到内存H2 1000用户*策略 约为 20000条
        //TODO 批量插入
        userPolicyDetailList.parallelStream().forEach(userPolicyDetail -> {
            UserPolicy policy = buildH2UserPolicy(userPolicyDetail);
            h2UserPolicyDAO.insert(policy);
        });
        logger.info("加载用户自定义策略缓存完毕,size={}", userPolicyDetailList.size());
    }

    private UserPolicyResult getUserLoanPoliciesFromDB() {
        UserPolicyParam policyParam = new UserPolicyParam();
        policyParam.setStatus(UserPolicyStatusEnum.start.getCode());
        UserPolicyResult userPolicyResult = userPolicyService.getUserPolicyDetailList(policyParam);
        return userPolicyResult;
    }

    public static UserPolicy buildH2UserPolicy(UserPolicyDetail userPolicyDetail) {
        UserPolicy policy = new UserPolicy();

        policy.setPolicyId(userPolicyDetail.getId());
        policy.setUserId(userPolicyDetail.getUserId());
        policy.setType(userPolicyDetail.getPolicyType());
        policy.setUsername(userPolicyDetail.getThirdUserUUID());
        policy.setBidAmount(userPolicyDetail.getBidAmount());

        policy.setAgeBegin(getDefaultValue(userPolicyDetail.getAgeBegin(), policy.getAgeBegin()));
        policy.setAgeEnd(getDefaultValue(userPolicyDetail.getAgeEnd(), policy.getAgeEnd()));
        policy.setSex(userPolicyDetail.getSex());

        policy.setCreditCode(userPolicyDetail.getCreditCode());
        policy.setCertificate(userPolicyDetail.getCertificate());
        policy.setThirdAuthInfo(userPolicyDetail.getThirdAuthInfo());
        policy.setStudyStyle(userPolicyDetail.getStudyStyle());
        policy.setGraduateSchoolType(userPolicyDetail.getGraduateSchoolType());

        policy.setAmountBegin(getDefaultValue(userPolicyDetail.getAmountBegin(), policy.getAmountBegin()));
        policy.setAmountEnd(getDefaultValue(userPolicyDetail.getAmountEnd(), policy.getAmountEnd()));
        policy.setMonthBegin(getDefaultValue(userPolicyDetail.getMonthBegin(), policy.getMonthBegin()));
        policy.setMonthEnd(getDefaultValue(userPolicyDetail.getMonthEnd(), policy.getMonthEnd()));
        policy.setRateBegin(getDefaultValue(userPolicyDetail.getRateBegin(), policy.getRateBegin()));
        policy.setRateEnd(getDefaultValue(userPolicyDetail.getRateEnd(), policy.getRateEnd()));
        policy.setLoanerSuccessCountBegin(getDefaultValue(userPolicyDetail.getLoanerSuccessCountBegin(), policy.getLoanerSuccessCountBegin()));
        policy.setLoanerSuccessCountEnd(getDefaultValue(userPolicyDetail.getLoanerSuccessCountEnd(), policy.getLoanerSuccessCountEnd()));
        policy.setWasteCountBegin(getDefaultValue(userPolicyDetail.getWasteCountBegin(), policy.getWasteCountBegin()));
        policy.setWasteCountEnd(getDefaultValue(userPolicyDetail.getWasteCountEnd(), policy.getWasteCountEnd()));
        policy.setNormalCountBegin(getDefaultValue(userPolicyDetail.getNormalCountBegin(), policy.getNormalCountBegin()));
        policy.setNormalCountEnd(getDefaultValue(userPolicyDetail.getNormalCountEnd(), policy.getNormalCountEnd()));
        policy.setOverdueLessCountBegin(getDefaultValue(userPolicyDetail.getOverdueLessCountBegin(), policy.getOverdueLessCountBegin()));
        policy.setOverdueLessCountEnd(getDefaultValue(userPolicyDetail.getOverdueLessCountEnd(), policy.getOverdueLessCountEnd()));
        policy.setOverdueMoreCountBegin(getDefaultValue(userPolicyDetail.getOverdueMoreCountBegin(), policy.getOverdueMoreCountBegin()));
        policy.setOverdueMoreCountEnd(getDefaultValue(userPolicyDetail.getOverdueMoreCountEnd(), policy.getOverdueMoreCountEnd()));
        policy.setTotalPrincipalBegin(getDefaultValue(userPolicyDetail.getTotalPrincipalBegin(), policy.getTotalPrincipalBegin()));
        policy.setTotalPrincipalEnd(getDefaultValue(userPolicyDetail.getTotalPrincipalEnd(), policy.getTotalPrincipalEnd()));
        policy.setOwingPrincipalBegin(getDefaultValue(userPolicyDetail.getOwingPrincipalBegin(), policy.getOwingPrincipalBegin()));
        policy.setOwingPrincipalEnd(getDefaultValue(userPolicyDetail.getOwingPrincipalEnd(), policy.getOwingPrincipalEnd()));
        policy.setAmountToReceiveBegin(getDefaultValue(userPolicyDetail.getAmountToReceiveBegin(), policy.getAmountToReceiveBegin()));
        policy.setAmountToReceiveEnd(getDefaultValue(userPolicyDetail.getAmountToReceiveEnd(), policy.getAmountToReceiveEnd()));
        policy.setLastSuccessBorrowDaysBegin(getDefaultValue(userPolicyDetail.getLastSuccessBorrowDaysBegin(), policy.getLastSuccessBorrowDaysBegin()));
        policy.setLastSuccessBorrowDaysEnd(getDefaultValue(userPolicyDetail.getLastSuccessBorrowDaysEnd(), policy.getLastSuccessBorrowDaysEnd()));
        policy.setRegisterBorrowMonthsBegin(getDefaultValue(userPolicyDetail.getRegisterBorrowMonthsBegin(), policy.getRegisterBorrowMonthsBegin()));
        policy.setRegisterBorrowMonthsEnd(getDefaultValue(userPolicyDetail.getRegisterBorrowMonthsEnd(), policy.getRegisterBorrowMonthsEnd()));

        policy.setAmountOwingTotalBegin(getDefaultValue(userPolicyDetail.getAmountOwingTotalBegin(), policy.getAmountOwingTotalBegin()));
        policy.setAmountOwingTotalEnd(getDefaultValue(userPolicyDetail.getAmountOwingTotalEnd(), policy.getAmountOwingTotalEnd()));

        policy.setOwingHighestDebtRatioBegin(getDefaultValue(userPolicyDetail.getOwingHighestDebtRatioBegin(), policy.getOwingHighestDebtRatioBegin()));
        policy.setOwingHighestDebtRatioEnd(getDefaultValue(userPolicyDetail.getOwingHighestDebtRatioEnd(), policy.getOwingHighestDebtRatioEnd()));
        policy.setAmtDebtRatBg(getDefaultValue(userPolicyDetail.getAmtDebtRatBg(), policy.getAmtDebtRatBg()));
        policy.setAmtDebtRatEd(getDefaultValue(userPolicyDetail.getAmtDebtRatEd(), policy.getAmtDebtRatEd()));

        //如果打标值为0 表示无限制
        policy.setCreditCode(userPolicyDetail.getCreditCode());
        policy.setCertificate(userPolicyDetail.getCertificate());
        policy.setThirdAuthInfo(userPolicyDetail.getThirdAuthInfo());
        policy.setStudyStyle(userPolicyDetail.getStudyStyle());
        policy.setGraduateSchoolType(userPolicyDetail.getGraduateSchoolType());

        return policy;
    }

    private static Integer getDefaultValue(Integer value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private static Double getDefaultValue(Double value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public List<UserPolicy> getAllUserPolicyList() {
        UserPolicy param = new UserPolicy();
        List<UserPolicy> userPolicies = h2UserPolicyDAO.selectListBySelective(param);
        return userPolicies;
    }


    public UserPolicy getUserPolicy(Long userId, String username, Long policyId) {
        UserPolicy userPolicy = h2UserPolicyDAO.selectOneByUniqueKey(userId, username, policyId);
        return userPolicy;
    }

    public List<UserPolicy> getUserPolicyList(UserPolicyQuery userPolicyQuery) {
        List<UserPolicy> userPolicies = h2UserPolicyDAO.selectListByQuery(userPolicyQuery);
        return userPolicies;
    }

    public void addUserPolicy(UserPolicy userPolicy) {
        try {
            h2UserPolicyDAO.insert(userPolicy);
            logger.info("插入H2-UserPolicy成功,userPolicy={}", JSONObject.toJSONString(userPolicy));
        } catch (Exception e) {
            logger.error("插入H2-UserPolicy失败,userPolicy=" + JSONObject.toJSONString(userPolicy), e);
        }
    }

    public void delUserPolicy(Long userId, String username, Long policyId) {
        String param = userId + "#" + username + "#" + policyId;
        try {
            h2UserPolicyDAO.deleteByUniqueKey(userId, username, policyId);
            logger.info("删除H2-UserPolicy成功,param={}", param);
        } catch (Exception e) {
            logger.error("删除H2-UserPolicy失败,param=" + param, e);
        }
    }

    public void updUserPolicy(Long userId, String username, Long policyId, Integer bidAmount) {
        String param = userId + "#" + username + "#" + policyId;
        try {
            h2UserPolicyDAO.updateByUniqueKey(userId, username, policyId, bidAmount);
            logger.info("更新H2-UserPolicy成功,param={}", param);
        } catch (Exception e) {
            logger.error("更新H2-UserPolicy失败,param=" + param, e);
        }
    }

    public void updUserPolicy(UserPolicy userPolicy) {
        try {
            h2UserPolicyDAO.updateByUniqueKeySelective(userPolicy);
            logger.info("更新H2-UserPolicy成功,param={}", JSONObject.toJSONString(userPolicy));
        } catch (Exception e) {
            logger.error("更新H2-UserPolicy失败,param=" + JSONObject.toJSONString(userPolicy), e);
        }
    }
}
