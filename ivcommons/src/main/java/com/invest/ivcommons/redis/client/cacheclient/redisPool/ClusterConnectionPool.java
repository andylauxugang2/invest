package com.invest.ivcommons.redis.client.cacheclient.redisPool;

import com.invest.ivcommons.core.AppProfile;
import com.invest.ivcommons.redis.client.cacheclient.*;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulConnection;
import com.lambdaworks.redis.cluster.ClusterClientOptions;
import com.lambdaworks.redis.cluster.ClusterTopologyRefreshOptions;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016/5/13.
 */
class ClusterConnectionPool extends RedisConnectionPoolBase {
    private RedisClusterClient client;
    private StatefulRedisClusterConnection<String, String> stringConnection;
    private StatefulRedisClusterConnection<String, byte[]> byteConnection;
    private StatefulRedisPubSubConnection<String, byte[]> pubConnection;

    public RedisClusterClient getClusterClient() {
        return this.client;
    }

    @Override
    public StatefulConnection<String, String> getStringConnection(boolean isRead) {
        if (stringConnection != null)
            return stringConnection;
        if (isStringConnectionSemaphore.tryAcquire()) {
            try {
                if (client == null)
                    return null;
                stringConnection = client.connect();
            } catch (Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
            } finally {
                isStringConnectionSemaphore.release();
            }
        }
        if (stringConnection != null)
            return stringConnection;
        else {
            return null;
        }
    }

    @Override
    public StatefulConnection<String, byte[]> getByteConnection(boolean isRead) {
        if (byteConnection != null)
            return byteConnection;
        if (isByteConnectionSemaphore.tryAcquire()) {
            try {
                if (client == null)
                    return null;
                byteConnection = client.connect(new StringByteCodec());
            } catch (Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
            } finally {
                isByteConnectionSemaphore.release();
            }
        }
        if (byteConnection != null)
            return byteConnection;
        else {
            return null;
        }
    }

    @Override
    public StatefulRedisPubSubConnection<String, byte[]> getPubConnection() {
        if (pubConnection != null)
            return pubConnection;
        if (isPubConnectionSemaphore.tryAcquire()) {
            try {
                if (client == null)
                    return null;
                pubConnection = client.connectPubSub(new StringByteCodec());
            } catch (Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
            } finally {
                isPubConnectionSemaphore.release();
            }
        }
        if (pubConnection != null)
            return pubConnection;
        else {
            return null;
        }
    }

    @Override
    public StatefulRedisPubSubConnection<String, byte[]> getSubByteConnection() {
        try {
            if (client == null)
                return null;
            return client.connectPubSub(new StringByteCodec());
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new RuntimeException(throwable);
        }
    }

    ClusterConnectionPool(CacheConfig cacheConfig) {
        super(cacheConfig);
        try {
            if (!cacheConfig.getType().equals(CacheGroupType.C)) {
                throw new RuntimeException("状态错误,必须是集群模式");
            }
            List<RedisURI> redisURIList = new ArrayList<>();
            for (RedisConfig redisConfig : cacheConfig.getRedisConfig()) {
                RedisURI uri = new RedisURI(redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeOut() != 0 ? redisConfig.getTimeOut() : 5, TimeUnit.SECONDS);
                if (StringUtils.isNotBlank(redisConfig.getPassword())) {
                    uri.setPassword(redisConfig.getPassword());
                }
                redisURIList.add(uri);
            }

            client = RedisClusterClient.create(redisURIList);

            ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                    .enablePeriodicRefresh(15, TimeUnit.SECONDS)
                    .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
                    .adaptiveRefreshTriggersTimeout(10, TimeUnit.SECONDS)
                    .build();

            client.setOptions(ClusterClientOptions.builder()
                    .topologyRefreshOptions(topologyRefreshOptions)
                    .autoReconnect(true)
                    .build());

            //client.getResources().commandLatencyCollector().retrieveMetrics()

            // RedisURI uri = builder.build();
//            this.client = RedisClusterClient.create(uri);
//            this.client.setOptions(new ClusterClientOptions.Builder()
//                    .refreshClusterView(true)
//                    .refreshPeriod(5, TimeUnit.SECONDS)
//                    .build());
            stringConnection = client.connect();
            byteConnection = client.connect(new StringByteCodec());
            pubConnection = client.connectPubSub(new StringByteCodec());

        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }

    @Override
    public void dispose() {
        try {
            if (stringConnection != null)
                stringConnection.close();
            if (byteConnection != null)
                byteConnection.close();
            if (pubConnection != null)
                pubConnection.close();
            if (client != null)
                client.shutdown();
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }

    public void setClientName() {
        try {
            if (byteConnection != null)
                byteConnection.sync().clientSetname(AppProfile.getAppName() + "_" + AppProfile.getEnvironment() + "_" + CacheClientConstant.version);
            if (stringConnection != null)
                stringConnection.sync().clientSetname(AppProfile.getAppName() + "_" + AppProfile.getEnvironment() + "_" + CacheClientConstant.version);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }
}
