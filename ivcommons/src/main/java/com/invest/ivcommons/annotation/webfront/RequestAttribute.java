package com.invest.ivcommons.annotation.webfront;

import java.lang.annotation.*;

/**
 * Created by xugang on 16/9/7.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestAttribute {
    /**
     * The request attribute parameter to bind to.
     */
    String value() ;
}
