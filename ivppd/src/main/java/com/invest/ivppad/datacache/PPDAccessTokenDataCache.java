package com.invest.ivppad.datacache;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.localcache.guava.AbstractLocalCache;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivppad.model.PPDUserAccessToken;
import com.invest.ivppad.model.protobuf.PPDUserAccessTokenProtos;
import com.invest.ivppad.util.KeyVersionUtils;
import com.invest.ivppad.util.RedisKeyUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by xugang on 17/3/31.
 */
@Component
public class PPDAccessTokenDataCache extends AbstractLocalCache<String, PPDUserAccessToken> {

    @Resource(name = "ppdCacheClientHA")
    protected CacheClientHA ppdCacheClientHA;

    @Override
    public PPDUserAccessToken load(String username) throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        String key = KeyVersionUtils.rediskeyPpdAccessTokenV(RedisKeyUtils.keyUserNameAccessToken(username));
        //logger.info("根据用户名查询token,key={}", key);
        byte[] bytes = ppdCacheClientHA.String().getBit(key);
        PPDUserAccessToken token = new PPDUserAccessToken();
        if (bytes != null) {
            PPDUserAccessTokenProtos.PPDUserAccessToken tokenTemp = PPDUserAccessTokenProtos.PPDUserAccessToken.parseFrom(bytes);
            token.setAccessToken(tokenTemp.getAccessToken());
            token.setExpiresIn(tokenTemp.getExpiresIn());
            token.setRefreshToken(tokenTemp.getRefreshToken());
        }
        long elapsedTimeInMillis = System.currentTimeMillis() - currentTimeMillis;
        logger.info("加载用户PPDUserAccessToken完毕,key={},token={},times={}", key, JSONObject.toJSONString(token), elapsedTimeInMillis);
        return token;
    }

    @Override
    public int getMaxSize() {
        return 100000;
    }

    @Override
    public int getRefreshInSeconds() {
        return 60;
    }
}
