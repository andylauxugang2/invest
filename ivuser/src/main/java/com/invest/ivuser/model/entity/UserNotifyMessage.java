package com.invest.ivuser.model.entity;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserNotifyMessage extends NotifyMessage {

    private static final long serialVersionUID = -1824553973146764718L;

    public static final short STATUS_UNREAD = 0;
    public static final short STATUS_READED = 1;

    private Long userId;
    private Short status;
    private Long messageId;
    private String messageValue;

}
