package com.invest.ivcommons.util.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by xugang on 2017/8/2.
 */
public final class NumberUtil {
    private static final Logger logger = LoggerFactory.getLogger(NumberUtil.class);

    public static String formatPattern(Double value, String pattern) {
        if (value == null) {
            return null;
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(value);
    }

    public static String formatDecimalToPercentage(double decimal, int minimumFractionDigits) {
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(minimumFractionDigits);
        return nt.format(decimal);
    }

    public static void main(String[] args) {
        System.out.println(formatPattern(112.0, "00.##"));
        System.out.println(formatDecimalToPercentage(0.1223, 2));

    }

}
