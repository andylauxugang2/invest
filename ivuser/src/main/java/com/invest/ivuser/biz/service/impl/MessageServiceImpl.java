package com.invest.ivuser.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivuser.biz.manager.NotifyMessageManager;
import com.invest.ivuser.biz.service.MessageService;
import com.invest.ivuser.common.IVUserErrorEnum;
import com.invest.ivuser.dao.query.UserNotifyMessageQuery;
import com.invest.ivuser.model.entity.NotifyMessage;
import com.invest.ivuser.model.entity.UserNotifyMessage;
import com.invest.ivuser.model.param.UserNotifyMessageParam;
import com.invest.ivuser.model.result.UserNotifyMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class MessageServiceImpl implements MessageService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotifyMessageManager notifyMessageManager;

    @Override
    public UserNotifyMessageResult getUserNotifyMessageById(Long userMessageId) {
        UserNotifyMessageResult result = new UserNotifyMessageResult();
        try {
            UserNotifyMessage userNotifyMessage = notifyMessageManager.getUserNotifyMessageById(userMessageId);
            result.setUserNotifyMessage(userNotifyMessage);
        } catch (Exception e) {
            logger.error("查询用户消息失败,id=" + userMessageId, e);
            IVUserErrorEnum.QUERY_USER_MESSAGE_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public UserNotifyMessageResult getUserNotifyMessageList(UserNotifyMessageParam userNotifyMessageParam) {
        UserNotifyMessageResult result = new UserNotifyMessageResult();
        try {
            UserNotifyMessageQuery query = new UserNotifyMessageQuery();
            query.setUserId(userNotifyMessageParam.getUserId());
            query.setStatus(userNotifyMessageParam.getStatus());
            boolean paged = userNotifyMessageParam.isPaged();
            query.setPaged(paged);
            int pageStart = (userNotifyMessageParam.getPage() - 1) * userNotifyMessageParam.getLimit();
            query.setPageStart(pageStart);
            query.setPageLimit(userNotifyMessageParam.getLimit());
            List<UserNotifyMessage> list = notifyMessageManager.getUserNotifyMessageList(query);
            int count = list.size();
            if (paged && count != 0) {
                count = notifyMessageManager.getUserNotifyMessageTotalCount(query);
            }
            result.setCount(count);
            result.setUserNotifyMessages(list);
        } catch (Exception e) {
            logger.error("查询消息失败,userNotifyMessageParam=" + JSONObject.toJSONString(userNotifyMessageParam), e);
            IVUserErrorEnum.QUERY_USER_MESSAGE_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public UserNotifyMessageResult getUserNotifyMessageCount(UserNotifyMessageParam userNotifyMessageParam) {
        UserNotifyMessageResult result = new UserNotifyMessageResult();
        try {
            UserNotifyMessageQuery query = new UserNotifyMessageQuery();
            query.setUserId(userNotifyMessageParam.getUserId());
            query.setStatus(userNotifyMessageParam.getStatus());
            int count = notifyMessageManager.getUserNotifyMessageTotalCount(query);
            result.setCount(count);
        } catch (Exception e) {
            logger.error("查询消息失败,userNotifyMessageParam=" + JSONObject.toJSONString(userNotifyMessageParam), e);
            IVUserErrorEnum.QUERY_USER_MESSAGE_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public Result modifyUserNotifyMessage(UserNotifyMessage userNotifyMessage) {
        Result result = new Result();
        try {
            notifyMessageManager.updateUserNotifyMessage(userNotifyMessage);
        } catch (Exception e) {
            logger.error("更新消息失败,userNotifyMessage=" + JSONObject.toJSONString(userNotifyMessage), e);
            IVUserErrorEnum.QUERY_USER_MESSAGE_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public Result modifyUserNotifyMessageBatch(List<Long> ids, UserNotifyMessage userNotifyMessage) {
        Result result = new Result();
        try {
            notifyMessageManager.updateUserNotifyMessageBatch(ids, userNotifyMessage);
        } catch (Exception e) {
            logger.error("批量更新消息失败,ids=" + ids, e);
            IVUserErrorEnum.QUERY_USER_MESSAGE_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public UserNotifyMessageResult getNotifyMessageById(Long id) {
        UserNotifyMessageResult result = new UserNotifyMessageResult();
        try {
            NotifyMessage notifyMessage = notifyMessageManager.getNotifyMessageById(id);
            result.setNotifyMessage(notifyMessage);
        } catch (Exception e) {
            logger.error("查询消息失败,id=" + id, e);
            IVUserErrorEnum.QUERY_USER_MESSAGE_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public Result addUserNotifyMessage(UserNotifyMessage userNotifyMessage) {
        Result result = new Result();
        try {
            notifyMessageManager.saveUserNotifyMessage(userNotifyMessage);
        } catch (Exception e) {
            logger.error("保存消息失败,userNotifyMessage=" + JSONObject.toJSONString(userNotifyMessage), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }
}
