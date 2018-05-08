package com.invest.ivcommons.redis.client.cacheclient.metric;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin on 2017/5/17.
 *
 */
public class MetricInfo {
    private ConcurrentHashMap<String, SlowCommand> slowCommand = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,MetricExp> expMap=new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Integer> keyCount = new ConcurrentHashMap<>();
    private AtomicInteger success=new AtomicInteger();
    private AtomicInteger error=new AtomicInteger();
    private String projectName;
    private String cacheName;
    private String env;



    public ConcurrentHashMap<String, SlowCommand> getSlowCommand() {
        return slowCommand;
    }

    public void setSlowCommand(ConcurrentHashMap<String, SlowCommand> slowCommand) {
        this.slowCommand = slowCommand;
    }

    public ConcurrentHashMap<String, MetricExp> getExpMap() {
        return expMap;
    }

    public void setExpMap(ConcurrentHashMap<String, MetricExp> expMap) {
        this.expMap = expMap;
    }

    public  AtomicInteger getSuccess() {
        return success;
    }

    public  void setSuccess(AtomicInteger success) {
        this.success = success;
    }

    public  AtomicInteger getError() {
        return error;
    }

    public  void setError(AtomicInteger error) {
        this.error = error;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public void addSlowCommand(String commandName, String key, long time) {
        slowCommand.computeIfPresent(commandName + ":" + key, (k, v) -> {
            v.getValueCount().incrementAndGet();
            if (v.getTime() < time)
                v.setTime(time);
            return v;
        });
        slowCommand.computeIfAbsent(commandName + ":" + key, (p) -> new SlowCommand(commandName, key, time));
    }

    public void addkey(String key) {
        if (keyCount.size() > 10000)
            return;
        keyCount.computeIfPresent(key, (k, v) -> v + 1);
        keyCount.putIfAbsent(key, 1);
    }

    public ConcurrentHashMap<String, Integer> getKeyCount() {
        return keyCount;
    }

    public void addException(String commandName, String key, Throwable throwable) {
        expMap.computeIfPresent(commandName + ":" + key, (k, v) -> {
            v.getCount().incrementAndGet();
            v.buildMesssage(throwable);
            return v;
        });
        expMap.computeIfAbsent(commandName + ":" + key, (p) -> new MetricExp(commandName, key, throwable));
    }

    public void addSuccess()
    {
        success.incrementAndGet();
    }

    public void addError()
    {
        error.incrementAndGet();
    }
}
