package com.invest.ivcommons.util.collections;

import java.util.Collection;
import java.util.List;

/**
 * Created by xugang on 2017/8/8.
 */
public class CollectionUtil {
    public static final Collection NULL_COLLECTION = new NullCollection();

    public static final <T> Collection<T> nullCollection() {
        return (List<T>) NULL_COLLECTION;
    }
}
