package com.invest.ivuser.biz.service;

import com.invest.ivuser.model.entity.UserThirdBindInfo;
import com.invest.ivuser.model.param.*;
import com.invest.ivuser.model.result.UserResult;
import com.invest.ivuser.model.result.UserThirdResult;

/**
 * Created by xugang on 2017/7/28.
 */
public interface UserThirdService {


    /**
     * 查询第三方绑定信息
     */
    UserThirdResult getUserThirdBindInfo(UserThirdParam userThirdParam);

    /**
     * 添加
     */
    UserThirdResult addUserThirdBindInfo(UserThirdBindInfo userThirdBindInfo);

    UserThirdResult removeUserThirdBindInfo(Long userId, Long bindId);
}
