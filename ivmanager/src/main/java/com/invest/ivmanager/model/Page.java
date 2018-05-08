package com.invest.ivmanager.model;

/**
 * Created by xugang on 15/9/17.
 */
public class Page {
    private int total;
    private int limit;
    private int start;
    private String listSql;
    private String totalSql;
    private boolean isAll;
    private boolean isDetail;
    private String sort; // 排序字段
    private String dir; // 升序/降序

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getListSql() {
        return listSql;
    }

    public void setListSql(String listSql) {
        this.listSql = listSql;
    }

    public String getTotalSql() {
        return totalSql;
    }

    public void setTotalSql(String totalSql) {
        this.totalSql = totalSql;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setIsAll(boolean isAll) {
        this.isAll = isAll;
    }

    public boolean isDetail() {
        return isDetail;
    }

    public void setIsDetail(boolean isDetail) {
        this.isDetail = isDetail;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
