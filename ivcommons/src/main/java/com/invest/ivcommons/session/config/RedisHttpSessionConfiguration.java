package com.invest.ivcommons.session.config;

import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivcommons.session.http.CookieSerializer;
import com.invest.ivcommons.session.http.HttpSessionFilter;
import com.invest.ivcommons.session.redis.RedisSessionRepository;
import com.invest.ivcommons.session.utils.HttpConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class RedisHttpSessionConfiguration {

    private Integer expiredTime = 60 * 60;
    private String domainName;
    private Integer cookieMaxAge = -1;

    @Bean
    public RedisSessionRepository redisSessionRepository(@Qualifier("cacheClientHA") CacheClientHA cacheClientHA) {
        RedisSessionRepository sessionRepository = new RedisSessionRepository(cacheClientHA);
        sessionRepository.setMaxIntervalTime(expiredTime);
        return sessionRepository;
    }

    @Bean
    public HttpSessionFilter httpSessinFilter(@Qualifier("redisSessionRepository") RedisSessionRepository redisSessionRepository) {
        CookieSerializer.cookieMaxAge = cookieMaxAge;
        CookieSerializer.domainName = domainName;
        CookieSerializer.cookieName = HttpConstants.COOKIE_NAME;
        return new HttpSessionFilter(redisSessionRepository);
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Integer getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

}
