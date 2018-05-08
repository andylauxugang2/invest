package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiLoanBaseResponse implements Serializable {

    private static final int RESULT_SUCESS = 1;

    @JsonProperty("Result")
    private int result; //返回码 0：错误 1：成功 -1：异常
    @JsonProperty("ResultMessage")
    private String resultMessage; //失败的消息
    @JsonProperty("ResultCode")
    private String resultCode;

    public boolean success() {
        if (RESULT_SUCESS == this.result) {
            return true;
        }
        return false;
    }
}