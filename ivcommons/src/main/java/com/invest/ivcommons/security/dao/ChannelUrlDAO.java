package com.invest.ivcommons.security.dao;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivcommons.security.domain.ChannelUrl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelUrlDAO extends BaseDAO<ChannelUrl> {
    ChannelUrl selectByUrlCodeAndChannel(ChannelUrl channelUrl);

    List<ChannelUrl> selectSelective(ChannelUrl channelUrl);
}