package com.invest.ivcommons.localcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 静态表的内存缓存
 * Created by xugang on 2017/7/30.
 */
public abstract class TableCache<T> implements InitializingBean {
    public static Logger logger = LoggerFactory.getLogger("TableCache");

    private static ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);

    private AtomicReference<T> ref = new AtomicReference<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        this.refreshCache();
        if (getRefreshIntervalInSec() <= 0) {
            return;
        }
        newScheduledThreadPool.scheduleAtFixedRate(this::refreshCache,
                this.getRefreshIntervalInSec(), this.getRefreshIntervalInSec(), TimeUnit.SECONDS);
    }

    public T get() {
        return this.ref.get();
    }

    public void refreshCache() {
        try {
            ref.set(TableCache.this.load());
        } catch (Exception e) {
            logger.error("Refresh " + cacheName() + " failed!", e);
        }
    }

    public String cacheName() {
        return getClass().getSimpleName();
    }

    protected abstract long getRefreshIntervalInSec();

    protected abstract T load() throws Exception;
}
