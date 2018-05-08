package com.invest.ivpush.message.model.dataobject;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * Created by xugang on 2017/7/27.
 */
public class Pagination<T extends Entity> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
