package com.invest.ivcommons.redis.client.cacheclient;

/**
 * Created by yanjie on 2016/5/3.
 */
public enum CacheGroupType
{
    M,//主从
    S,//单机
    C,//集群
    R,//读写分离
    //Z,//分片
    P,//Redis Proxy
}
