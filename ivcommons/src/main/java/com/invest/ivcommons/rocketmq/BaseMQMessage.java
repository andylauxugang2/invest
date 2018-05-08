package com.invest.ivcommons.rocketmq;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xugang on 2017/8/8.
 */
@Data
public class BaseMQMessage implements Serializable {
    public static final String OPT_TYPE_ADD = "add"; //新增
    public static final String OPT_TYPE_DEL = "del"; //删除
    public static final String OPT_TYPE_UPD = "upd"; //修改 包括新增
    public static final String OPT_TYPE_UPD_ALL = "upd_all"; //修改所有

    private String optType; //操作类型
}
