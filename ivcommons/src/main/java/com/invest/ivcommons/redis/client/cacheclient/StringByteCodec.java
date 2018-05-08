package com.invest.ivcommons.redis.client.cacheclient;

import com.lambdaworks.redis.codec.RedisCodec;
import com.lambdaworks.redis.protocol.LettuceCharsets;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * Created by yanjie on 2016/5/5.
 *
 */
//ok-xyj
public class StringByteCodec implements RedisCodec<String,byte[]> {
    private Charset charset= LettuceCharsets.UTF8;
    private static final byte[] EMPTY = new byte[0];

    @Override
    public String decodeKey(ByteBuffer byteBuffer) {
        return charset.decode(byteBuffer).toString();
    }

    @Override
    public ByteBuffer encodeKey(String s) {
        return s == null ? ByteBuffer.wrap(EMPTY) : this.charset.encode(s);
    }

    @Override
    public byte[] decodeValue(ByteBuffer byteBuffer) {
        return bufferToByte(byteBuffer);
    }

    @Override
    public ByteBuffer encodeValue(byte[] bytes) {
        return ByteBuffer.wrap(bytes);
    }

    private static byte[] bufferToByte(ByteBuffer buffer) {
        byte[] b = new byte[buffer.remaining()];
        buffer.get(b);
        return b;
    }
}
