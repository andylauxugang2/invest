package com.invest.ivcommons.rocketmq.model;

import com.invest.ivcommons.rocketmq.BaseMQMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by xugang on 2017/8/8.
 */
@Data
@AllArgsConstructor
public class LoanableDetailMessage extends BaseMQMessage {
    private int pageIndex;
    private String encodeMessage; //base64 + protobuf
}
