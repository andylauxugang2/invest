package com.invest.ivcommons.redis.client.cacheclient.redisPool;

import com.google.gson.Gson;
import com.invest.ivcommons.redis.client.cacheclient.CacheConfig;
import com.invest.ivcommons.redis.client.cacheclient.RedisConfig;
import com.invest.ivcommons.redis.client.cacheclient.StringByteCodec;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**ok
 * Created by yanjie on 2016/5/5.
 *
 */
public class SingleRedisConnectionPool extends RedisConnectionPoolBase {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public SingleRedisConnectionPool(CacheConfig cacheConfig) {
        super(cacheConfig);
        try {
            if (cacheConfig.getRedisConfig().size() <= 0) {
                throw new RuntimeException("至少要一个有效的缓存配置!");
            }
            RedisConfig config = cacheConfig.getRedisConfig().get(0);
            RedisURI.Builder builder = RedisURI.Builder.redis(config.getHost(), config.getPort());
            if (!StringUtils.isBlank(config.getPassword())) {
                builder = builder.withPassword(config.getPassword());
            }
            builder = builder.withTimeout(config.getTimeOut() != 0 ? config.getTimeOut() : 5, TimeUnit.SECONDS);
            RedisURI uri = builder.build();
            client = RedisClient.create(uri);

            stringConnection = client.connect();
            byteConnection = client.connect(new StringByteCodec());
            pubConnection=client.connectPubSub(new StringByteCodec());
            //此处不初始化pubsub,因为发布订阅用的较少,需要1个单独的连接,不用的话浪费
        } catch (Throwable throwable) {
            Gson gson=new Gson();
            logger.error(throwable.getMessage(), throwable, gson.toJson(cacheConfig));
            throw throwable;
        }
    }
}
