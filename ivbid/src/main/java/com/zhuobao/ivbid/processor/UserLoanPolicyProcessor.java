package com.zhuobao.ivbid.processor;

import com.invest.ivcommons.rocketmq.model.UserLoanPolicyMessage;
import com.invest.ivcommons.util.beans.BeanUtils;
import com.invest.ivuser.biz.service.UserPolicyService;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.ext.UserPolicyDetail;
import com.invest.ivuser.model.param.UserPolicyParam;
import com.invest.ivuser.model.result.UserPolicyResult;
import com.zhuobao.ivbid.datacache.UserPolicyDataCache;
import com.zhuobao.ivbid.model.entity.UserPolicy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 散标策略 处理器
 * mq consumer 调用
 * 用到service,manager
 * Created by xugang on 2017/8/9.
 */
@Service
public class UserLoanPolicyProcessor {
    private static final Logger logger = LoggerFactory.getLogger(UserLoanPolicyProcessor.class);

    @Autowired
    private UserPolicyDataCache userPolicyDataCache;

    @Autowired
    private UserPolicyService userPolicyService;

    public void process(UserLoanPolicyMessage userLoanPolicyMessage) {
        if (userLoanPolicyMessage == null) {
            logger.error("参数为空,无法处理");
            return;
        }
        String optType = userLoanPolicyMessage.getOptType();
        if (StringUtils.isEmpty(optType)) {
            logger.error("操作类型为空,无法处理");
            return;
        }

        Long userId = userLoanPolicyMessage.getUserId();
        Long policyId = userLoanPolicyMessage.getPolicyId();
        String username = userLoanPolicyMessage.getUsername();

        //更新h2本地散标策略缓存
        try {
            switch (optType) {
                case UserLoanPolicyMessage.OPT_TYPE_DEL: { //删除
                    userPolicyDataCache.delUserPolicy(userId, username, policyId);
                    break;
                }
                case UserLoanPolicyMessage.OPT_TYPE_UPD: { //更新
                    UserPolicy userPolicy = userPolicyDataCache.getUserPolicy(userId, username, policyId);
                    if (userPolicy == null) { //新增
                        userPolicy = buildUserPolicy(userLoanPolicyMessage, false);
                        userPolicyDataCache.addUserPolicy(userPolicy);
                    } else { //更新投标金额
                        userPolicyDataCache.updUserPolicy(userId, username, policyId, userLoanPolicyMessage.getBidAmount());
                    }
                    break;
                }
                case UserLoanPolicyMessage.OPT_TYPE_UPD_ALL: { //更新所有
                    List<UserPolicy> userPolicyList = buildUserPolicyList(userLoanPolicyMessage.getUserId(), userLoanPolicyMessage.getPolicyId());
                    if (CollectionUtils.isEmpty(userPolicyList))
                        throw new IllegalStateException("查询用户散标策略详情失败,userId=" + userId + ",policyId=" + policyId);
                    userPolicyList.stream().forEach(userPolicy -> userPolicyDataCache.updUserPolicy(userPolicy));
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("处理散标策略MQ失败,userId={},username={},e={}", userLoanPolicyMessage.getUserId(),
                    userLoanPolicyMessage.getUsername(), e.getMessage());
            //TODO 轮询重试
        }


    }

    private List<UserPolicy> buildUserPolicyList(Long userId, Long policyId) {
        //查询该策略id对应的 status为开启的 挂载第三方账户的 所有策略详情
        UserPolicyParam userPolicyParam = new UserPolicyParam();
        userPolicyParam.setUserId(userId); //userId为空表示 后台修改系统散标策略,所有用户生效,而自定义为用户id
        userPolicyParam.setStatus(com.invest.ivuser.model.entity.UserPolicy.STATUS_ON);
        userPolicyParam.setPolicyId(policyId);
        UserPolicyResult userPolicyResult = userPolicyService.getUserPolicyDetailList(userPolicyParam);
        if (userPolicyResult.isFailed()) {
            return null;
        }
        List<UserPolicyDetail> userPolicyDetailList = userPolicyResult.getUserPolicyDetails();
        if (CollectionUtils.isEmpty(userPolicyDetailList)) {
            return null;
        }
        List<UserPolicy> result = new ArrayList<>();
        userPolicyDetailList.stream().forEach(userPolicyDetail -> {
            UserPolicy userPolicy = new UserPolicy();
            BeanUtils.copyProperties(userPolicyDetail, userPolicy); //BeanUtils.copyProperties只拷贝不为null的属性
            userPolicy.setUsername(userPolicyDetail.getThirdUserUUID());
            userPolicy.setPolicyId(userPolicyDetail.getId());
            userPolicy.setType(userPolicyDetail.getPolicyType());
            result.add(userPolicy);
        });
        return result;
    }

    private UserPolicy buildUserPolicy(UserLoanPolicyMessage userLoanPolicyMessage, boolean coverNull) throws Exception {
        Long policyId = userLoanPolicyMessage.getPolicyId();
        Long userId = userLoanPolicyMessage.getUserId();
        String username = userLoanPolicyMessage.getUsername();

        UserPolicy userPolicy = new UserPolicy();
        //查询散标列表
        UserPolicyResult userPolicyResult = userPolicyService.getLoanPolicy(policyId);
        if (userPolicyResult.isFailed()) {
            throw new IllegalStateException("查询散标策略失败,policyId=" + policyId);
        }
        LoanPolicy loanPolicy = userPolicyResult.getLoanPolicy();

        if (coverNull) org.springframework.beans.BeanUtils.copyProperties(loanPolicy, userPolicy);
        else BeanUtils.copyProperties(loanPolicy, userPolicy); //BeanUtils.copyProperties只拷贝不为null的属性

        userPolicy.setUserId(userId);
        userPolicy.setUsername(username);
        userPolicy.setPolicyId(policyId);
        userPolicy.setType(loanPolicy.getPolicyType());
        userPolicy.setBidAmount(userLoanPolicyMessage.getBidAmount());

        return userPolicy;
    }
}
