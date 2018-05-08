package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.dao.query.UserNotifyMessageQuery;
import com.invest.ivuser.model.entity.NotifyMessage;
import com.invest.ivuser.model.entity.UserNotifyMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotifyMessageDAO extends BaseDAO<UserNotifyMessage> {

    List<UserNotifyMessage> selectListByQuery(UserNotifyMessageQuery query);

    int selectCountByQuery(UserNotifyMessageQuery query);

    int updateByUserIdAndMessageId(UserNotifyMessage userNotifyMessage);

    void updateByPrimaryKeySelectiveBatch(@Param("ids") List<Long> ids, @Param("userNotifyMessage") UserNotifyMessage userNotifyMessage);
}