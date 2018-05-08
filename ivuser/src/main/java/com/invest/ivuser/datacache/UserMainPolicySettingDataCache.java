package com.invest.ivuser.datacache;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.localcache.TableCache;
import com.invest.ivuser.common.UserPolicyStatusEnum;
import com.invest.ivuser.dao.db.UserMainPolicyDAO;
import com.invest.ivuser.model.vo.UserMainPolicyVO;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by xugang on 17/1/8.
 */
@Component
public class UserMainPolicySettingDataCache extends TableCache {
    private static final long DEFAULT_SECONDS = 60;

    @Value("${refreshUserMainPolicySettingInSec}")
    private Long refreshUserMainPolicySettingInSec;

    protected MultiKeyMap userMainPolicyMap = new MultiKeyMap();

    @Autowired
    protected UserMainPolicyDAO userMainPolicyDAO;

    @Override
    protected long getRefreshIntervalInSec() {
        if (this.refreshUserMainPolicySettingInSec == null) {
            return DEFAULT_SECONDS;
        }
        return refreshUserMainPolicySettingInSec;
    }

    public UserMainPolicyVO getUserMainPolicyByUniqKey(Long userId, String thirdUUID, Long mainPolicyId) {
        if (userId == null || StringUtils.isEmpty(thirdUUID) || mainPolicyId == null) {
            logger.error("输入参数不能为空");
            return null;
        }
        logger.debug("内存主策略列表:userMainPolicyMap={}", JSONObject.toJSONString(userMainPolicyMap));
        UserMainPolicyVO result = (UserMainPolicyVO) userMainPolicyMap.get(userId, thirdUUID, mainPolicyId);
        return result;
    }

    @Override
    protected UserMainPolicyVO load() throws Exception {

        List<UserMainPolicyVO> userMainPolicyVOs = userMainPolicyDAO.selectUserMainPolicyDetailList(null, null, null,
                UserPolicyStatusEnum.start.getCode());
        MultiKeyMap tempMultiKeyMap = new MultiKeyMap();

        if (CollectionUtils.isEmpty(userMainPolicyVOs)) {
            logger.error("userMainPolicyVOs records is empty! Please check it !");
            userMainPolicyMap = tempMultiKeyMap;
            return null;
        }


        userMainPolicyVOs.forEach(o -> {
            Long userId = o.getUserId();
            String thirdUserUUID = o.getThirdUserUUID();
            Long mainPolicyId = o.getMainPolicyId();
            UserMainPolicyVO userMainPolicyVO = (UserMainPolicyVO) tempMultiKeyMap.get(userId, thirdUserUUID, mainPolicyId);
            if (userMainPolicyVO == null) {
                tempMultiKeyMap.put(userId, thirdUserUUID, mainPolicyId, o);
            }
        });

        userMainPolicyMap = tempMultiKeyMap;
        logger.info("SUCCESS load all usermainpolicy records, total db size: {}, key size: {}, expected key size: no limit", userMainPolicyVOs.size(), tempMultiKeyMap.keySet().size());
        return null;
    }
}
