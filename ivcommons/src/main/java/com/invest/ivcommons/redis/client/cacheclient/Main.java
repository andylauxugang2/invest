package com.invest.ivcommons.redis.client.cacheclient;


import com.invest.ivcommons.core.AppProfile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yanjie on 2016/5/3.
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        single();

    }

    public static void single(){
        CacheClientHA clientHA = new CacheClientHA("127.0.0.1", 6379, "", 5000, true);

//        clientHA.pubsub().addListener((channel,data)->{
//            System.out.println(channel+new String(data));
//        });

//        for (int i=0;i<100;i++)
//        clientHA.pubsub().subscribe("bbb","ccc","ddd","eee","fff","rrrr","ttrtr","tyttyt","erere");
//
//        Thread.sleep(2000);
        //clientHA.pubsub().subscribe("bbb","ccc","ddd","eee");

        //Thread.sleep(2);
//        clientHA.pubsub().publish("aaa.1","xiaoaaa");
        clientHA.pubsub().publish("bbb","xiaobbb");
//        clientHA.pubsub().publish("ccc","xiaoccc");
//        clientHA.pubsub().publish("fff","xiaofff");
        //  System.in.read();
        //  clientHA.String().setexBit("test",100, (new Date()).toString().getBytes());
        //   KeyScanCursor<String> xxxx = clientHA.Key().scan("*", null);
        //   if (!xxxx.isFinished())
        //       clientHA.Key().scan("*",xxxx);
        //   System.out.println(clientHA.String().getBit("test").length);
        //ValueScanCursor<String> resultList = clientHA.Set().sscan("test", "test*", null);

        while (true) {
            try {
                clientHA.String().setex(UUID.randomUUID().toString(), 15,(new Date()).toString());
                byte[] data = clientHA.String().getBit("aaa");
                //System.out.println(new String(data));
                //System.out.println(clientHA.String().get("aaa"));
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
//            try {
//                //Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                //e.printStackTrace();
//            }
//        }
        }
    }

    public static void cluster(){
        AppProfile.getAppName();
        CacheClientHA clientHA = new CacheClientHA("base.cache", true);

//        clientHA.pubsub().addListener((channel,data)->{
//            System.out.println(channel+new String(data));
//        });

//        for (int i=0;i<100;i++)
//        clientHA.pubsub().subscribe("bbb","ccc","ddd","eee","fff","rrrr","ttrtr","tyttyt","erere");
//
//        Thread.sleep(2000);
        //clientHA.pubsub().subscribe("bbb","ccc","ddd","eee");

        //Thread.sleep(2);
//        clientHA.pubsub().publish("aaa.1","xiaoaaa");
        clientHA.pubsub().publish("bbb","xiaobbb");
//        clientHA.pubsub().publish("ccc","xiaoccc");
//        clientHA.pubsub().publish("fff","xiaofff");
        //  System.in.read();
        //  clientHA.String().setexBit("test",100, (new Date()).toString().getBytes());
        //   KeyScanCursor<String> xxxx = clientHA.Key().scan("*", null);
        //   if (!xxxx.isFinished())
        //       clientHA.Key().scan("*",xxxx);
        //   System.out.println(clientHA.String().getBit("test").length);
        //ValueScanCursor<String> resultList = clientHA.Set().sscan("test", "test*", null);

        while (true) {
            try {
                clientHA.String().setex(UUID.randomUUID().toString(), 15,(new Date()).toString());
                byte[] data = clientHA.String().getBit("aaa");
                //System.out.println(new String(data));
                //System.out.println(clientHA.String().get("aaa"));
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
//            try {
//                //Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                //e.printStackTrace();
//            }
//        }
        }
    }
}
