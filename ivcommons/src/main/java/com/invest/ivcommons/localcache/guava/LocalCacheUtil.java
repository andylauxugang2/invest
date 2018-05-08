package com.invest.ivcommons.localcache.guava;

import com.google.common.base.Throwables;
import com.google.common.cache.*;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 基于guava的本地cache工具类
 * Created by xugang on 2017/7/30.
 */
public class LocalCacheUtil {

    public static <K, V> LoadingCache<K, V> newCache(final AbstractLocalCache<K, V> cache) {

        CacheLoader<K, V> cacheLoader = new CacheLoader<K, V>() {
            private ListeningExecutorService wrapperPoolExecutor = cache.getListeningExecutorService();

            @Override
            public V load(K key) throws Exception {
                try {
                    return cache.load(key);
                } catch (Exception e) {
                    AbstractLocalCache.logger.error("{} load key({}) error! {}", new Object[]{cache.name(), key, Throwables.getStackTraceAsString(e)});
                    throw e;
                }
            }

            @Override
            public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
                boolean async = (wrapperPoolExecutor != null);
                if (AbstractLocalCache.logger.isDebugEnabled()) {
                    AbstractLocalCache.logger.debug("{} refresh key {} by {}", new Object[]{cache.name(), key, async ? "async" : "sync"});
                }
                if (!async) {
                    return Futures.immediateFuture(load(key));
                } else {
                    return wrapperPoolExecutor.submit(() -> load(key));
                }
            }
        };

        return newCache(cache.concurrencyLevel(), cache.getMaxSize(), cache.getMaxWeight(), cache.getWeigher(),
                cache.getRefreshInSeconds(), cacheLoader, cache.getRemovalListener());
    }


    public static <K, V> LoadingCache<K, V> newCache(int concurrencyLevel, int maxSize, int maxWeight, Weigher<K, V> weigher,
                                                     int refreshInSeconds, CacheLoader<K, V> cacheLoader, RemovalListener removalListener) {
        CacheBuilder cacheBuilder = CacheBuilder.newBuilder().recordStats()
                .concurrencyLevel(concurrencyLevel > 0 ? concurrencyLevel : 8);
        if (maxSize > 0) {
            cacheBuilder.maximumSize(maxSize);
        } else if (maxWeight > 0 && weigher != null) {
            cacheBuilder.maximumWeight(maxWeight).weigher(weigher);
        }
        if (removalListener != null) {
            cacheBuilder.removalListener(removalListener);
        }
        if (refreshInSeconds > 0) {
            cacheBuilder.refreshAfterWrite(refreshInSeconds, TimeUnit.SECONDS);
        }
        return cacheBuilder.build(cacheLoader);
    }


}
