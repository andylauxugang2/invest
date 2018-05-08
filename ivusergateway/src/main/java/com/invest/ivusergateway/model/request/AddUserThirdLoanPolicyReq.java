package com.invest.ivusergateway.model.request;

import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class AddUserThirdLoanPolicyReq {

    private Long userId;
    private Long policyId;
    private List<String> thirdUUIDs;
}
