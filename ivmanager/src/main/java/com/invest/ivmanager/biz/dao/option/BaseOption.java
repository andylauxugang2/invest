package com.invest.ivmanager.biz.dao.option;

import java.lang.reflect.Field;

/**
 * Created by xugang on 15/7/9.
 */
public class BaseOption {

    private boolean updateCreateTime;
    private boolean updateUpdateTime;

    public boolean isNeedUpdateDB() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getBoolean(this)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean isUpdateCreateTime() {
        return updateCreateTime;
    }

    public void setUpdateCreateTime(boolean updateCreateTime) {
        this.updateCreateTime = updateCreateTime;
    }

    public boolean isUpdateUpdateTime() {
        return updateUpdateTime;
    }

    public void setUpdateUpdateTime(boolean updateUpdateTime) {
        this.updateUpdateTime = updateUpdateTime;
    }
}
