package com.invest.ivcommons.util.format;

import com.invest.ivcommons.base.money.Money;
import com.invest.ivcommons.base.money.MoneyType;

import java.util.Locale;

/**
 * Created by xugang on 2017/8/2.
 */
public final class MoneyUtil {
    private static MoneyType type = new MoneyType(Locale.CHINA);

    /**
     * 格式化 人民币 ￥1.00
     *
     * @param smallDenomination
     * @return
     */
    public static String formatMoneyRMB(int smallDenomination) {
        Money money = type.money(smallDenomination);
        return money.toString();
    }

    /**
     * 转为 分 便于计算
     * @param normalDenomination
     * @return
     */
    public static int toSmallDenomination(int normalDenomination){
        return normalDenomination * 100;
    }
}
