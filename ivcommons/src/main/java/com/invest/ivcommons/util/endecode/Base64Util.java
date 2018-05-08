package com.invest.ivcommons.util.endecode;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 统一的base64编解码工具类
 * Created by xugang on 2017/7/30.
 */
public class Base64Util {
    /**
     * 将字节数组base64编码为字符串
     * @param bytes 字节数组
     * @return base64编码的字符串
     */
    public static String encodeToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes) ;
    }

    /**
     * 将base64编码的字符串以UTF8编码字符集解码为原字节数组
     * @param base64Str base64编码的字符串
     * @return 原字节数组
     */
    public static byte[] decode(String base64Str) {
        return Base64.getDecoder().decode(base64Str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将base64编码的字符串以制定编码字符集解码为原字节数组
     * @param base64Str base64编码的字符串
     * @param charset   制定的编码字符集
     * @return 原字节数组
     */
    public static byte[] decode(String base64Str, Charset charset) {
        return Base64.getDecoder().decode(base64Str.getBytes(charset));
    }
}
