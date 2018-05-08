package com.invest.ivcommons.annotation.webfront;

import java.lang.annotation.*;

/**
 * 标注此注解的类, 类中方法均需要验证token信息
 * 标注此注解的方法, 需要验证token信息
 * Created by xugang on 2016/9/6.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenValidated {
    String uri() default "/houbank";
}
