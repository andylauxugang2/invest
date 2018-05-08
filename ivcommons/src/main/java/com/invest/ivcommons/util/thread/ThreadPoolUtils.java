package com.invest.ivcommons.util.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by andyxu on 2017/3/21.
 */
@Slf4j
public class ThreadPoolUtils {

    /**
     * 创建线程池
     *
     * @param threadCount
     * @param blockingQueue
     * @return
     */
    public static ExecutorService createBlockingPool(int threadCount, BlockingQueue blockingQueue) {
        return createBlockingPool(8, threadCount, blockingQueue);
    }

    /**
     * 创建线程池
     *
     * @param threadCount
     * @param blockingQueue
     * @return
     */
    public static ExecutorService createBlockingPool(int corePoolSize, int threadCount, BlockingQueue blockingQueue) {
        return new ThreadPoolExecutor(corePoolSize,
                threadCount,
                1L,
                TimeUnit.SECONDS,
                blockingQueue,
                (r, executor) -> {
                    if (!executor.isShutdown()) {
                        try {
                            log.error("reject handler!");
                            executor.getQueue().put(r);
                        } catch (InterruptedException e) {
                            log.error("put block data error!", e);
                        }
                    }
                });
    }

    /**
     * 创建线程池
     *
     * @param corePoolSize
     * @param maxThreadCount
     * @return
     */
    public static ExecutorService createBlockingPool(int corePoolSize, int maxThreadCount) {
        return createBlockingPool(corePoolSize, maxThreadCount, new LinkedTransferQueue());
    }
}
