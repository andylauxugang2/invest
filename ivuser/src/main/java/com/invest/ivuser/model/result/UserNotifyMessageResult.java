package com.invest.ivuser.model.result;

import com.invest.ivuser.model.entity.NotifyMessage;
import com.invest.ivuser.model.entity.UserNotifyMessage;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserNotifyMessageResult extends PagedBaseResult {

    private static final long serialVersionUID = -7436813682311249816L;

    private List<NotifyMessage> notifyMessages;
    private NotifyMessage notifyMessage;

    private List<UserNotifyMessage> userNotifyMessages;
    private UserNotifyMessage userNotifyMessage;
}
