package com.invest.ivcommons.localcache.guava;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.Weigher;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xugang on 2017/7/30.
 */
public abstract class AbstractLocalCache<K, V> {
    public static final int UN_SET = -1; //未设置的属性值
    private static final ListeningExecutorService listeningPoolExecutor = MoreExecutors.listeningDecorator(new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedTransferQueue<>()));
    public static Logger logger = LoggerFactory.getLogger("LocalCache");
    LoadingCache<K, V> loadingCache = LocalCacheUtil.newCache(this);

    /**
     * 加载KEY的值
     * 如果key的值传null，则表示加载全量数据
     */
    public abstract V load(K key) throws Exception;

    /**
     * 本地缓存的名字, 默认当前类名, 覆写此方法以实现自定义名字
     */
    public String name() {
        return getClass().getSimpleName();
    }

    /**
     * 当底层资源不存在某个key对应的值的时候，需要填充一个空值，防止穿透底层资源, 覆写此方法以实现空值填充
     */
    public V nullValue() {
        return null;
    }

    public V get(K key) {
        try {
            return loadingCache.getUnchecked(key);
        } catch (CacheLoader.InvalidCacheLoadException cacheLoadException) {
            if (logger.isWarnEnabled()) {
                logger.warn("Loading from {} local cache error, key is {}, error message is {}", new Object[]{name(), key, cacheLoadException.getMessage()});
            }
            V nullValue = nullValue();
            if (nullValue != null) {
                loadingCache.put(key, nullValue);
            }
            return nullValue;
        } catch (Exception e) {
            logger.error("Loading from {} local cache error, key is {}, error message is {}", new Object[]{name(), key, e.getMessage()});
            return nullValue();
        }
    }

    public void remove(K key) {
        loadingCache.invalidate(key);
    }

    public void put(K key, V v) {
        loadingCache.put(key, v);
    }

    //清空全部缓存
    public void removeAll() {
        loadingCache.invalidateAll();
    }

    //查看缓存大小
    public long cacheSize() {
        return loadingCache.size();
    }

    //查看缓存统计
    public String cacheStats() {
        return loadingCache.stats().toString();
    }

    /**
     * 最大的缓存数量(默认无限大), 达到最大容量时的剔除算法是LRU
     */
    public int getMaxSize() {
        return Integer.MAX_VALUE;
    }

    /**
     * 最大权重值
     */
    public int getMaxWeight() {
        return UN_SET;
    }

    /**
     * 缓存对象权重计算器
     */
    public Weigher<K, V> getWeigher() {
        return null;
    }

    /**
     * 本地刷新的频率（秒），可以覆写本方法以定制刷新频率
     */
    public int getRefreshInSeconds() {
        return 60 * 60;
    }

    /**
     * 定义item移除监听器
     */
    protected RemovalListener getRemovalListener() {
        return null;
    }

    /**
     * 缓存刷新线程池, 非空则异步刷新缓存. 即:当旧值更新的时候异步更新，但当次请求依然返回旧缓存值. 默认异步刷新
     */
    protected ListeningExecutorService getListeningExecutorService() {
        return listeningPoolExecutor;
    }

    /**
     * 缓存内部存储的并发更新级别
     */
    public int concurrencyLevel() {
        return 8;
    }
}
