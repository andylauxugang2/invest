package com.invest.ivppad.biz.manager;

import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivppad.base.property.PropertyObject;
import com.invest.ivppad.util.ObjectDigitalSignHelper;
import com.invest.ivppad.util.RsaCryptoHelper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xugang on 17/1/9.
 */
@Getter
public abstract class PPDOpenApiManagerBase {
    private static final Logger logger = LoggerFactory.getLogger(PPDOpenApiManagerBase.class);

    @Resource(name = "ppdCacheClientHA")
    protected CacheClientHA ppdCacheClientHA;
    @Value(value = "${openapi.ppd.appid}")
    private String appid;
    @Value(value = "${openapi.ppd.signVersion}")
    private String signVersion;
    @Value(value = "${openapi.ppd.serviceVersion}")
    private String serviceVersion;
    @Value(value = "${openapi.ppd.clientPrivateKey}")
    private String clientPrivateKey;
    @Value(value = "${openapi.ppd.serverPublicKey}")
    private String serverPublicKey;

    @Resource(name = "ppdOpenApiRestTemplate")
    protected RestTemplate ppdOpenApiRestTemplate;


    private RetryTemplate retryTemplate = new RetryTemplate();

    private RsaCryptoHelper rsaCryptoHelper;

    @PostConstruct
    public void init() {
        rsaCryptoHelper = new RsaCryptoHelper(RsaCryptoHelper.PKCSType.PKCS8, this.serverPublicKey, this.clientPrivateKey);
    }

    /**
     * 不带请求头headers
     *
     * @param url
     * @param paramMap
     * @param method
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> ResponseEntity<T> call(String url, Map<String, Object> paramMap, HttpMethod method, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws Exception {
        LocalDateTime startDateTime = LocalDateTime.now();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.setAll(paramMap);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
        ResponseEntity<T> result = ppdOpenApiRestTemplate.exchange(url, method, httpEntity, responseType, uriVariables);
        //埋点请求花费时间
        LocalDateTime endDateTime = LocalDateTime.now();
        logger.info(String.format("请求ppdOpenApi接口成功,耗时埋点:url=%s,times=%s", url, Duration.between(startDateTime, endDateTime).toMillis()));
        return result;
    }

    /**
     * 带 请求头headers
     *
     * @param url
     * @param requestBody
     * @param method
     * @param httpHeaders
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> ResponseEntity<T> call(String url, String requestBody, HttpMethod method, HttpHeaders httpHeaders, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws Exception {
        LocalDateTime startDateTime = LocalDateTime.now();
        HttpEntity<String> entityCredentials = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<T> result = ppdOpenApiRestTemplate.exchange(url, method, entityCredentials, responseType, uriVariables);
        //埋点请求花费时间
        LocalDateTime endDateTime = LocalDateTime.now();
        logger.info(String.format("请求ppdOpenApi接口成功,耗时埋点:url=%s,times=%s", url, Duration.between(startDateTime, endDateTime).toMillis()));
        return result;
    }

    public String postForObject(String url, Map<String, Object> paramMap) throws Exception {
        LocalDateTime startDateTime = LocalDateTime.now();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.setAll(paramMap);
        String result = ppdOpenApiRestTemplate.postForObject(url, param, String.class);
        //埋点请求花费时间
        LocalDateTime endDateTime = LocalDateTime.now();
        logger.info(String.format("请求ppdOpenApi接口成功,耗时埋点:url=%s,times=%s", url, Duration.between(startDateTime, endDateTime).toMillis()));
        return result;
    }

    /**
     * 带重试 不带请求头headers
     *
     * @param url
     * @param paramMap
     * @param method
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws Exception
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1))
    public <T> ResponseEntity<T> callRetry(String url, Map<String, Object> paramMap, HttpMethod method, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws Exception {
        //抛出任何Exception都会重试，直到策略终止，调用recoveryCallback
        ResponseEntity<T> result = retryTemplate.execute(context -> call(url, paramMap, method, responseType, uriVariables));
        return result;
    }

    /**
     * 带重试 带请求头headers
     *
     * @param url
     * @param requestBody
     * @param method
     * @param httpHeaders
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws Exception
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public <T> ResponseEntity<T> callRetry(String url, String requestBody, HttpMethod method, HttpHeaders httpHeaders, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws Exception {
        //抛出任何Exception都会重试，直到策略终止，调用recoveryCallback
        ResponseEntity<T> result = retryTemplate.execute(context -> call(url, requestBody, method, httpHeaders, responseType, uriVariables));
        return result;
    }

    /**
     * 获取通用请求头
     *
     * @param accessToken
     * @param propertyObjects
     * @return
     */
    protected HttpHeaders getRequestHeadersCommon(String accessToken, PropertyObject... propertyObjects) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            headers.add("X-PPD-APPID", this.appid);
            headers.add("X-PPD-SIGNVERSION", this.signVersion);
            headers.add("X-PPD-SERVICEVERSION", this.serviceVersion);

            //获取UTC时间作为时间戳
            Calendar cal = Calendar.getInstance();
            int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
            int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
            cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long timestamp = (cal.getTime().getTime() - dateFormat.parse("1970-01-01 00:00:00").getTime()) / 1000;
            headers.add("X-PPD-TIMESTAMP", timestamp.toString()); //UTC时间戳 yyyy-MM-dd HH:mm:ss
            //对时间戳进行签名
            headers.add("X-PPD-TIMESTAMP-SIGN", rsaCryptoHelper.sign(this.appid + timestamp).replaceAll("\\r", "").replaceAll("\\n", ""));

            //使用私钥对请求报文体进行签名
            String sign = rsaCryptoHelper.sign(ObjectDigitalSignHelper.getObjectHashString(propertyObjects)).replaceAll("\\r", "").replaceAll("\\n", "");//签名
            headers.add("X-PPD-SIGN", sign);

            if (accessToken != null && !"".equals(accessToken)) headers.add("X-PPD-ACCESSTOKEN", accessToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return headers;
    }

    /**
     * 获取通用请求头
     *
     * @param accessToken
     * @param propertyObjects
     * @return
     */
    protected Map<String, String> getRequestCommonHeaders(String accessToken, PropertyObject... propertyObjects) {
        Map<String, String> headers = new LinkedHashMap<>();
        try {
            headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            headers.put("X-PPD-APPID", this.appid);
            headers.put("X-PPD-SIGNVERSION", this.signVersion);
            headers.put("X-PPD-SERVICEVERSION", this.serviceVersion);

            //获取UTC时间作为时间戳
            Calendar cal = Calendar.getInstance();
            int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
            int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
            cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long timestamp = (cal.getTime().getTime() - dateFormat.parse("1970-01-01 00:00:00").getTime()) / 1000;
            headers.put("X-PPD-TIMESTAMP", timestamp.toString()); //UTC时间戳 yyyy-MM-dd HH:mm:ss
            //对时间戳进行签名
            headers.put("X-PPD-TIMESTAMP-SIGN", rsaCryptoHelper.sign(this.appid + timestamp).replaceAll("\\r", "").replaceAll("\\n", ""));

            //使用私钥对请求报文体进行签名
            String sign = rsaCryptoHelper.sign(ObjectDigitalSignHelper.getObjectHashString(propertyObjects)).replaceAll("\\r", "").replaceAll("\\n", "");//签名
            headers.put("X-PPD-SIGN", sign);

            if (accessToken != null && !"".equals(accessToken)) headers.put("X-PPD-ACCESSTOKEN", accessToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return headers;
    }

    protected String decryptData(String encryptData) {
        String result = null;
        try {
            result = rsaCryptoHelper.decryptByPrivateKey(encryptData);
        } catch (Exception e) {
            logger.error("解密数据失败", e);
        }
        return result;
    }
}
