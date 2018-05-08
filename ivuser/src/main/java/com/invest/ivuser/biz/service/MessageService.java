package com.invest.ivuser.biz.service;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.NotifyMessage;
import com.invest.ivuser.model.entity.UserNotifyMessage;
import com.invest.ivuser.model.param.UserNotifyMessageParam;
import com.invest.ivuser.model.result.UserNotifyMessageResult;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
public interface MessageService {

    UserNotifyMessageResult getUserNotifyMessageById(Long userMessageId);

    UserNotifyMessageResult getUserNotifyMessageList(UserNotifyMessageParam userNotifyMessageParam);

    UserNotifyMessageResult getUserNotifyMessageCount(UserNotifyMessageParam userNotifyMessageParam);

    Result modifyUserNotifyMessage(UserNotifyMessage userNotifyMessage);

    Result modifyUserNotifyMessageBatch(List<Long> ids, UserNotifyMessage userNotifyMessage);

    UserNotifyMessageResult getNotifyMessageById(Long messageId);

    Result addUserNotifyMessage(UserNotifyMessage userNotifyMessage);
}
