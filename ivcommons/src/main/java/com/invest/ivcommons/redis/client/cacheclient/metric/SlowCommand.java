package com.invest.ivcommons.redis.client.cacheclient.metric;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by admin on 2017/5/17.
 */
public class SlowCommand {
    private String commandName;
    private String key;
    private AtomicLong valueCount;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AtomicLong getValueCount() {
        return valueCount;
    }

    public SlowCommand(String commandName, String key,long time) {
        this.commandName = commandName;
        this.key = key;
        this.time=time;
        this.valueCount = new AtomicLong();
    }
}
