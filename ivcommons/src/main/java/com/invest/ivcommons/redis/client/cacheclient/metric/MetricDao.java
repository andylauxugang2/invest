package com.invest.ivcommons.redis.client.cacheclient.metric;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/26.
 */
public class MetricDao {
    long success;
    long error;
    List<SlowCommand>  slowCommandList;
    List<MetricExp> metricExpList;
    Map<String,Integer> maxKeys;

    public Map<String,Integer> getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(Map<String,Integer> maxKeys) {
        this.maxKeys = maxKeys;
    }

    public long getSuccess() {
        return success;
    }

    public void setSuccess(long success) {
        this.success = success;
    }

    public long getError() {
        return error;
    }

    public void setError(long error) {
        this.error = error;
    }

    public List<SlowCommand> getSlowCommandList() {
        return slowCommandList;
    }

    public void setSlowCommandList(List<SlowCommand> slowCommandList) {
        this.slowCommandList = slowCommandList;
    }

    public List<MetricExp> getMetricExpList() {
        return metricExpList;
    }

    public void setMetricExpList(List<MetricExp> metricExpList) {
        this.metricExpList = metricExpList;
    }
}
