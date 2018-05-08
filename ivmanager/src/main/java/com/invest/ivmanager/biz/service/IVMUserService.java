package com.invest.ivmanager.biz.service;

import com.invest.ivmanager.model.ListRange;
import com.invest.ivmanager.model.request.UserReq;
import com.invest.ivmanager.model.result.UserManagerResult;

/**
 * Created by xugang on 2017/10/12.do best.
 */
public interface IVMUserService {

    ListRange getUsers(UserReq userReq);

    UserManagerResult loginOneKey(UserReq userReq);

    UserManagerResult getUserThirdUUIDInfo(UserReq userReq);
}
