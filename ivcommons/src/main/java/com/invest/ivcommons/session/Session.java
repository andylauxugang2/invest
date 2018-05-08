package com.invest.ivcommons.session;

import java.util.Set;

/**
 * Created by xugang on 2017/7/29.
 */
public interface Session {

    String getId();

    <T> T getAttribute(String attributeName);

    Set<String> getAttributeNames();

    void setAttribute(String attributeName, Object attributeValue);

    void removeAttribute(String attributeName);

}
