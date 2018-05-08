package com.invest.ivcommons.redis.client.cacheclient;


/**
 * Created by yanjie on 2016/5/3.
 */
//ok-xyj
public class RedisConfig {
    private String ip;
    private boolean enabled;
    private boolean read;
    private boolean write;
    private String password;
    private int timeOut = 5000;
    private int maxPool = 20;
    private int minPool = 3;
    private String redisName;
    private String type = "redis";
    private boolean sentinel = false;

    public boolean isSentinel() {
        return sentinel;
    }

    public void setSentinel(boolean sentinel) {
        this.sentinel = sentinel;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getMaxPool() {
        return maxPool;
    }

    public void setMaxPool(int maxPool) {
        this.maxPool = maxPool;
    }

    public int getMinPool() {
        return minPool;
    }

    public void setMinPool(int minPool) {
        this.minPool = minPool;
    }

    public String getRedisName() {
        return redisName;
    }

    public void setRedisName(String redisName) {
        this.redisName = redisName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String host;

    public String getHost() {
        if (host != null)
            return host;
        host = ip.split(":")[0];
        return host;
    }

    int port;

    public int getPort() {

        if (port != 0)
            return port;
        port = Integer.parseInt(ip.split(":")[1]);
        return port;
    }

    @Override
    public String toString() {
        return "RedisConfig{" +
                "ip='" + ip + '\'' +
                ", enabled=" + enabled +
                ", read=" + read +
                ", write=" + write +
                ", password='" + password + '\'' +
                ", timeOut=" + timeOut +
                ", maxPool=" + maxPool +
                ", minPool=" + minPool +
                ", redisName='" + redisName + '\'' +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}