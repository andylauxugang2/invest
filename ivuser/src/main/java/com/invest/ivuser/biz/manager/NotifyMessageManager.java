package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.dao.query.UserNotifyMessageQuery;
import com.invest.ivuser.model.entity.NotifyMessage;
import com.invest.ivuser.model.entity.UserNotifyMessage;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class NotifyMessageManager extends BaseManager {

    public UserNotifyMessage getUserNotifyMessageById(Long userMessageId) {
        UserNotifyMessage result;
        try {
            result = userNotifyMessageDAO.selectByPrimaryKey(userMessageId);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    public List<UserNotifyMessage> getUserNotifyMessageList(UserNotifyMessageQuery query) {
        List<UserNotifyMessage> result;
        try {
            result = userNotifyMessageDAO.selectListByQuery(query);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    public int getUserNotifyMessageTotalCount(UserNotifyMessageQuery query) {
        int count = 0;
        try {
            count = userNotifyMessageDAO.selectCountByQuery(query);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return count;
    }

    public void updateUserNotifyMessage(UserNotifyMessage userNotifyMessage) {
        try {
            userNotifyMessageDAO.updateByPrimaryKeySelective(userNotifyMessage);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public NotifyMessage getNotifyMessageById(Long id) {
        NotifyMessage result;
        try {
            result = notifyMessageDAO.selectByPrimaryKey(id);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    public void saveNotifyMessage(UserNotifyMessage userNotifyMessage) {
        try {
            int line = notifyMessageDAO.insert(userNotifyMessage);
            if (line == 1) {
            } else {
                throw new IVDAOException("插入消息数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入消息失败,userNotifyMessage=" + JSONObject.toJSONString(userNotifyMessage), e);
            throw new IVDAOException(e);
        }
    }

    public void saveUserNotifyMessage(UserNotifyMessage userNotifyMessage) {
        try {
            int line = userNotifyMessageDAO.insert(userNotifyMessage);
            if (line == 1) {
            } else {
                throw new IVDAOException("插入用户消息数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入用户消息失败,userNotifyMessage=" + JSONObject.toJSONString(userNotifyMessage), e);
            throw new IVDAOException(e);
        }
    }

    public void updateUserNotifyMessageBatch(List<Long> ids, UserNotifyMessage userNotifyMessage) {
        try {
            userNotifyMessageDAO.updateByPrimaryKeySelectiveBatch(ids, userNotifyMessage);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }
}
