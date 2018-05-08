package com.invest.ivcommons.util.collections;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * 过滤null元素
 * Created by xugang on 2017/8/8.
 */
public class NullCollection extends AbstractList<Object> implements RandomAccess, Serializable {

    private static final long serialVersionUID = -7303270522523108722L;

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 1;
    }

    public boolean contains(Object obj) {
        return null == obj;
    }

    private Object readResolve() {
        return null;
    }
}
