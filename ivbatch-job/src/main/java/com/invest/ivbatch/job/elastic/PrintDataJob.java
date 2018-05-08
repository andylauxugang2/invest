/*
package com.invest.ivbatch.job.elastic;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.invest.ivbatch.common.error.ElasticExecuteError;
import com.invest.ivcommons.util.format.JSONUtil;
import com.invest.ivppad.datacache.UserPolicyDataCache;
import com.invest.ivppad.model.entity.UserPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

*/
/**
 * 同步用户账户 包括余额
 * Created by xugang on 2017/9/18.
 *//*

@Component
public class PrintDataJob extends ElasticBaseJob {

    @Autowired
    private UserPolicyDataCache userPolicyDataCache;

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        JSONObject param = super.parseJobParam(shardingContext.getJobParameter());
        int type = JSONUtil.safeGetInt(param, "type", 1); //默认查询内存散标策略快照

        switch (type) {
            case 1: {
                List<UserPolicy> userPolicies = userPolicyDataCache.getAllUserPolicyList();
                logger.info("内存散标策略list:{}", JSONObject.toJSONString(userPolicies));
            }
        }
    }

}
*/
