package com.invest.ivcommons.security.datacache;

import com.google.common.collect.Maps;
import com.invest.ivcommons.localcache.TableCache;
import com.invest.ivcommons.security.SourceEnums;
import com.invest.ivcommons.security.dao.ChannelUrlDAO;
import com.invest.ivcommons.security.domain.ChannelUrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 航司缓存
 * Created by xugang on 17/1/8.
 */
//@Component
public class ChannelUrlDataCache extends TableCache<Map<String, ChannelUrl>> {
    @Value("${ivcommons.datacache.refreshInterval.channelUrlInSec}")
    private Long refreshIntervalSec;

    private static final long DAY_IN_SECONDS = 2 * 60 * 60;

    @Autowired
    protected ChannelUrlDAO channelUrlDAO;

    @Override
    protected long getRefreshIntervalInSec() {
        if(this.refreshIntervalSec == null){
            return DAY_IN_SECONDS;
        }
        return refreshIntervalSec;
    }

    public ChannelUrl getChannelUrlByCodeAndChannel(String channel, String uri) {
        if(StringUtils.isEmpty(channel) || StringUtils.isEmpty(uri)){
            logger.error("输入参数不能为空");
            return null;
        }
        if(SourceEnums.valueOf(channel) == null) return null;

        Map<String, ChannelUrl> channelMap = super.get();
        return channelMap == null ? null : channelMap.get(key(channel, uri));
    }

    private String key(String channel, String uri) {
        return channel + ":" + uri;
    }

    @Override
    protected Map<String, ChannelUrl> load() throws Exception {
        Map<String, ChannelUrl> result = Maps.newHashMap();

        ChannelUrl channelUrl = new ChannelUrl();
        List<ChannelUrl> channelUrlList = channelUrlDAO.selectSelective(channelUrl);

        if (CollectionUtils.isEmpty(channelUrlList)) {
            logger.error("channelUrl records is empty! Please check it !");
            return null;
        }

        channelUrlList.forEach(o -> {
            boolean expired = o.isExpired();
            if (!expired) { //未过期
                result.put(key(o.getChannel(), o.getUrl()), o);
            }
        });

        logger.info("SUCCESS load all ChannelUrl records, total db size: {}, key size: {}, expected key size: no limit", channelUrlList.size(), result.keySet().size());
        return result;
    }
}
