package com.invest.ivppad.datacache;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by xugang on 2017/8/31.do best.
 */
public class UserAccountInfoDataCacheTest extends TestCase {

    public void testUpdateUserAccountBalance() throws Exception {
        UserAccountInfoDataCache cache = new UserAccountInfoDataCache();
        cache.updateUserAccountBalance("ppd1212128080", 1000);
        cache.updateUserAccountBalance("ppd1212128080", 500);
        cache.updateUserAccountBalance("ppd1212128080", 200);
    }
}