package com.zhuobao.ivsyncjob.job.elastic;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivppad.biz.service.ppdopenapi.AccountService;
import com.invest.ivppad.datacache.UserAccountInfoDataCache;
import com.invest.ivppad.model.result.PPDOpenApiAccountResult;
import com.invest.ivppad.util.KeyVersionUtils;
import com.invest.ivppad.util.RedisKeyUtils;
import com.invest.ivuser.biz.service.BlackListService;
import com.invest.ivuser.biz.service.MessageService;
import com.invest.ivuser.dao.db.UserThirdBindInfoDAO;
import com.invest.ivuser.datacache.BlackListThirdDataCache;
import com.invest.ivuser.datacache.NotifyMessageDataCache;
import com.invest.ivuser.model.entity.BlackListThird;
import com.invest.ivuser.model.entity.NotifyMessage;
import com.invest.ivuser.model.entity.UserNotifyMessage;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.invest.ivuser.common.Constants.NOTIFYMSGUNIKEY_GET_PPD_BALANCE_ERROR;

/**
 * 同步用户账户 包括余额
 * Created by xugang on 2017/9/18.
 */
@Component
public class SyncUserAccountInfoJob extends ElasticBaseJob {

    @Resource
    protected AccountService accountService;
    @Autowired
    protected UserThirdBindInfoDAO userThirdBindInfoDAO;
    @Autowired
    private NotifyMessageDataCache notifyMessageDataCache;
    @Resource
    private MessageService messageService;

    @Resource(name = "ppdCacheClientHA")
    protected CacheClientHA ppdCacheClientHA;

    @Autowired
    private UserAccountInfoDataCache userAccountInfoDataCache;

    @Resource
    protected BlackListService blackListService;

    @Autowired
    private BlackListThirdDataCache blackListThirdDataCache;

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();

        UserThirdBindInfo param = new UserThirdBindInfo();
        List<String> thirdUUIDList = userThirdBindInfoDAO.selectAllThirdUUIDList(param);
        if (CollectionUtils.isEmpty(thirdUUIDList)) {
            logger.error("获取第三方账号列表为空,不去同步用户账户信息");
            return;
        }

        thirdUUIDList.stream().forEach(thirdUUID -> {
            //判断是否已在黑名单
            boolean isInBlack = blackListThirdDataCache.isInThirdBlackList(BlackListThird.TYPE_THIRD_USERNAME, thirdUUID);
            if (isInBlack) {
                logger.info("已在黑名单,不去获取用户账户余额,thirdUUID={}", thirdUUID);
                return;
            }
            //查询账户余额
            PPDOpenApiAccountResult accountResult = accountService.getAccountBalance(thirdUUID);
            if (accountResult.isFailed()) {
                logger.error("获取用户账户余额失败,userName={},error={}", thirdUUID, accountResult);
                //修改本地缓存余额为-1 标识异常 TODO 临时方案 分布式就惨了
                userAccountInfoDataCache.updateUserAccountBalance(thirdUUID, -1);
                //如果余额都获取不到,则认为其他需要授权的接口也调不通,为了节省带宽,放入黑名单,重新授权后释放黑名单
                addBlackList(thirdUUID);
                //通知用户
                notifyUserMessage(thirdUUID);
                return;
            }
            int balance = accountResult.getBalance(); //分
            logger.info("获取用户账户余额成功,balance={}[分],username={}", balance, thirdUUID);

            //放入redis
            try {
                String key = KeyVersionUtils.rediskeyUserAccountBalanceV(RedisKeyUtils.keyUserAccountBalance(thirdUUID));
                ppdCacheClientHA.String().setex(key, RedisKeyUtils.KEY_EXPIRETIME_USER_ACCOUNT_BALANCE, String.valueOf(balance));
            } catch (Exception e) {
                logger.error("放入redis余额异常,username=" + thirdUUID, e);
                return;
            }
        });
    }

    private void addBlackList(String thirdUUID) {
        BlackListThird blackListThird = new BlackListThird();
        blackListThird.setBlacklistType(BlackListThird.TYPE_THIRD_USERNAME);
        blackListThird.setBlacklistValue(thirdUUID);
        Result addBlackListThirdResult = blackListService.addBlackListThird(blackListThird);
        if (addBlackListThirdResult.isFailed()) {
            logger.error("放入黑名单失败,blackListThird=" + JSONObject.toJSONString(blackListThird) + ",msg=" + addBlackListThirdResult.getErrorMsg());
        }
    }

    private void notifyUserMessage(String thirdUUID) {
        try {
            NotifyMessage notifyMessage = notifyMessageDataCache.getNotifyMessageByUniqKey(NOTIFYMSGUNIKEY_GET_PPD_BALANCE_ERROR);
            if (notifyMessage == null) {
                return;
            }
            //查询用户id 一个第三方账户只能被一个手机号绑定
            UserThirdBindInfo userThirdBindInfoDBParam = new UserThirdBindInfo();
            userThirdBindInfoDBParam.setThirdUserUUID(thirdUUID);
            List<UserThirdBindInfo> userThirdBindInfoList = userThirdBindInfoDAO.selectListBySelective(userThirdBindInfoDBParam);
            if (CollectionUtils.isEmpty(userThirdBindInfoList)) {
                return;
            }

            Long userId = userThirdBindInfoList.get(0).getUserId();

            //控制频率 30分钟内只提醒一次
            String key = RedisKeyUtils.keyUserNotifyMessageForGetPPDBalanceError(userId, thirdUUID);
            boolean exists = ppdCacheClientHA.Key().exists(key);
            if (exists) {
                return;
            }

            UserNotifyMessage userNotifyMessage = new UserNotifyMessage();
            userNotifyMessage.setMessageId(notifyMessage.getId());
            userNotifyMessage.setStatus(UserNotifyMessage.STATUS_UNREAD);
            userNotifyMessage.setUserId(userId);
            userNotifyMessage.setMessageValue(thirdUUID);
            messageService.addUserNotifyMessage(userNotifyMessage);

            ppdCacheClientHA.String().setex(key, RedisKeyUtils.KEY_EXPIRETIME_UNM_GETPPDBALANCEERROR, NumberUtils.INTEGER_ONE.toString());

        } catch (Exception e) {
            logger.error("通知用户获取账户余额发生异常,thirdUUID=" + thirdUUID, e);
        }

    }

}
