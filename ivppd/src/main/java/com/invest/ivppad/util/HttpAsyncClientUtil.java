package com.invest.ivppad.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * Created by xugang on 2017/9/5.do best.
 */
public class HttpAsyncClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpAsyncClientUtil.class);

    private static CloseableHttpAsyncClient httpclient;

    static {
        ConnectingIOReactor ioReactor = null;
        try {
            //配置io线程
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors() * 3).build();
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

    public static void main2(String[] args) {
        CloseableHttpAsyncClient httpclient = null;
        try {
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
            PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
            cm.setMaxTotal(100);
            httpclient = HttpAsyncClients.custom().setConnectionManager(cm).build();

            // Start the client
            httpclient.start();

            // Execute request
            final HttpGet request1 = new HttpGet("http://www.apache.org/");
            Future<HttpResponse> future = httpclient.execute(request1, null);
            // and wait until a response is received
            HttpResponse response1 = future.get();
            System.out.println(request1.getRequestLine() + "->" + response1.getStatusLine());

            // One most likely would want to use a callback for operation result
            final CountDownLatch latch1 = new CountDownLatch(1);
            final HttpGet request2 = new HttpGet("http://www.apache.org/");
            httpclient.execute(request2, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response2) {
                    latch1.countDown();
                    System.out.println(request2.getRequestLine() + "->" + response2.getStatusLine());
                }

                public void failed(final Exception ex) {
                    latch1.countDown();
                    System.out.println(request2.getRequestLine() + "->" + ex);
                }

                public void cancelled() {
                    latch1.countDown();
                    System.out.println(request2.getRequestLine() + " cancelled");
                }

            });
            latch1.await();

            // In real world one most likely would also want to stream
            // request and response body content
            final CountDownLatch latch2 = new CountDownLatch(1);
            final HttpGet request3 = new HttpGet("http://www.apache.org/");
            HttpAsyncRequestProducer producer3 = HttpAsyncMethods.create(request3);
            AsyncCharConsumer<HttpResponse> consumer3 = new AsyncCharConsumer<HttpResponse>() {

                HttpResponse response;

                @Override
                protected void onResponseReceived(final HttpResponse response) {
                    this.response = response;
                }

                @Override
                protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
                    // Do something useful
                }

                @Override
                protected void releaseResources() {
                }

                @Override
                protected HttpResponse buildResult(final HttpContext context) {
                    return this.response;
                }

            };
            httpclient.execute(producer3, consumer3, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response3) {
                    latch2.countDown();
                    System.out.println(request2.getRequestLine() + "->" + response3.getStatusLine());
                }

                public void failed(final Exception ex) {
                    latch2.countDown();
                    System.out.println(request2.getRequestLine() + "->" + ex);
                }

                public void cancelled() {
                    latch2.countDown();
                    System.out.println(request2.getRequestLine() + " cancelled");
                }

            });
            latch2.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public static void main(String[] args) {
        try {

            post("https://gw.open.ppdai.com/invest/LLoanInfoService/LoanList", null, null, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    try {
                        logger.info("status=" + response.getStatusLine().getStatusCode());
                        logger.info("length=" + response.getEntity().getContentLength());
                        System.out.println("status=" + response.getStatusLine().getStatusCode());

                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String result = EntityUtils.toString(entity, "UTF-8");
                            System.out.println(result);
                            ObjectMapper mapper = new ObjectMapper();
                            PPDOpenApiLoanListResponse ppdOpenApiLoanListResponse = JSON.parseObject(result, PPDOpenApiLoanListResponse.class);
                            PPDOpenApiLoanListResponse ppdOpenApiLoanListResponse2 = mapper.readValue(result, PPDOpenApiLoanListResponse.class);
                            System.out.println(ppdOpenApiLoanListResponse);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void failed(final Exception ex) {
                }

                public void cancelled() {
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
