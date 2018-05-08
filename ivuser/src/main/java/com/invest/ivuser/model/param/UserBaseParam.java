package com.invest.ivuser.model.param;

import com.invest.ivcommons.base.param.BaseParam;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserBaseParam extends BaseParam {
    private Long userId;
    private String thirdUserUUID;
}
