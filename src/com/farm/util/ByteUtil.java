package com.farm.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author: taoroot
 * @date: 2017/10/31
 * @description: 字节工具类
 */
public class ByteUtil {

    /**
     * 字节数组 转 16进制字符串
     * @param bytes 待转换字节数组
     * @return 16进制字符串 (返回的字母都是大写)
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }


    /**
     * 字节 转 16进制字符串
     * @param b 待转换字节
     * @return 16进制字符串 (返回的字母都是大写)
     */
    public static String bytesToHexString(byte b) {
        StringBuilder stringBuilder = new StringBuilder("");
        int v = b & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * byte[] to short
     * @param b 待转换字节数组
     * @return short值
     */
    public static short bytesToShort(byte[] b) {
        short b1 = (short) (b[0] & 0xff);
        short b2 = (short) (b[1] & 0xff);
        return (short) (b1 | (b2 << 8));
    }
}
