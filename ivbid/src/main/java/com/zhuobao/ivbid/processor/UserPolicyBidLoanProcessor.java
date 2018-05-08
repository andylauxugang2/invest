package com.zhuobao.ivbid.processor;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.DateFormatUtil;
import com.invest.ivcommons.util.thread.ThreadPoolUtils;
import com.invest.ivppad.biz.service.ppdopenapi.LoanService;

import static com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail;

import com.invest.ivppad.biz.service.ppdopenapi.impl.LoanServiceImpl;
import com.invest.ivppad.datacache.UserAccountInfoDataCache;
import com.invest.ivppad.model.http.response.PPDOpenApiBidLoanResponse;
import com.invest.ivppad.model.param.PPDOpenApiBidLoanParam;
import com.invest.ivppad.model.result.PPDOpenApiLoanResult;
import com.invest.ivuser.biz.service.UserAccountService;
import com.invest.ivuser.biz.service.UserLoanService;
import com.invest.ivuser.common.SexEnum;
import com.invest.ivuser.common.UserPolicyTypeEnum;
import com.invest.ivuser.datacache.UserAccountDataCache;
import com.invest.ivuser.datacache.UserMainPolicySettingDataCache;
import com.invest.ivuser.model.entity.MainPolicy;
import com.invest.ivuser.model.entity.UserLoanRecord;
import com.invest.ivuser.model.result.UserAccountResult;
import com.invest.ivuser.model.result.UserLoanResult;
import com.invest.ivuser.model.vo.UserMainPolicyVO;
import com.zhuobao.ivbid.biz.manager.DistributedLockManager;
import com.zhuobao.ivbid.common.Constants;
import com.zhuobao.ivbid.datacache.UserPolicyDataCache;
import com.zhuobao.ivbid.dao.h2.query.UserPolicyQuery;
import com.zhuobao.ivbid.model.entity.UserPolicy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 用户策略投标 处理器
 * mq consumer 调用
 * 用到service,manager
 * Created by xugang on 2017/8/9.
 */
@Service
public class UserPolicyBidLoanProcessor {
    private static final Logger logger = LoggerFactory.getLogger(UserPolicyBidLoanProcessor.class);

    @Autowired
    private UserPolicyDataCache userPolicyDataCache;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserLoanService userLoanService;
    @Autowired
    private UserMainPolicySettingDataCache userMainPolicySettingDataCache;
    @Autowired
    private UserAccountInfoDataCache userAccountInfoDataCache;
    @Autowired
    private UserAccountDataCache userAccountDataCache;

    //@Autowired
    //private DistributedLockManager distributedLockManager;

    @Resource
    private UserAccountService userAccountService;

    //TODO 配置中心大小
    private static ExecutorService saveUserLoanRecordExcutorService = Executors.newWorkStealingPool();
    private ExecutorService addNotifyMessageExcutorService = ThreadPoolUtils.createBlockingPool(10, 20);

    //标不多 每次过滤一页来的标就几个
    private ExecutorService bidLoanExcutorService = ThreadPoolUtils.createBlockingPool(10, 20);

    public void process(List<LoanListingDetail> loanListingDetailList, int pageIndex) {
        if (CollectionUtils.isEmpty(loanListingDetailList)) {
            logger.error("可投标的详情列表参数为空,无需继续投标");
            return;
        }
        //逐条标的详情列表去匹配用户策略数据,找出命中的策略去投标
        loanListingDetailList.parallelStream().forEach(loanListingDetail -> {
            //获取分布式锁ListingId 防止重复过滤和投标 已经在刷标的时候加了lock
            /*boolean acLock = distributedLockManager.acquireDistributedLock(String.valueOf(loanListingDetail.getListingId()), Constants.DIS_LOCK_EXPIREINSECONDS_LISTINGID);
            if (!acLock) {
                logger.info("未获取到分布式锁,不去处理投标,bizKey={}", loanListingDetail.getListingId());
                return;
            }*/

            UserPolicyQuery query = buildUserPolicyQuery(loanListingDetail);
            long start0 = System.currentTimeMillis();
            logger.debug("获取散标详情消息:{}", JSONObject.toJSONString(loanListingDetail));
            logger.debug("此时查询内存条件:{}", JSONObject.toJSONString(query));
            //如果命中多个策略 按照自定义 > 系统 type 排序
            List<UserPolicy> userPolicies = userPolicyDataCache.getUserPolicyList(query);
            logger.info("查询散标策略耗时:{}", System.currentTimeMillis() - start0);

            if (CollectionUtils.isEmpty(userPolicies)) {
                logger.info("未命中策略不去投标,listingId={}", loanListingDetail.getListingId());
                return;
            }

            Integer listingId = loanListingDetail.getListingId();

            logger.info("命中该散标的策略,listingId={},policyIds={}", listingId, userPolicies.stream().map(o -> o.getPolicyId()).collect(Collectors.toList()));

            //过滤:按照username分组 相同type只有一个username去投标
            Map<String, UserPolicy> userPolicyMap = new HashMap<>();
            userPolicies.stream().forEach(userPolicy -> {
                if (!userPolicyMap.containsKey(userPolicy.getUsername())) {
                    userPolicyMap.put(userPolicy.getUsername(), userPolicy);
                }
            });

            logger.info("命中该散标的策略,listingId={},userPolicyUsername={}", listingId, userPolicyMap.keySet());

            //按照用户&用户策略优先级 且用户多策略只选最高策略级别 异步投标
            for (UserPolicy userPolicy : userPolicyMap.values()) {
                Long userId = userPolicy.getUserId();
                Integer bidAmount = userPolicy.getBidAmount();

                /*if (userId == 1) { //测试用户
                }*/
                //判断捉宝币是否充足
                Integer bidAmountBalance = userAccountDataCache.getBidAmountBalanceByUserId(userId);
                logger.info("获取捉宝币余额:userId={},bidAmountBalance={}", userId, bidAmountBalance);
                if (bidAmountBalance == null || (bidAmountBalance - bidAmount) < 0) {
                    logger.info("用户账户捉宝币为空或余额不足,不去投标,userId={},bidAmountBalance={},bidAmount={}.listingId={}", userId, bidAmountBalance, bidAmount, listingId);
                    continue;
                }

                String username = userPolicy.getUsername();
                Long mainPolicyId = MainPolicy.MAIN_POLICY_ID_USER_LOAN;
                if (UserPolicyTypeEnum.SYS_LOAN_POLICY.getCode() == userPolicy.getType()) {
                    mainPolicyId = MainPolicy.MAIN_POLICY_ID_SYS_LOAN;
                }
                UserMainPolicyVO userMainPolicy = userMainPolicySettingDataCache.getUserMainPolicyByUniqKey(userId, userPolicy.getUsername(), mainPolicyId);
                logger.info("获取主策略:userMainPolicy={}", JSONObject.toJSONString(userMainPolicy));
                if (userMainPolicy == null) {
                    logger.info("未查询到用户设置的任何散标策略配置,不去投标,userId={},username={},mainPolicyId={}.listingId={}", userId, userPolicy.getUsername(), mainPolicyId, listingId);
                    continue;
                }
                //判断用户为第三方设置的账户保留金额
                if (userMainPolicy.getAmountStart() != null && userMainPolicy.getAmountStart() > bidAmount) {
                    bidAmount = userMainPolicy.getAmountStart();
                }
                logger.debug("计算投标金额结果:userId={},username={},listingId={},bidAmount={}", userId, username, listingId, bidAmount);
                Integer balance = userAccountInfoDataCache.get(username);
                if (balance == null) {
                    logger.info("获取账户余额为空,不去投标:userId={},username={},listingId={}", userId, username, listingId);
                    continue;
                }
                if (balance < bidAmount) {
                    logger.info("账户余额不足,不去投标:userId={},username={},balance={},listingId={}", userId, username, balance, listingId);
                    continue;
                }
                logger.info("获取账户余额:userId={},username={},listingId={},balance={}", userId, username, listingId, balance);
                Integer remainBalance = balance / 100 - bidAmount;
                logger.info("计算账户保留金额:userId={},username={},listingId={},remainBalance={}", userId, username, listingId, remainBalance);
                if (remainBalance < userMainPolicy.getAccountRemain()) {
                    logger.info("超过用户主策略保留金额,不去投标,userId={},username={},listingId={},policyId={},remain={}", userId, username, listingId, userPolicy.getPolicyId(), userMainPolicy.getAccountRemain());
                    continue;
                }
                int remainFunding = loanListingDetail.getRemainFunding();
                if (remainFunding < bidAmount) {
                    logger.info("剩余可投标金额小于策略金额,不去投标:userId={},username={},listingId={},remainFunding={}", userId, username, listingId, remainFunding);
                    continue;
                }

                logger.debug("准备去投标:userId={},username={},listingId={},bidAmount={}", userId, username, listingId, bidAmount);

                final Integer _bidAmount = bidAmount;
                bidLoanExcutorService.submit(() -> {
                    PPDOpenApiBidLoanParam ppdOpenApiBidLoanParam = new PPDOpenApiBidLoanParam();
                    ppdOpenApiBidLoanParam.setAmount(_bidAmount);
                    ppdOpenApiBidLoanParam.setListingId(listingId);
                    ppdOpenApiBidLoanParam.setUseCoupon(PPDOpenApiBidLoanParam.USECOUPON_VALUE_TRUE);
                    ppdOpenApiBidLoanParam.setUserName(username);
                    ppdOpenApiBidLoanParam.setUserId(userId);
                    Map<String, Object> extParam = new HashMap<>();
                    extParam.put(BidLoanFutureCallback.EXT_PARAM_NAME_REMAINBALANCE, remainBalance);
                    extParam.put(PPDOpenApiBidLoanParam.EXT_PARAM_NAME_PAGEINDEX, pageIndex);
                    logger.debug("拼接投标参数完:userId={},username={},listingId={},param={}", userId, username, listingId, JSONObject.toJSONString(ppdOpenApiBidLoanParam));
                    long start = System.currentTimeMillis();

                    logger.info("开始投标,userId={},userName={},listingId={},policyId={},pageIndex={}", userId, ppdOpenApiBidLoanParam.getUserName(), listingId, userPolicy.getPolicyId(), pageIndex);
                    PPDOpenApiLoanResult loanResult = loanService.bidLoan(ppdOpenApiBidLoanParam, new BidLoanFutureCallback(start, userPolicy, listingId, extParam));
                    if (loanResult.isFailed()) {
                        logger.debug("投标失败,userId={},userName={},listingId={},policy={},error={}",
                                userId, ppdOpenApiBidLoanParam.getUserName(), listingId, JSONObject.toJSONString(userPolicy),
                                loanResult.getErrorMsg());
                        return;
                    }
                });
            }
        });
    }

    class BidLoanFutureCallback implements FutureCallback<HttpResponse> {
        public static final String EXT_PARAM_NAME_REMAINBALANCE = "remainBalance";
        private long start;
        private UserPolicy userPolicy;
        private Integer listingId;
        private Map<String, Object> extParam;

        BidLoanFutureCallback(long start, UserPolicy userPolicy, Integer listingId, Map<String, Object> extParam) {
            this.start = start;
            this.userPolicy = userPolicy;
            this.listingId = listingId;
            this.extParam = extParam;
        }

        @Override
        public void completed(HttpResponse response) {
            Long userId = userPolicy.getUserId();
            String userName = userPolicy.getUsername();
            Integer bidAmount = userPolicy.getBidAmount();
            try {
                logger.debug("bid loan status=" + response.getStatusLine().getStatusCode());
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                String result = EntityUtils.toString(entity, "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                PPDOpenApiBidLoanResponse ppdOpenApiBidLoanResponse = mapper.readValue(result, PPDOpenApiBidLoanResponse.class);

                if (!ppdOpenApiBidLoanResponse.success()) {
                    logger.error("投标失败,userName={},listingId={},amount={},extparam={},error={}", new Object[]{userName, listingId, bidAmount, extParam, ppdOpenApiBidLoanResponse.getResultMessage()});
                    return;
                }

                //实时更新余额
                userAccountInfoDataCache.updateUserAccountBalance(userName, bidAmount);

                logger.info("投标成功,userId={},username={},listingId={},amount={},extparam={},耗时={}",
                        new Object[]{userId, userName, listingId, bidAmount, extParam, System.currentTimeMillis() - start});

                //投标成功更新账户剩余金额 TODO 放入队列中 异步更新 如果被本地缓存刷新掉 以本地缓存刷新结果为准
                //int remainBalance = (Integer) extParam.get(EXT_PARAM_NAME_REMAINBALANCE);
                //updateUserBalanceExcutorService.submit(() -> userAccountInfoDataCache.updateUserAccountBalance(userName, remainBalance * 100));

                //加入队列,批量保存投资记录
                List<UserLoanRecord> userLoanRecords = new ArrayList<>();
                UserLoanRecord userLoanRecord = LoanServiceImpl.buildUserLoanRecord(userName, ppdOpenApiBidLoanResponse);
                userLoanRecord.setUserId(userId);
                userLoanRecord.setPolicyId(userPolicy.getPolicyId());
                userLoanRecord.setPolicyType(userPolicy.getType());
                userLoanRecords.add(userLoanRecord);
                //异步保存
                userLoanRecords.stream().forEach(o -> saveUserLoanRecordExcutorService.submit(new SaveUserLoanRecordCallable(userLoanRecord)));
            } catch (Exception e) {
                logger.error("投标接口失败,listingId=" + listingId + ",userPolicy=" + JSONObject.toJSONString(userPolicy), e);
            }
        }

        @Override
        public void failed(Exception e) {
            logger.error("投标接口失败,listingId=" + listingId + ",userPolicy=" + JSONObject.toJSONString(userPolicy), e);
        }

        @Override
        public void cancelled() {
            logger.info("bid loan cancelled");
        }
    }

    private UserPolicyQuery buildUserPolicyQuery(LoanListingDetail loanListingDetail) {
        UserPolicyQuery query = new UserPolicyQuery();
        query.setAmount(loanListingDetail.getAmount());
        query.setMonth(loanListingDetail.getMonths());
        query.setRate(loanListingDetail.getCurrentRate());
        query.setLoanerSuccessCount(loanListingDetail.getSuccessCount());
        query.setCreditCode(loanListingDetail.getCreditCodeFlag());
        query.setCertificate(loanListingDetail.getEducationFlag());
        query.setThirdAuthInfo(loanListingDetail.getThirdAuthFlag());
        query.setGraduateSchoolType(loanListingDetail.getGraduateSchoolFlag());
        query.setStudyStyle(loanListingDetail.getStudyStyleFlag());
        query.setAmount(loanListingDetail.getAmount());
        query.setAge(loanListingDetail.getAge());
        Short sex = null; //未知
        if (loanListingDetail.getGender() == LoanListingDetail.GENDER_FEMALE) {
            sex = SexEnum.female.getCode();
        } else if (loanListingDetail.getGender() == LoanListingDetail.GENDER_MALE) {
            sex = SexEnum.male.getCode();
        }
        query.setSex(sex);

        query.setCreditCode(loanListingDetail.getCreditCodeFlag());
        query.setThirdAuthInfo(loanListingDetail.getThirdAuthFlag());
        query.setGraduateSchoolType(loanListingDetail.getGraduateSchoolFlag());
        query.setStudyStyle(loanListingDetail.getStudyStyleFlag());
        query.setCertificate(loanListingDetail.getEducationFlag());

        query.setWasteCount(loanListingDetail.getWasteCount());
        query.setNormalCount(loanListingDetail.getNormalCount());
        query.setOverdueLessCount(loanListingDetail.getOverdueLessCount());
        query.setOverdueMoreCount(loanListingDetail.getOverdueMoreCount());
        query.setTotalPrincipal(new BigDecimal(loanListingDetail.getTotalPrincipal()).intValue());
        query.setOwingPrincipal(loanListingDetail.getOwingPrincipal());
        query.setAmountToReceive(loanListingDetail.getAmountToReceive());
        query.setAmountOwingTotal(loanListingDetail.getAmount() + loanListingDetail.getOwingAmount()); //借款+待还
        //如果successCount=0 表示首借 情况
        if (StringUtils.isNotBlank(loanListingDetail.getLastSuccessBorrowTime())) {
            //距最后一次借款成功天数 当前时间 now - 2017-08-14T07:17:40
            Date start = DateFormatUtil.convertStr2Date(loanListingDetail.getLastSuccessBorrowTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND);
            if (start != null) {
                int days = DateUtil.dayBetween(start, DateUtil.getCurrentDatetime());
                query.setLastSuccessBorrowDays(days);
            }
        }
        if (StringUtils.isNotBlank(loanListingDetail.getRegisterTime())) {
            //本次借款距注册时间月数 当前时间 now - 2016-11-04T04:57:40.473
            Date start = DateFormatUtil.convertStr2Date(loanListingDetail.getRegisterTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND);
            if (start != null) {
                int months = DateUtil.monthBetween(DateUtil.getCurrentDatetime(), start);
                query.setRegisterBorrowMonths(months);
            }
        }

        //两位小数、四舍五入 历史最高负债=待还+已还,注意:如果此处设置值则不会投"首借"的标
        double highestDebt = loanListingDetail.getHighestDebt();
        if (highestDebt != NumberUtils.DOUBLE_ZERO) { //借款人最高历史负债为0 首借
            Double owingHighestDebtRatio = BigDecimal.valueOf(loanListingDetail.getOwingAmount()).divide(BigDecimal.valueOf(highestDebt), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            query.setOwingHighestDebtRatio(owingHighestDebtRatio); //待还金额/历史最高负债
            //添加埋点
            if (owingHighestDebtRatio > 1) {
                logger.error("埋点:待还金额/历史最高负债大于1");
            }
            if (owingHighestDebtRatio < 0) {
                logger.error("埋点:待还金额/历史最高负债小于0");
            }
        }

        //两位小数、四舍五入 历史最高负债=待还+已还,注意:如果此处设置值则不会投"首借"的标
        if (highestDebt != NumberUtils.DOUBLE_ZERO) { //借款人最高历史负债为0 首借
            Double amtDebtRat = BigDecimal.valueOf(loanListingDetail.getAmount()).divide(BigDecimal.valueOf(highestDebt), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            query.setAmtDebtRat(amtDebtRat); //本次借款/历史最高负债
            //添加埋点
            if (amtDebtRat > 1) {
                logger.error("埋点:本次借款/历史最高负债大于1");
            }
            if (amtDebtRat < 0) {
                logger.error("埋点:本次借款/历史最高负债小于0");
            }
        }

        return query;
    }

    //异步保存成功投标记录
    class SaveUserLoanRecordCallable implements Callable<List<Integer>> {
        private UserLoanRecord userLoanRecord;

        SaveUserLoanRecordCallable(UserLoanRecord userLoanRecord) {
            this.userLoanRecord = userLoanRecord;
        }

        @Override
        public List<Integer> call() {
            Long userId = userLoanRecord.getUserId();
            UserLoanResult userLoanResult = userLoanService.addUserLoanRecord(userLoanRecord);
            if (userLoanResult.isFailed()) {
                logger.error("保存用户投资记录失败,userLoanRecord={},error={}", new Object[]{JSONObject.toJSONString(userLoanRecord), userLoanResult.getErrorMsg()});
                //埋点 TODO
            }
            //更新用户捉宝币余额 扣款
            //保证原子性操作 使用sql事务控制并发update
            UserAccountResult userAccountResult = userAccountService.withholdUserAccountBalance(userId, userLoanRecord.getParticipationAmount());
            if (userAccountResult.isFailed()) {
                logger.error("扣款捉宝币余额失败,userId={},error={}", userId, userAccountResult.getErrorMsg());
                //埋点 TODO
            }
            return null;
        }
    }
}
