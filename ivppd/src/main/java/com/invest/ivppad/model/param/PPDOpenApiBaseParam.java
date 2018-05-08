package com.invest.ivppad.model.param;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共请求体参数
 * Created by xugang on 16/11/2.
 */
@Data
public class PPDOpenApiBaseParam implements Serializable {

    private String userName;
    private Long userId;

    private Map<String, Object> extParam = new HashMap<>();

}
