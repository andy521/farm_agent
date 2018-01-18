package com.farm.protocol;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * @author: taoroot
 * @date: 2017/11/23
 * @description: 1. 封装 IoBuffer
 * 2. 多字节, 小端存储
 */
public class MyBuffer {
    IoBuffer buff;

    public MyBuffer() {
        buff = IoBuffer.allocate(2048);
        buff.mark();
    }

    public MyBuffer(byte[] bytes) {
        buff = IoBuffer.allocate(2048);
        buff.mark();
        buff.put(bytes);
        buff.limit(bytes.length);
        buff.reset();
    }

    // ============= putByte method ===============
    public void putByte(byte a) {
        buff.put(a);
    }

    public void putShort(short a) {
        buff.put((byte) (a & 0xff));
        buff.put((byte) ((a >>> 8) & 0x00ff));
    }

    public void putArray(byte[] a) {
        for (int i = a.length - 1; i >= 0; i--) {
            buff.put(a[i]);
        }
    }

    // 正常写入
    public void putArrayNoChange(byte[] a) {
        for (int i = 0; i < a.length; i++) {
            buff.put(a[i]);
        }
    }

    public void putInt(int a) {
        for (int i = 0; i < 4; i++) {
            buff.put((byte) (a >>> (i * 8)));
        }
    }

    public void putFloat(float a) {
        // IEEE 754
        int fbit = Float.floatToIntBits(a);
        for (int i = 0; i < 4; i++) {
            buff.put((byte) (fbit >>> (i * 8)));
        }
    }

    public void putString(String a) {
        StringBuffer buf = new StringBuffer(a);
        byte[] bytes = buf.reverse().toString().getBytes();
        putArray(bytes);
    }

    // ============= get method ===============
    public byte get() {
        return buff.get();
    }

    public byte[] gets(int n) {
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++) {
            bytes[i] = get();
        }
        return bytes;
    }

    public short getShort() {
        short b1 = (short) (get() & 0xff);
        short b2 = (short) (get() & 0xff);
        return (short) (b1 | (b2 << 8));
    }

    public int getInt() {
        int b1 = get() & 0xff;
        int b2 = get() & 0xff;
        int b3 = get() & 0xff;
        int b4 = get() & 0xff;
        return (b1 | (b2 << 8) | (b3 << 16) | (b4 << 24));
    }

    public float getFloat() {
        int accum = 0;
        accum = accum | (get() & 0xff);
        accum = accum | (get() & 0xff) << 8;
        accum = accum | (get() & 0xff) << 16;
        accum = accum | (get() & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }

    public String getString(int len) {
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < len; i++) {
            buf.append((char)get());
        }
        return buf.reverse().toString();
    }

    /**
     * 得到所有字节数组
     *
     * @return
     */
    public byte[] array() {
        int pos = buff.position();
        byte[] data = new byte[pos];
        buff.reset();
        buff.get(data);
        return data;
    }


}
