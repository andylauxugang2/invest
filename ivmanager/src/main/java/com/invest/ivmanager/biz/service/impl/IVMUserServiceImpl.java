package com.invest.ivmanager.biz.service.impl;

import com.invest.ivmanager.biz.service.IVMUserService;
import com.invest.ivmanager.model.ListRange;
import com.invest.ivmanager.model.request.UserReq;
import com.invest.ivmanager.model.result.UserManagerResult;
import com.invest.ivuser.biz.manager.UserManager;
import com.invest.ivuser.biz.manager.UserThirdBindInfoManager;
import com.invest.ivuser.biz.service.UserService;
import com.invest.ivuser.model.entity.User;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import com.invest.ivuser.model.result.UserResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/10/13.do best.
 */
@Service("ivmanagerUserService")
public class IVMUserServiceImpl implements IVMUserService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserThirdBindInfoManager userThirdBindInfoManager;

    @Autowired
    private UserService userService;

    @Override
    public ListRange getUsers(UserReq userReq) {
        ListRange listRange = new ListRange();
        User userParam = new User();
        userParam.setId(userReq.getUserId());
        userParam.setMobile(userReq.getMobile());
        List<User> userList = userManager.getUserList(userParam);
        if (CollectionUtils.isEmpty(userList)) {
            return listRange;
        }

        listRange.setData(userList);
        listRange.setTotalSize(userList.size());

        return listRange;
    }

    @Override
    public UserManagerResult loginOneKey(UserReq userReq) {
        UserManagerResult result = new UserManagerResult();
        UserResult userResult = userService.getLoginKey("ivmanager", userReq.getUsername());
        if (userResult.isFailed()) {
            throw new RuntimeException(userResult.getErrorMsg());
        }
        String token = userResult.getToken();
        result.setToken(token);
        return result;
    }

    @Override
    public UserManagerResult getUserThirdUUIDInfo(UserReq userReq) {
        UserManagerResult result = new UserManagerResult();
        User user = userManager.getUserByMobile(userReq.getMobile());
        List<UserThirdBindInfo> userThirdBindInfoList = userThirdBindInfoManager.getByUserId(user.getId());
        if (CollectionUtils.isEmpty(userThirdBindInfoList)) {
            throw new RuntimeException("无第三方绑定");
        }
        result.setUserThirdBindInfos(userThirdBindInfoList);
        return result;
    }
}
