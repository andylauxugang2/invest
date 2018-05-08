package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiBaseResponse implements Serializable {

    private static final String RETURNCODE_SUCESS = "0";

    @JsonProperty("ReturnCode")
    private String returnCode; //返回码 0=成功， -1=失败
    @JsonProperty("ReturnMessage")
    private String returnMessage; //失败的消息

    public boolean success() {
        if(RETURNCODE_SUCESS.equals(this.returnCode)){
            return true;
        }
        if(returnMessage == null || returnMessage.trim().equals("")){
            return true;
        }
        return false;
    }
}