package com.invest.ivusergateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class SaveUserMainPolicyReq {

    private Long userMainPolicyId; //如果非空表示更改
    private Long userId;
    private Long mainPolicyId;
    private String thirdUserUUID;
    private Integer amountStart;
    private Integer amountMax;
    private Integer accountRemain;
    private Short status;

}
