package com.invest.ivcommons.rocketmq.model;

import com.invest.ivcommons.rocketmq.BaseMQMessage;
import lombok.Data;

/**
 * Created by xugang on 2017/8/8.
 */
@Data
public class UserLoanPolicyMessage extends BaseMQMessage {

    private static final long serialVersionUID = 445738181563919130L;

    private Long userId;
    private String username; //ppd username
    private Long policyId;

    private Integer bidAmount; //投标金额
}
