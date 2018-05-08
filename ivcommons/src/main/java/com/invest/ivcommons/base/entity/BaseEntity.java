package com.invest.ivcommons.base.entity;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xugang on 2017/7/27.
 */
public class BaseEntity implements Serializable {
    private Long id;
    private boolean isDelete;
    private Date createTime = new DateTime().toDate();
    private Date updateTime = new DateTime().toDate();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
