package com.invest.ivuser.datacache;

import com.invest.ivcommons.localcache.TableCache;
import com.invest.ivuser.biz.service.BlackListService;
import com.invest.ivuser.model.entity.BlackListThird;
import com.invest.ivuser.model.result.BlackListResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xugang on 17/1/8.
 */
@Component
public class BlackListThirdDataCache extends TableCache {
    private static final long DEFAULT_SECONDS = 60 * 2;

    protected MultiKeyMap blackListThirdMap = new MultiKeyMap();

    @Value("${refreshBlackListThirdInSec}")
    private Integer refreshBlackListThirdInSec;

    @Resource
    protected BlackListService blackListService;

    @Override
    protected long getRefreshIntervalInSec() {
        if (this.refreshBlackListThirdInSec == null) {
            return DEFAULT_SECONDS;
        }
        return refreshBlackListThirdInSec;
    }

    public boolean isInThirdBlackList(String type, String value) {
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(value)) {
            logger.error("获取第三方账户黑名单本地缓存数据失败,输入参数不能为空");
            return false;
        }
        BlackListThird result = (BlackListThird) blackListThirdMap.get(type, value);
        return result != null;
    }

    @Override
    protected Integer load() throws Exception {

        BlackListThird param = new BlackListThird();
        param.setIsDelete(Boolean.FALSE);
        BlackListResult blackListResult = blackListService.getBlackListThirdList(param);

        if (blackListResult.isFailed()) {
            logger.error("查询黑名单失败,result=" + blackListResult.getErrorMsg());
            return null;
        }

        MultiKeyMap tempMultiKeyMap = new MultiKeyMap();

        List<BlackListThird> blackListThirds = blackListResult.getBlackListThirds();
        if (CollectionUtils.isEmpty(blackListThirds)) {
            blackListThirdMap = tempMultiKeyMap;
            return null;
        }

        blackListThirds.stream().forEach(o -> {
            String type = o.getBlacklistType();
            String value = o.getBlacklistValue();
            BlackListThird result = (BlackListThird) tempMultiKeyMap.get(type, value);
            if (result == null) {
                tempMultiKeyMap.put(type, value, o);
            }
        });

        blackListThirdMap = tempMultiKeyMap;
        logger.info("加载黑名单成功,dbsize={},keysize={}", blackListThirds.size(), blackListThirdMap.keySet().size());
        return null;
    }
}
