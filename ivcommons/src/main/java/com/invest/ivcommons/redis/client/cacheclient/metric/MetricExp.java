package com.invest.ivcommons.redis.client.cacheclient.metric;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin on 2017/5/17.
 */
public class MetricExp {
   private String command = "";
    private String key = "";
    private String message = "";
    private AtomicInteger count = new AtomicInteger(0);
    private String stackTrace = "";

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    public MetricExp(String command, String key, Throwable throwable) {
        this.command = command;
        this.key = key;
        this.buildMesssage(throwable);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void buildMesssage(Throwable throwable) {
        if (!this.getMessage().equals(throwable.getMessage())) {
            this.setMessage(throwable.getMessage());
            StringBuilder stackTrack = new StringBuilder();
            for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
                stackTrack.append(stackTraceElement.toString());
            }
            this.setStackTrace(stackTrack.toString());
        }
    }
}
