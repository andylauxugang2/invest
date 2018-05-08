package com.zhuobao.ivsyncjob.job.elastic;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.zhuobao.ivsyncjob.biz.manager.TokenManager;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * accessToken授权后的有效期是7天,失效后需要刷新3个月有效期
 * 1分钟刷新一次
 * 刷新失败后 需要通知用户手动授权
 * Created by xugang on 2017/9/18.
 */
@Component
public class RefreshAccessTokenJob extends ElasticBaseJob {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();

        JSONObject param = super.parseJobParam(shardingContext.getJobParameter());

        //开始下载
        tokenManager.refreshAccessToken();
    }

}
