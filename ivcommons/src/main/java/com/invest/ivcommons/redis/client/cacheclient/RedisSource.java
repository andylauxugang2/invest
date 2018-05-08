package com.invest.ivcommons.redis.client.cacheclient;

import com.invest.ivcommons.core.AppProfile;
import com.invest.ivcommons.redis.client.cacheclient.redisPool.RedisPoolManager;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by yanjie on 2016/5/5.
 */
//ok-xyj
public class RedisSource {
    protected static Logger logger = LoggerFactory.getLogger(RedisSource.class);

    private static AtomicReference<HashMap<String, CacheConfig>> allDic = new AtomicReference<>();
    private static String cacheName;

    public String getCacheName() {
        return cacheName;
    }

    private static final Object lockObj = new Object();

    static {
        RedisSource.configCenterClient_ConfigChanged(getCacheConfigReSource());
    }

    private static String getCacheConfigReSource() {
        try {
            InputStream inputStream = RedisSource.class.getClass().getResourceAsStream("/redis/ivcommons-cache-config.xml");
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = inputStream.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
            return out.toString();
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return null;
    }

    /*
    * 哨兵的处理不再使用固定数量的哨兵,而是采用各个项目都建立独立的哨兵进行管理,这个方法没有作用了
    * */
    @Deprecated
    public static String[] getSentinelList() {
        return new String[0];
    }

    static void buildPool() {
        synchronized (lockObj) {
            String config = null;
            try {
                config = getCacheConfigReSource();
                //不再需要读取哨兵信息
//                String sentinel = ConfigCenterClient.get("TCBase.Cache", "SentinelList");
//                String[] sentinelArray = StringUtils.split(sentinel, ';');
//                if (sentinelArray.length == 0) {
//                    throw new RuntimeException("can not find sentinelList");
//                }
//                sentinelList.set(sentinelArray);
                buildCacheDic(config);
                logger.info("RedisSource-BuildPool-info", config);
            } catch (Exception ex) {
                if (StringUtils.isBlank(config))
                    logger.error(ex.getMessage(), ex, "config is null!");
                else
                    logger.error(ex.getMessage(), ex, config);
            }
        }
    }

    public static CacheConfig getConfig(String name) {
        HashMap<String, CacheConfig> map = allDic.get();
        if (map == null)
            return null;
        return map.getOrDefault(name, null);
    }

    private static void configCenterClient_ConfigChanged(String data) {
        //哨兵部分不用监控
//        if (data.getProjectName().equals("TCBase.Cache")) {
//            try {
//                String sentinel = ConfigCenterClient.get("TCBase.Cache", "SentinelList");
//                String[] sentinelArray = StringUtils.split(sentinel, ';');
//                if (sentinelArray.length == 0) {
//                    throw new RuntimeException("can not find sentinelList");
//                }
//                sentinelList.set(sentinelArray);
//            } catch (Throwable throwable) {
//                ComponentLog.LogMessage("TCBase.Cache", "configCenterClient_ConfigChanged-SentinelList-error", LogType.Info, throwable, data);
//                return;
//            }
//
//        }

        if(StringUtils.isEmpty(data)){
            return;
        }
        String cacheInfo = data;
        logger.info("切换缓存库开始");
        buildCacheDic(cacheInfo);
        logger.info("切换缓存库结束");
    }

    @SuppressWarnings("unchecked")
    private static void buildCacheDic(String config) {
        if(config == null){
            logger.warn("cache-config.xml file not found!");
            return;
        }
        synchronized (lockObj) {
            HashMap<String, CacheConfig> configDic = new HashMap<>();

            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(new StringReader(config));
                Element element = document.getRootElement();
                if (!"base.cache".equals(element.getName())) {
                    return;
                }
                cacheName = element.attributeValue("name") == null ? AppProfile.getAppName().trim() : element.attributeValue("name").trim();
                Iterator<Element> iterator = element.elementIterator("cache");
                while (iterator.hasNext()) {
                    Element cacheElement = iterator.next();
                    CacheConfig cacheConfig = new CacheConfig();
                    String nameAttr = cacheElement.attributeValue("name");
                    if (StringUtils.isBlank(nameAttr)) {
                        continue;
                    }
                    cacheConfig.name = nameAttr.trim();

                    cacheConfig.setEnabled(cacheElement.attributeValue("enabled") == null
                            ? false
                            : Boolean.parseBoolean(cacheElement.attributeValue("enabled").trim()));
                    cacheConfig.setPlus(cacheElement.attributeValue("plus") == null
                            ? false
                            : Boolean.parseBoolean(cacheElement.attributeValue("plus").trim()));

                    cacheConfig.setMasterName(cacheElement.attributeValue("masterName") == null
                            ? null
                            : cacheElement.attributeValue("masterName").trim());

                    cacheConfig.setReadSlave(cacheElement.attributeValue("readSlave") == null
                            ? false
                            : Boolean.parseBoolean(cacheElement.attributeValue("readSlave").trim()));

                    cacheConfig.setScene(cacheElement.attributeValue("scene") == null
                            ? ""
                            : cacheElement.attributeValue("scene").trim());

                    cacheConfig.setType(cacheElement.attributeValue("type") == null
                            ? CacheGroupType.S
                            : CacheGroupType.valueOf(cacheElement.attributeValue("type").trim()));

                    Iterator<Element> redisIterator = cacheElement.elementIterator("redis");
                    while (redisIterator.hasNext()) {
                        Element redisElement = redisIterator.next();
                        RedisConfig redisConfig = new RedisConfig();
                        redisConfig.setIp(redisElement.attributeValue("ip") == null
                                ? null
                                : redisElement.attributeValue("ip").trim());
                        redisConfig.setSentinel(redisElement.attributeValue("sentinel") == null
                                ? false
                                : Boolean.parseBoolean(redisElement.attributeValue("sentinel").trim()));

                        redisConfig.setRedisName(redisElement.attributeValue("redisName") == null
                                ? "redis"
                                : redisElement.attributeValue("redisName").trim());
                        redisConfig.setTimeOut(redisElement.attributeValue("timeOut") == null
                                ? 1000
                                : Integer.parseInt(redisElement.attributeValue("timeOut").trim()));
                        redisConfig.setEnabled(redisElement.attributeValue("enabled") == null
                                ? true
                                : Boolean.parseBoolean(redisElement.attributeValue("enabled").trim()));
                        redisConfig.setPassword(redisElement.attributeValue("password") == null
                                ? null
                                : redisElement.attributeValue("password").trim());
                        redisConfig.setType(redisElement.attributeValue("type") == null
                                ? "redis"
                                : redisElement.attributeValue("type").trim());
                        cacheConfig.redisConfig.add(redisConfig);
                    }
                    configDic.put(cacheConfig.getName(), cacheConfig);
                }
                allDic.set(configDic);
                RedisPoolManager.replacePool(configDic.values().toArray(new CacheConfig[configDic.size()]));
                logger.info("替换CacheDic成功");
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
