package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.UserAccount;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserAccountResult extends Result {

    private static final long serialVersionUID = -6851418907032637594L;

    private UserAccount userAccount;
}
