package com.invest.ivuser.datacache;

import com.invest.ivcommons.localcache.TableCache;
import com.invest.ivuser.dao.db.NotifyMessageDAO;
import com.invest.ivuser.model.entity.NotifyMessage;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xugang on 17/1/8.
 */
@Component
public class NotifyMessageDataCache extends TableCache {
    private static final long DEFAULT_SECONDS = 60 * 60;

    protected Map<String, NotifyMessage> notifyMessageMap = new HashMap<>();

    @Autowired
    protected NotifyMessageDAO notifyMessageDAO;

    @Override
    protected long getRefreshIntervalInSec() {
        return DEFAULT_SECONDS;
    }

    public NotifyMessage getNotifyMessageByUniqKey(String messageUniKey) {
        if (StringUtils.isEmpty(messageUniKey)) {
            logger.error("输入参数不能为空");
            return null;
        }
        NotifyMessage result = notifyMessageMap.get(messageUniKey);
        return result;
    }

    @Override
    protected NotifyMessage load() throws Exception {

        NotifyMessage param = new NotifyMessage();
        List<NotifyMessage> notifyMessages = notifyMessageDAO.selectListBySelective(param);
        Map<String, NotifyMessage> tempMap = new HashMap();

        if (CollectionUtils.isEmpty(notifyMessages)) {
            logger.warn("notifyMessages records is empty! Please check it !");
            notifyMessageMap = tempMap;
            return null;
        }


        notifyMessages.forEach(o -> {
            String key = o.getUniqueKey();
            if (o.getUniqueKey() == null) {
                return;
            }
            NotifyMessage notifyMessage = tempMap.get(key);
            if (notifyMessage == null) {
                tempMap.put(key, o);
            }
        });

        notifyMessageMap = tempMap;
        logger.info("SUCCESS load all NotifyMessage records, total db size: {}, key size: {}, expected key size: no limit", notifyMessages.size(), tempMap.keySet().size());
        return null;
    }
}
