/*
package com.invest.ivpush.message.provider.interceptor;

import javax.annotation.Resource;

import com.invest.ivpush.message.provider.service.MessagePlusService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ThirdAuthInterceptor {

	@Resource
	private MessagePlusService messagePlusService;

	@AfterReturning(value="execution(* com.appleframework.message.provider.service.impl.ThirdAuthServiceImpl.save(..))", argNames="rtv", returning="rtv")
	public void afterSaveMethod(JoinPoint jp, final Object rtv) {
		//Long id = (Long)rtv;
		//messagePlusService.remove(id);
    }

	@AfterReturning(value="execution(* com.appleframework.message.provider.service.impl.ThirdAuthServiceImpl.delete(..))", argNames="rtv", returning="rtv")
	public void afterDeleteMethod(JoinPoint jp, final Object rtv) {
		//Long id = (Long)rtv;
		//messagePlusService.remove(id);
    }

	@AfterReturning(value="execution(* com.appleframework.message.provider.service.impl.ThirdAuthServiceImpl.update(..))", argNames="rtv", returning="rtv")
	public void afterUpdateMethod(JoinPoint jp, final Object rtv) {
		//Long id = (Long)rtv;
		//messagePlusService.remove(id);
    }

}*/
