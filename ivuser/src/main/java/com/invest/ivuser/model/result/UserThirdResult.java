package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserThirdResult extends Result {
    private static final long serialVersionUID = -4619288839107627161L;

    private List<UserThirdBindInfo> userThirdBindInfos;

    private UserThirdBindInfo userThirdBindInfo;

}
