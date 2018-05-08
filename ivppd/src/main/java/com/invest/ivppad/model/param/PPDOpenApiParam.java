package com.invest.ivppad.model.param;

import lombok.Data;

import java.util.Map;

/**
 * Created by xugang on 16/11/2.
 */
@Data
public class PPDOpenApiParam extends PPDOpenApiBaseParam {
    private static final long serialVersionUID = -3148889180208129245L;
    private int cityId;
    private int ruleId; //当前选择的登录方式 id
    private Map<String, Object> params; //额外参数

}
