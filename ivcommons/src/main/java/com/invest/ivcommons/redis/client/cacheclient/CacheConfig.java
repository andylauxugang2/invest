package com.invest.ivcommons.redis.client.cacheclient;

import java.util.ArrayList;
import java.util.List;

/**ok
 * Created by yanjie on 2016/5/3.
 *
 */
public class CacheConfig {
    public CacheConfig() {
        redisConfig = new ArrayList<>();
    }

    List<RedisConfig> redisConfig;
    String name;
    private boolean readSlave;
    private boolean enabled;
    private CacheGroupType type;
    private String scene;

    private String masterName;
    private boolean plus;

    private boolean isOffline;

    public List<RedisConfig> getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(List<RedisConfig> redisConfig) {
        this.redisConfig = redisConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReadSlave() {
        return readSlave;
    }

    public void setReadSlave(boolean readSlave) {
        this.readSlave = readSlave;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CacheGroupType getType() {
        return type;
    }

    public void setType(CacheGroupType type) {
        this.type = type;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public boolean isPlus() {
        return plus;
    }

    public void setPlus(boolean plus) {
        this.plus = plus;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }
}
