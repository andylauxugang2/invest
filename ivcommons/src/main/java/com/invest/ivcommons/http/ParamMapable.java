package com.invest.ivcommons.http;

import java.util.Map;

/**
 * Created by xugang on 17/1/4.
 */
public interface ParamMapable {
    /**
     * 将对象field转为Map<FieldName, FieldValue>
     * @return
     */
    Map<String, Object> toParamMap();
}
