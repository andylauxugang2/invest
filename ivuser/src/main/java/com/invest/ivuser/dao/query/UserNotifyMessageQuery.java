package com.invest.ivuser.dao.query;

import lombok.Data;

/**
 * Created by xugang on 2017/8/10.
 */
@Data
public class UserNotifyMessageQuery extends PagedBaseQuery {
    private Long userId;
    private Short status;
}
