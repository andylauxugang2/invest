package com.invest.ivuser.model.vo;

import com.invest.ivcommons.util.date.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/8/15.
 */
@Data
public class UserMainPolicyVO {

    private Long mainPolicyId;
    private Long userPolicyId;
    private Long userId;
    private String name;
    private String thirdUserUUID;
    private Integer amountStart;
    private Integer amountMax;
    private Integer accountRemain;

    private Date userMainPolicyCreateTime; //用户主策略添加时间

    private String userMainPolicyCreateTimeFormat;

    private Integer userPolicyStatus;

    public void setUserMainPolicyCreateTime(Date userMainPolicyCreateTime) {
        this.userMainPolicyCreateTime = userMainPolicyCreateTime;
        if (userMainPolicyCreateTime != null) {
            this.userMainPolicyCreateTimeFormat = DateUtil.dateToString(userMainPolicyCreateTime, DateUtil.DATE_FORMAT_DATETIME_COMMON);
        }
    }
}
