package com.invest.ivuser.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivuser.biz.manager.UserThirdBindInfoManager;
import com.invest.ivuser.biz.service.UserThirdService;
import com.invest.ivuser.common.IVUserErrorEnum;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import com.invest.ivuser.model.param.UserThirdParam;
import com.invest.ivuser.model.result.UserThirdResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class UserThirdServiceImpl implements UserThirdService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserThirdBindInfoManager userThirdBindInfoManager;

    @Override
    public UserThirdResult getUserThirdBindInfo(UserThirdParam userThirdParam) {
        UserThirdResult result = new UserThirdResult();
        Long userId = userThirdParam.getUserId();
        try {
            List<UserThirdBindInfo> userThirdBindInfos = userThirdBindInfoManager.getByUserId(userId);
            //logger.info("查询到用户绑定信息,result={}", JSONObject.toJSONString(userThirdBindInfos));
            result.setUserThirdBindInfos(userThirdBindInfos);
        } catch (Exception e) {
            logger.error("查询到用户绑定的第三方信息失败,userId=" + userId, e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }

        return result;
    }

    @Override
    public UserThirdResult addUserThirdBindInfo(UserThirdBindInfo userThirdBindInfo) {
        UserThirdResult result = new UserThirdResult();
        Long userId = userThirdBindInfo.getUserId();
        String thirdUserUUID = userThirdBindInfo.getThirdUserUUID();
        try {
            //判断是否存在
            List<UserThirdBindInfo> userThirdBindInfoList = userThirdBindInfoManager.getByUserIdAndThirdUserUUID(userId, thirdUserUUID);
            if (CollectionUtils.isEmpty(userThirdBindInfoList)) {
                userThirdBindInfoManager.saveOne(userThirdBindInfo);
                logger.info("插入用户绑定的第三方信息成功,result={}", JSONObject.toJSONString(userThirdBindInfo));
                return result;
            }
            //存在了 直接更新DB
            if (userThirdBindInfoList.size() > 1) {
                throw new IllegalStateException("绑定信息不唯一,size=" + userThirdBindInfoList.size());
            }
            userThirdBindInfoManager.updateOneByUserIdAndThirdUserUUID(userThirdBindInfo);
        } catch (Exception e) {
            logger.error("插入用户绑定的第三方信息失败,userThirdBindInfo=" + JSONObject.toJSONString(userThirdBindInfo), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }
        return result;
    }

    @Override
    public UserThirdResult removeUserThirdBindInfo(Long userId, Long bindId) {
        UserThirdResult result = new UserThirdResult();
        try {
            UserThirdBindInfo userThirdBindInfo = userThirdBindInfoManager.getById(bindId);
            //删db
            userThirdBindInfoManager.delById(bindId);
            logger.info("删除用户绑定信息成功,bindId={},userId={}", bindId, userId);
            result.setUserThirdBindInfo(userThirdBindInfo);
        } catch (Exception e) {
            logger.error("删除用户绑定信息失败,userId=" + userId + ",bindId=" + bindId, e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }

        return result;
    }
}
