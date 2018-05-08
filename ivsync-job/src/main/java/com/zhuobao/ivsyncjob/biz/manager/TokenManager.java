package com.zhuobao.ivsyncjob.biz.manager;

import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.thread.ThreadPoolUtils;
import com.invest.ivppad.biz.manager.PPDOpenApiAuthManager;
import com.invest.ivppad.biz.manager.PPDOpenApiUserManager;
import com.invest.ivppad.model.PPDUserAccessToken;
import com.invest.ivppad.model.http.request.PPDOpenApiRefreshTokenRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiAuthResponse;
import com.invest.ivuser.biz.manager.BlackListThirdManager;
import com.invest.ivuser.biz.manager.UserThirdBindInfoManager;
import com.invest.ivuser.model.entity.BlackListThird;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Created by xugang on 2017/8/6.
 */
@Service
public class TokenManager {
    private static Logger logger = LoggerFactory.getLogger(TokenManager.class);

    @Resource(name = "ppdCacheClientHA")
    private CacheClientHA ppdCacheClientHA;

    @Autowired
    private UserThirdBindInfoManager userThirdBindInfoManager;

    @Autowired
    private PPDOpenApiUserManager openApiUserManager;

    @Autowired
    private PPDOpenApiAuthManager openApiAuthManager;

    @Autowired
    private BlackListThirdManager blackListThirdManager;

    private ExecutorService refreshAccessTokenExcutorService = ThreadPoolUtils.createBlockingPool(10, 20);

    /**
     * 刷新accessToken
     */
    public void refreshAccessToken() {
        //查询所有过期的accessToken
        UserThirdBindInfo dbQuery = new UserThirdBindInfo();
        dbQuery.setExpiredTime(DateUtil.getCurrentDatetime());
        List<UserThirdBindInfo> userThirdBindInfoList = userThirdBindInfoManager.getList(dbQuery);
        if (CollectionUtils.isEmpty(userThirdBindInfoList)) {
            return;
        }
        //多线程刷新 每个username限定只能被一个userid绑定
        userThirdBindInfoList.stream().forEach(o -> {
            doRefresh(o);
        });
    }

    private void doRefresh(UserThirdBindInfo o) {
        refreshAccessTokenExcutorService.submit(new RefreshAccessTokenCallable(o));
    }


    class RefreshAccessTokenCallable implements Callable<PPDUserAccessToken> {
        private UserThirdBindInfo userThirdBindInfo;

        RefreshAccessTokenCallable(UserThirdBindInfo userThirdBindInfo) {
            this.userThirdBindInfo = userThirdBindInfo;
        }

        @Override
        public PPDUserAccessToken call() throws Exception {
            if (userThirdBindInfo == null) {
                return null;
            }
            //判断是否过期 刷新token
            Date expiredDate = userThirdBindInfo.getExpiredTime();
            Date now = DateUtil.getCurrentDatetime();
            if (!now.after(expiredDate)) {
                //未过期无需处理
                return null;
            }

            String username = userThirdBindInfo.getThirdUserUUID();

            //调用刷新接口
            PPDOpenApiRefreshTokenRequest request = new PPDOpenApiRefreshTokenRequest();
            request.setAppID(openApiAuthManager.getAppid());
            request.setOpenID(userThirdBindInfo.getOpenID());
            request.setRefreshToken(userThirdBindInfo.getRefreshToken());
            PPDOpenApiAuthResponse response = openApiAuthManager.refreshToken(username, request);
            if (!response.success()) {
                logger.debug("刷新用户授权AccessToken失败,username={},response={}", username, response);
                return null;
            }

            //过期更新redis token
            PPDUserAccessToken token = new PPDUserAccessToken();
            token.setAccessToken(response.getAccessToken()); //设置token为新的token
            token.setExpiresIn(response.getExpiresIn()); //过期时间90天 单位秒
            token.setRefreshToken(response.getRefreshToken());
            openApiUserManager.saveUserNameAccessToken(username, token);

            //更新db
            userThirdBindInfo.setUpdateTime(now);
            //过期时间减去一天时间 为了提前刷新token
            userThirdBindInfo.setExpiredTime(DateUtil.dateAdd(now, response.getExpiresIn() - 60 * 60 * 24, Calendar.SECOND));
            userThirdBindInfo.setOpenID(response.getOpenID());
            userThirdBindInfo.setAccessToken(response.getAccessToken());
            userThirdBindInfo.setRefreshToken(response.getRefreshToken());
            userThirdBindInfoManager.updateById(userThirdBindInfo);

            logger.info("AccessToken过期重新刷新成功,username={},expiredDate={}", username, DateUtil.dateToString(expiredDate, DateUtil.DATE_FORMAT_DATETIME_COMMON));

            //出狱黑名单
            boolean deleteFlag = blackListThirdManager.deleteBlackListThird(BlackListThird.TYPE_THIRD_USERNAME, username);
            if (deleteFlag) {
                logger.info("AccessToken过期重新刷新成功,出狱黑名单,username={}", username);
            }
            return token;
        }
    }
}
