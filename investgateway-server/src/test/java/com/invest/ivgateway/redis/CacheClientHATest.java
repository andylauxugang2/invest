package com.invest.ivgateway.redis;

import com.invest.ivcommons.core.AppProfile;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by xugang on 2017/7/29.
 */
public class CacheClientHATest extends TestCase {

    CacheClientHA clientHA = new CacheClientHA("127.0.0.1", 6379, "", 5000, true);

    @Before
    public void init(){
        AppProfile.getAppName();
    }

    @Test
    public void testKey() throws Exception {

    }

    @Test
    public void testString() throws Exception {
        clientHA.String().setex("string", 15, (new Date()).toString());
        byte[] data = clientHA.String().getBit("string");
        System.out.println(new String(data));
    }

    public void testHash() throws Exception {
        clientHA.Hash().hset("hash", "user", "xugang");
        Map<String, String> map = clientHA.Hash().hgetall("hash");
        System.out.println(map);
        clientHA.Hash().hdel("hash", "user");
    }

    public void testSet() throws Exception {
        clientHA.Set().sadd("set", "1");
        clientHA.Set().sadd("set", "2");
        Set<String> sets = clientHA.Set().smembers("set");
        System.out.println(sets);

        clientHA.Set().saddBit("set2", "3".getBytes());
        Set<byte[]> sets2 = clientHA.Set().smembersBit("set2");
        System.out.println(sets2);
    }

    public void testList() throws Exception {

    }

    public void testPubsub() throws Exception {

    }
}