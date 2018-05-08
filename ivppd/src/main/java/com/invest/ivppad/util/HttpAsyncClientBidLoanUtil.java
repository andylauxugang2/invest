package com.invest.ivppad.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by xugang on 2017/9/5.do best.
 */
public class HttpAsyncClientBidLoanUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpAsyncClientBidLoanUtil.class);

    private static CloseableHttpAsyncClient httpclient;

    static {
        ConnectingIOReactor ioReactor = null;
        try {
            //配置io线程
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors() * 20).build();
            //设置连接池大小
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setMaxTotal(1000);
        cm.setDefaultMaxPerRoute(1000);
        httpclient = HttpAsyncClients.custom().setConnectionManager(cm).build();
        // Start the client
        httpclient.start();

    }

    public static void post(String url, Map<String, String> param, Map<String, String> headerMap, FutureCallback<HttpResponse> futureCallback) throws Exception {
        // 以post方式请求
        HttpPost request = new HttpPost(url);
        StringEntity params = new StringEntity(JSONObject.toJSONString(param));
        request.setEntity(params);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        httpclient.execute(request, futureCallback);
    }

}
