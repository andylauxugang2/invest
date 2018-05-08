package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class PagedBaseParam extends BaseParam {
    private boolean paged;
    private int page;
    private int limit;
}
