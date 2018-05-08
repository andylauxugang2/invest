package com.invest.ivuser.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivcommons.rocketmq.model.UserLoanPolicyMessage;
import com.invest.ivuser.biz.manager.*;
import com.invest.ivuser.biz.service.UserPolicyService;
import com.invest.ivuser.common.IVUserErrorEnum;
import com.invest.ivuser.common.UserPolicyTypeEnum;
import com.invest.ivuser.model.entity.BizOptLog;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.UserMainPolicy;
import com.invest.ivuser.model.entity.UserPolicy;
import com.invest.ivuser.model.entity.ext.UserPolicyDetail;
import com.invest.ivuser.model.param.LoanPolicyParam;
import com.invest.ivuser.model.param.UserPolicyParam;
import com.invest.ivuser.model.result.LoanPolicyResult;
import com.invest.ivuser.model.result.UserPolicyResult;
import com.invest.ivuser.model.vo.UserMainPolicyVO;
import com.invest.ivuser.mq.producer.UserLoanPolicyProducer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class UserPolicyServiceImpl implements UserPolicyService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoanPolicyManager loanPolicyManager;

    @Autowired
    private UserMainPolicyManager userMainPolicyManager;

    @Autowired
    private UserPolicyManager userPolicyManager;

    @Resource
    private UserLoanPolicyProducer userLoanPolicyProducer;

    @Autowired
    private BizOptLogManager bizOptLogManager;

    @Autowired
    private MainPolicyManager mainPolicyManager;

    @Override
    public UserPolicyResult getUserPolicyCount(UserPolicyParam param) {
        UserPolicyResult result = new UserPolicyResult();
        try {
            int count = userPolicyManager.getUserPolicyCount(param.getUserId(), param.getPolicyType(), param.getStatus());
            result.setUserPolicyCount(count);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public UserPolicyResult getLoanPolicy(Long id) {
        UserPolicyResult result = new UserPolicyResult();
        LoanPolicy loanPolicy = loanPolicyManager.getLoanPolicyById(id);
        result.setLoanPolicy(loanPolicy);
        return result;
    }

    @Override
    public List<UserMainPolicyVO> getUserMainPolicies(Long userId, String thirdUserUUID) {
        //查询用户主策略
        UserMainPolicy userMainPolicy = new UserMainPolicy();
        userMainPolicy.setThirdUserUUID(thirdUserUUID);
        userMainPolicy.setUserId(userId);
        return userMainPolicyManager.getUserMainPolicyList(userMainPolicy);
    }

    @Override
    public List<UserMainPolicy> getUserMainPolicies(UserMainPolicy userMainPolicy) {
        //查询用户主策略
        return userMainPolicyManager.getUserMainPolicyEntityList(userMainPolicy);
    }

    @Override
    public UserMainPolicyVO getUserMainPolicy(Long userId, String thirdUserUUID, Long mainPolicyId) {
        return userMainPolicyManager.getUserMainPolicyByUniqueKey(userId, thirdUserUUID, mainPolicyId);
    }

    @Override
    public UserMainPolicy saveUserMainPolicy(UserMainPolicy userMainPolicy) {
        try {
            userMainPolicyManager.saveUserMainPolicy(userMainPolicy);
        } catch (Exception e) {
            //ignore
        }
        return userMainPolicy;
    }

    @Override
    public UserPolicyResult removeUserMainPolicy(Long userId, String thirdUserUUID, Long mainPolicyId) {
        UserPolicyResult result = new UserPolicyResult();
        try {
            userMainPolicyManager.delUserMainPolicy(userId, thirdUserUUID, mainPolicyId);
            logger.info("删除用户主策略成功,userId={},thirdUserUUID={},mainPolicyId={}", userId, thirdUserUUID, mainPolicyId);
        } catch (Exception e) {
            logger.error("删除用户主策略失败,userId=" + userId + ",thirdUserUUID=" + thirdUserUUID + ",mainPolicyId=" + mainPolicyId, e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public LoanPolicyResult getLoanPolicies(LoanPolicyParam param) {
        LoanPolicyResult result = new LoanPolicyResult();
        Short policyType = param.getPolicyType();
        LoanPolicy loanPolicy = new LoanPolicy();
        loanPolicy.setRiskLevel(param.getRiskLevel());
        loanPolicy.setPolicyType(policyType);
        //loanPolicy.setUserId(param.getUserId());
        loanPolicy.setStatus(param.getStatus());

        if (policyType != null && UserPolicyTypeEnum.USER_LOAN_POLICY.getCode() == policyType) {
            loanPolicy.setUserId(param.getUserId());
        }
        try {
            List<LoanPolicy> loanPolicyList = loanPolicyManager.getLoanPolicyList(loanPolicy);
            result.setLoanPolicies(loanPolicyList);
        } catch (Exception e) {
            logger.error("查询散标策略异常,param=" + JSONObject.toJSONString(param));
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public UserPolicyResult addUserPolicyBatch(Long userId, String thirdUserUUID, List<Long> policyIds) {
        UserPolicyResult result = new UserPolicyResult();
        List<UserPolicy> userPolicies = new ArrayList<>(policyIds.size());
        try {
            //不需要发送策略缓存同步MQ 默认挂载策略,status是关闭的,开启时同步MQ即可.

            for (Long policyId : policyIds) {
                UserPolicy loanPolicy = new UserPolicy();
                loanPolicy.setBidAmount(UserPolicy.BIDAMOUNT_DEFAULT); //默认50元投资金额
                loanPolicy.setStatus(UserPolicy.STATUS_OFF); //默认关闭
                loanPolicy.setPolicyId(policyId);
                loanPolicy.setUserId(userId);
                loanPolicy.setThirdUserUUID(thirdUserUUID);
                userPolicies.add(loanPolicy);
            }
            userPolicyManager.batchSaveUserPolicy(userPolicies);
        } catch (Exception e) {
            logger.error("批量保存用户系统散标策略失败,userId=" + userId + ",thirdUserUUID=" + thirdUserUUID, e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }

        return result;
    }

    @Override
    public Result delLoanPolicy(Long userId, List<Long> loanPolicyIds) {
        Result result = new Result();
        try {
            //判断该策略是否被挂载 挂载的不能删除 要先解挂
            for (Long loanPolicyId : loanPolicyIds) {
                boolean exists = userPolicyManager.existsUserPolicy(userId, loanPolicyId);
                if (exists) {
                    IVUserErrorEnum.DELETE_ERROR.fillResult(result);
                    result.format("[策略编号:" + loanPolicyId + "已被挂载,请去解挂后再删除]");
                    return result;
                }
            }
            loanPolicyManager.updateDeletedBatchById(loanPolicyIds);

            //TODO 保存操作日志

        } catch (Exception e) {
            logger.error("删除用户自定义散标策略失败,ids=" + JSONObject.toJSONString(loanPolicyIds), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    //以下如果配置spring事务,则整个方法完成后才会commit db 会出现mq消费和db插入速度不一致问题 需要去掉事务
    //TODO 加锁控制
    @Override
    public LoanPolicyResult saveLoanPolicy(LoanPolicy loanPolicy) {
        LoanPolicyResult result = new LoanPolicyResult();
        Long id = loanPolicy.getId();
        Long userId = loanPolicy.getUserId(); //为空表示 后台修改系统散标策略，而自定义为用户id
        try {
            if (id == null) {
                loanPolicyManager.addLoanPolicy(loanPolicy);
            } else {
                //先入库 保证mq消费端处理的数据从db顺序读出
                loanPolicyManager.updateLoanPolicyById(loanPolicy);

                //如果有挂载的第三方账户 发送MQ同步消息 只发送policyId 消费方查询需要同步的策略详情
                boolean exists = userPolicyManager.existsUserPolicy(userId, id, UserPolicy.STATUS_ON);
                if (exists) {
                    UserLoanPolicyMessage msg = new UserLoanPolicyMessage();
                    msg.setUserId(userId);
                    msg.setPolicyId(id);
                    msg.setOptType(UserLoanPolicyMessage.OPT_TYPE_UPD_ALL);
                    boolean sendResult = userLoanPolicyProducer.sendMsg(msg);
                    if (!sendResult) {
                        //TODO 回滚DB操作
                        IVUserErrorEnum.MODIFY_ERROR.fillResult(result);
                        return result;
                    }
                    saveBizOptLog(userId, id, BizOptLog.OPT_TYPE_UPD, BizOptLog.STATUS_LOAN_POLICY_NOSYNC);
                }
            }
        } catch (Exception e) {
            logger.error("保存自定义散标策略失败,loanPolicy=" + JSONObject.toJSONString(loanPolicy), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }

        return result;
    }

    @Override
    public UserPolicyResult getUserPolicies(UserPolicyParam param) {
        UserPolicyResult result = new UserPolicyResult();
        UserPolicy userPolicy = new UserPolicy();
        userPolicy.setUserId(param.getUserId());
        userPolicy.setThirdUserUUID(param.getThirdUserUUID());
        List<UserPolicy> userPolicies = userPolicyManager.getUserPolicyList(userPolicy);
        result.setUserPolicies(userPolicies);
        return result;
    }

    //查询包括t_loan_policy详情
    @Override
    public UserPolicyResult getUserPolicyDetailList(UserPolicyParam param) {
        UserPolicyResult result = new UserPolicyResult();
        List<UserPolicyDetail> userPolicyDetailList = loanPolicyManager.getUserPolicyDetailList(param);
        result.setUserPolicyDetails(userPolicyDetailList);
        return result;
    }

    @Override
    public List<String> getNoSelPolicyThirdUUIDList(Long userId, Long policyId, Short policyType, Long mainPolicyId) {
        List<String> result = new ArrayList<>();
        List<UserPolicy> userPolicyList = userPolicyManager.getNoSelPolicyThirdUUIDList(userId, policyType, mainPolicyId);
        if (CollectionUtils.isEmpty(userPolicyList)) {
            return result;
        }

        Set<String> exclude = new HashSet<>(10);
        for (UserPolicy userPolicy : userPolicyList) {
            Long _policyId = userPolicy.getPolicyId();
            if (policyId.equals(_policyId)) {
                exclude.add(userPolicy.getThirdUserUUID());
            }
        }

        for (UserPolicy userPolicy : userPolicyList) {
            String uuid = userPolicy.getThirdUserUUID();
            if (!exclude.contains(uuid) && !result.contains(uuid)) {
                result.add(userPolicy.getThirdUserUUID());
            }
        }
        return result;
    }

    @Override
    public UserPolicyResult attachUserThirdLoanPolicy(Long userId, Long policyId, List<String> thirdUUIDList) {
        UserPolicyResult result = new UserPolicyResult();
        final int defaultBidAmount = 50;
        try {
            List<UserPolicy> userPolicies = new ArrayList<>(thirdUUIDList.size());
            for (String thirdUUID : thirdUUIDList) {
                UserPolicy userPolicy = new UserPolicy();
                userPolicy.setPolicyId(policyId);
                userPolicy.setStatus(UserPolicy.STATUS_OFF); //默认关闭
                userPolicy.setUserId(userId);
                userPolicy.setBidAmount(defaultBidAmount);
                userPolicy.setThirdUserUUID(thirdUUID);
                userPolicies.add(userPolicy);
            }

            userPolicyManager.batchSaveUserPolicy(userPolicies);

        } catch (Exception e) {
            logger.error("批量保存用户第三方自定义散标策略失败,userId=" + userId + ",thirdUserUUID=" + thirdUUIDList, e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }

        return result;
    }

    @Override
    public Result dettachUserThirdLoanPolicy(Long userId, List<Long> userPolicyIds) {
        Result result = new Result();
        List<UserPolicy> userPolicyList = new ArrayList<>();
        Set<Long> failedMsgSet = new HashSet<>();
        try {
            //同步发消息通知策略引擎 更新本地策略缓存
            userPolicyIds.stream().forEach(userPolicyId -> {
                UserPolicy userPolicy = userPolicyManager.getUserPolicyById(userPolicyId);
                UserLoanPolicyMessage msg = new UserLoanPolicyMessage();
                //通过userid username policyid定位唯一h2 内存策略
                msg.setUserId(userPolicy.getUserId());
                msg.setUsername(userPolicy.getThirdUserUUID());
                msg.setPolicyId(userPolicy.getPolicyId());
                msg.setBidAmount(userPolicy.getBidAmount());
                msg.setOptType(UserLoanPolicyMessage.OPT_TYPE_DEL);

                boolean sendResult = userLoanPolicyProducer.sendMsg(msg);
                if (!sendResult) {
                    failedMsgSet.add(userPolicyId);
                }
                userPolicyList.add(userPolicy);
            });

            if (CollectionUtils.isNotEmpty(failedMsgSet)) {
                logger.error("发送散标策略同步mq失败,failedMsgSet=" + failedMsgSet);
                IVUserErrorEnum.DELETE_ERROR.fillResult(result);
                result.setErrorMsg(String.format(result.getErrorMsg(), "策略编号:" + failedMsgSet));
                //TODO 异步重试
                return result;
            }
            //修改db 逻辑删除
            userPolicyManager.deleteBatchUserPolicyById(userPolicyIds);

            //保存日志
            userPolicyList.stream().forEach(userPolicy -> saveBizOptLog(userId, userPolicy.getPolicyId(), BizOptLog.OPT_TYPE_DEL, BizOptLog.STATUS_LOAN_POLICY_NOSYNC));
        } catch (Exception e) {
            logger.error("删除用户自定义散标策略失败,userPolicyIds=" + JSONObject.toJSONString(userPolicyIds), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public Result modifyLoanPolicy(LoanPolicy loanPolicy) {
        Result result = new Result();
        try {
            loanPolicyManager.updateLoanPolicyCommonById(loanPolicy);
        } catch (Throwable e) {
            logger.error("修改用户散标策略失败,loanPolicy=" + JSONObject.toJSONString(loanPolicy), e);
            //ignore
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public Result modifyUserPolicy(UserPolicy userPolicy) {
        Result result = new Result();
        Long userId = userPolicy.getUserId();
        try {
            UserPolicy userPolicyFromDB = userPolicyManager.getUserPolicyById(userPolicy.getId());
            Long policyId = userPolicyFromDB.getPolicyId();
            //查询用户主策略是否开启散标策略
            LoanPolicy loanPolicy = loanPolicyManager.getLoanPolicyById(policyId);
            Short policyType = loanPolicy.getPolicyType();
            UserMainPolicyVO userMainPolicy = userMainPolicyManager.getUserMainPolicyByUniqueKey(userId, userPolicyFromDB.getThirdUserUUID(), Long.valueOf(policyType));
            if (userMainPolicy == null) {
                IVUserErrorEnum.USER_MAIN_POLICY_CLOSED_ERROR.fillResult(result);
                result.format(UserPolicyTypeEnum.findByCode(policyType).getDesc());
                return result;
            }
            //同步发消息通知策略引擎 更新本地策略缓存
            UserLoanPolicyMessage msg = new UserLoanPolicyMessage();
            //通过userid username policyid定位唯一h2 内存策略
            msg.setUserId(userId);
            msg.setUsername(userPolicyFromDB.getThirdUserUUID());
            msg.setPolicyId(policyId);
            msg.setBidAmount(userPolicy.getBidAmount());
            short status = userPolicy.getStatus();
            if (status == UserPolicy.STATUS_OFF) { //关闭的从内存删除掉
                msg.setOptType(UserLoanPolicyMessage.OPT_TYPE_DEL);
            } else { //开启 新增或修改
                msg.setOptType(UserLoanPolicyMessage.OPT_TYPE_UPD);
            }
            boolean sendResult = userLoanPolicyProducer.sendMsg(msg);
            if (!sendResult) {
                IVUserErrorEnum.SAVE_ERROR.fillResult(result);
                return result;
            }

            userPolicyManager.updateUserPolicyById(userPolicy);

            //保存操作日志
            saveBizOptLog(userPolicy.getUserId(), userPolicyFromDB.getPolicyId(), BizOptLog.OPT_TYPE_UPD, BizOptLog.STATUS_LOAN_POLICY_NOSYNC);

        } catch (Throwable e) {
            logger.error("保存用户系统散标策略设置失败,id=" + userPolicy.getId(), e);
            //ignore
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public Result delLoanPolicy(Long userId, Long policyId) {
        Result result = new Result();
        List<Long> userPolicyIds = new ArrayList<>();
        List<UserPolicy> userPolicyList = new ArrayList<>();
        Set<Long> failedMsgSet = new HashSet<>();
        try {
            UserPolicy dbParam = new UserPolicy();
            dbParam.setPolicyId(policyId);
            List<UserPolicy> userPolicies = userPolicyManager.getUserPolicyList(dbParam);
            if (CollectionUtils.isNotEmpty(userPolicies)) {
                //同步发消息通知策略引擎 更新本地策略缓存
                userPolicies.stream().forEach(o -> {
                    UserLoanPolicyMessage msg = new UserLoanPolicyMessage();
                    //通过userid username policyid定位唯一h2 内存策略
                    msg.setUserId(o.getUserId());
                    msg.setUsername(o.getThirdUserUUID());
                    msg.setPolicyId(o.getPolicyId());
                    msg.setOptType(UserLoanPolicyMessage.OPT_TYPE_DEL);

                    boolean sendResult = userLoanPolicyProducer.sendMsg(msg);
                    if (!sendResult) {
                        failedMsgSet.add(o.getPolicyId());
                    }
                    userPolicyList.add(o);
                    userPolicyIds.add(o.getId());
                });

                if (CollectionUtils.isNotEmpty(failedMsgSet)) {
                    logger.error("发送散标策略同步mq失败,failedMsgSet=" + failedMsgSet);
                    IVUserErrorEnum.DELETE_ERROR.fillResult(result);
                    result.setErrorMsg(String.format(result.getErrorMsg(), "策略编号:" + failedMsgSet));
                    //TODO 异步重试
                    return result;
                }
                //db删除
                userPolicyManager.deleteBatchUserPolicyById(userPolicyIds);
            }

            List<Long> policyIds = new ArrayList<>();
            policyIds.add(policyId);
            loanPolicyManager.deleteBatchById(policyIds);
            //保存日志
            userPolicyList.stream().forEach(userPolicy -> saveBizOptLog(userId, userPolicy.getPolicyId(), BizOptLog.OPT_TYPE_DEL, BizOptLog.STATUS_LOAN_POLICY_NOSYNC));
        } catch (Exception e) {
            logger.error("删除散标策略失败,userPolicyIds=" + JSONObject.toJSONString(userPolicyIds), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    private static ScheduledExecutorService dbExecutorService = Executors.newScheduledThreadPool(50);

    private void saveBizOptLog(Long userId, Long policyId, Short optType, Short syncStatus) {
        if(userId == null) userId = -1L;
        BizOptLog log = new BizOptLog();
        log.setUserId(userId);
        log.setBizId(policyId);
        log.setOptType(optType);
        log.setStatus(syncStatus);
        dbExecutorService.schedule(() -> {
            try {
                bizOptLogManager.saveBizOptLog(log);
            } catch (Exception e) {
                //插入失败表 TODO 轮询
            }
        }, 0, TimeUnit.SECONDS);
    }
}
