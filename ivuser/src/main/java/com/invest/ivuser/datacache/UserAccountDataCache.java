package com.invest.ivuser.datacache;

import com.invest.ivcommons.localcache.TableCache;
import com.invest.ivuser.dao.db.UserAccountDAO;
import com.invest.ivuser.model.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标专家用户刷新账户余额本地缓存
 * Created by xugang on 17/1/8.
 */
@Component
public class UserAccountDataCache extends TableCache {
    private static final long DEFAULT_SECONDS = 60 * 5;

    /**
     * Map<userId,可投标剩余金额>
     */
    protected Map<Long, Integer> userAccountMap = new HashMap<>();

    @Value("${refreshUserAccountInSec}")
    private Integer refreshUserAccountInSec;

    @Autowired
    protected UserAccountDAO userAccountDAO;

    @Override
    protected long getRefreshIntervalInSec() {
        if (this.refreshUserAccountInSec == null) {
            return DEFAULT_SECONDS;
        }
        return refreshUserAccountInSec;
    }

    public Integer getBidAmountBalanceByUserId(Long userId) {
        if (userId == null) {
            logger.error("获取用户账户可投标剩余金额本地缓存数据失败,输入参数不能为空");
            return null;
        }
        return userAccountMap.get(userId);
    }

    @Override
    protected Integer load() throws Exception {

        UserAccount param = new UserAccount();
        List<UserAccount> userAccounts = userAccountDAO.selectListBySelective(param);
        Map<Long, Integer> tempMap = new HashMap(100);

        if (CollectionUtils.isEmpty(userAccounts)) {
            userAccountMap = tempMap;
            logger.error("userAccounts records is empty! Please check it !");
            return null;
        }

        userAccounts.forEach(o -> {
            Long key = o.getUserId();
            if (key == null) {
                return;
            }
            Integer bidAmountBalance = tempMap.get(key);
            if (bidAmountBalance == null) {
                tempMap.put(key, o.getBidAmountBalance());
            }
        });

        userAccountMap = tempMap;
        logger.info("加载标专家账户成功,dbsize={},keysize={}", userAccounts.size(), tempMap.keySet().size());
        return null;
    }
}
