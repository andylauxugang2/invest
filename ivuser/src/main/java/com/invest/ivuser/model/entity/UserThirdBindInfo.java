package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import com.invest.ivcommons.util.date.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserThirdBindInfo extends BaseEntity {

    private static final long serialVersionUID = -4064105209548065136L;
    private Long userId; //策略所属用户id
    private String thirdUserUUID;
    private Date expiredTime;
    private String accessToken;
    private String refreshToken;
    private String openID;

    //展示
    private String createTimeFormat;
    public void setCreateTime(Date createTime) {
        super.setCreateTime(createTime);
        if(createTime != null){
            this.createTimeFormat = DateUtil.dateToString(createTime, DateUtil.DATE_FORMAT_DATETIME_COMMON);
        }
    }

    private String expiredTimeFormat;
    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
        if(expiredTime != null){
            this.expiredTimeFormat = DateUtil.dateToString(expiredTime, DateUtil.DATE_FORMAT_DATETIME_COMMON);
        }
    }

}
