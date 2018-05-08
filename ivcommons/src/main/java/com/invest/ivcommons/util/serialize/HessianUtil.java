package com.invest.ivcommons.util.serialize;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Hessian2序列化实现
 * Created by xugang on 2017/7/29.
 */
public final class HessianUtil {
    /**
     * 将对象序列化为字节数组（此方法自带压缩功能）
     *
     * @param obj 待序列化的对象实例
     * @return 序列化之后的字节数组
     */
    public static byte[] toBytes(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOs1 = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOs1);
        ByteArrayOutputStream byteArrayOs2 = new ByteArrayOutputStream();
        DeflaterOutputStream deflateOs = new DeflaterOutputStream(byteArrayOs2);
        boolean hessianIsClose = false;
        try {
            hessian2Output.writeObject(obj); // Output to byteArrayOs1
            hessian2Output.close();
            hessianIsClose = true;
            deflateOs.write(byteArrayOs1.toByteArray());
            deflateOs.close();
            return byteArrayOs2.toByteArray();
        } finally {
            if (!hessianIsClose) {
                hessian2Output.close();
            }
            deflateOs.close();
        }
    }

    /**
     * 将字节数组反序列化为对象实例
     *
     * @param value 通过{@linkplain HessianUtils#toBytes(java.lang.Object)}序列化后的字节数组
     * @param <T>   对象所在的类
     * @return 具体对应的对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromBytes(byte[] value) throws IOException {
        InflaterInputStream inflaterIs = new InflaterInputStream(new ByteArrayInputStream(value));
        Hessian2Input hessian2Input = new Hessian2Input(inflaterIs);
        try {
            return (T) hessian2Input.readObject();
        } finally {
            hessian2Input.close();
            inflaterIs.close();
        }
    }
}
