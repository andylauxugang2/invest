package com.invest.ivmanager.annotations;

import java.lang.annotation.*;

/**
 * 标注到类或方法上后，该资源未登录也可以访问了
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SkipTokenValidation {
    String uri() default "/invest";
}
