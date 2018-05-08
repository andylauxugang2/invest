package com.invest.ivmanager.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/10/31.do best.
 */
@Data
public class UserManagerResult extends Result {
    private String token;
    private List<UserThirdBindInfo> userThirdBindInfos;
}
