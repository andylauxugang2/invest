package com.invest.ivusergateway.model.request;

import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class ReadNotifyMessageReq {

    private Long userMessageId;
    private List<Long> userMessageIds;
    private Long userId;
}
