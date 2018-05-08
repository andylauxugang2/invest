package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiQueryUserBalanceResponse extends PPDOpenApiBaseResponse {

    @JsonProperty("Balance")
    private List<Balance> balanceList; //加密后的用户名称

    @Data
    public static class Balance {
        @JsonProperty("AccountCategory")
        private String accountCategory; //用户备付金.用户投标锁定\用户备付金.用户现金余额\用户备付金.用户提现锁定
        @JsonProperty("Balance")
        private BigDecimal balance; //100.00 0.00
    }
}