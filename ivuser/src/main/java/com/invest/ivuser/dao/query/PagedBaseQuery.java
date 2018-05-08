package com.invest.ivuser.dao.query;

import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/8/10.
 */
@Data
public class PagedBaseQuery {
    private boolean paged; //是否分页
    private int pageStart; //页数 从1开始
    private int pageLimit;
}
