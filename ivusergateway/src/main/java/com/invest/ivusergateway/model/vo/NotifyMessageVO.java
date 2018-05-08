package com.invest.ivusergateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class NotifyMessageVO {

    private long id;
    private long messageId;
    private String title;
    private String content;
    private String link;
    private String type;
    private short status;

    private String createTime;
}
