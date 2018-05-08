package com.invest.ivmanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 15/9/17.
 */
public class ListRange {
    private List data = new ArrayList();
    private int totalSize;

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
