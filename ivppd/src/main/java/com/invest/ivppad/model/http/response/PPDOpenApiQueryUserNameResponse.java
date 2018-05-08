package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiQueryUserNameResponse extends PPDOpenApiBaseResponse {

    @JsonProperty("UserName")
    private String userName; //加密后的用户名称

}