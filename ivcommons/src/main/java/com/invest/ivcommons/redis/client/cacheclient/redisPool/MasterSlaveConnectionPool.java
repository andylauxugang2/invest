package com.invest.ivcommons.redis.client.cacheclient.redisPool;

import com.google.gson.Gson;
import com.invest.ivcommons.redis.client.cacheclient.CacheConfig;
import com.invest.ivcommons.redis.client.cacheclient.RedisConfig;
import com.invest.ivcommons.redis.client.cacheclient.StringByteCodec;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.sentinel.api.StatefulRedisSentinelConnection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ok
 * Created by yanjie on 2016/5/5.
 */
class MasterSlaveConnectionPool extends RedisConnectionPoolBase {
    private Gson gson = new Gson();
    private StatefulRedisSentinelConnection<String, String> sentinelConnection;
    protected static Logger logger = LoggerFactory.getLogger(MasterSlaveConnectionPool.class);


    @Override
    public void dispose() {
        try {
            if (sentinelConnection != null)
                sentinelConnection.close();
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
        super.dispose();
    }

    MasterSlaveConnectionPool(CacheConfig config) {
        super(config);
        try {
            List<RedisConfig> list = config.getRedisConfig();
            if (list.size() == 0) {
                throw new RuntimeException("没有配置缓存信息!");
            }
            RedisURI.Builder builder = RedisURI.builder();
            for (RedisConfig redisConfig : list) {
                if (!redisConfig.isSentinel())
                    continue;
                builder = builder.withSentinel(redisConfig.getHost(), redisConfig.getPort());
            }
            builder.withSentinelMasterId(config.getMasterName());

            builder = builder.withTimeout(list.get(0).getTimeOut() == 0 ? list.get(0).getTimeOut() : 5000, TimeUnit.MILLISECONDS);
            if (!StringUtils.isBlank(list.get(0).getPassword())) {
                builder = builder.withPassword(list.get(0).getPassword());
            }
            RedisURI uri = builder.build();
            client = RedisClient.create(uri);
            stringConnection = client.connect();
            byteConnection = client.connect(new StringByteCodec());
            sentinelConnection = client.connectSentinel();
            pubConnection = client.connectPubSub(new StringByteCodec());
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable, gson.toJson(config));
        }
    }
}
