package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserNotifyMessageParam extends PagedBaseParam {
    private Short status;
    private boolean paged;
    private int page;
    private int limit;
}
