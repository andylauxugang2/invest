package com.invest.ivcommons.util.string;

import java.util.Random;

/**
 * Created by xugang on 2017/7/28.
 */
public abstract class StringUtil {

    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String numberChar = "0123456789";

    /**
     * 获取length位随机码 包括大小写数字
     *
     * @param length
     * @return
     */
    public static String genRandomMixString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }

    /**
     * 获取六位随机验证码
     *
     * @return
     */
    public static int getRandomNumberSix() {
        return (int) (Math.random() * 900000 + 100000);
    }

    /**
     * 获取length位随机码 包括数字
     *
     * @param length
     * @return
     */
    public static String genRandomNumString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return sb.toString();
    }
}
