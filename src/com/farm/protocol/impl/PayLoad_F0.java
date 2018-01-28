package com.farm.protocol.impl;

import com.farm.protocol.IMessage;
import com.farm.protocol.MyBuffer;
import com.farm.util.MD5Util;


/**
 * @author: taoroot
 * @date: 2018/1/28
 * @description: 注册包, 回应包
 */
public class PayLoad_F0 implements IMessage {
    public static final byte success =  0;
    public static final byte fail =  1;
    // 注册结果
    private byte status = 0;
    // 16位token
    private String token = "";

    public PayLoad_F0() {
    }

    public PayLoad_F0(byte status, String id) {
        this.status = status;
        if (id != null) {
            this.token = getToken(id);
        }
    }

    @Override
    public byte[] WriteToBytes() {
        MyBuffer myBuffer = new MyBuffer();
        myBuffer.putByte(status);
        myBuffer.putString(token);
        return myBuffer.array();
    }

    @Override
    public void ReadFromBytes(byte[] messageBytes) {
        MyBuffer myBuffer = new MyBuffer(messageBytes);
        status = myBuffer.get();
        token = myBuffer.getString(16);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("\"status\":")
                .append(status);
        sb.append(",\"token\":\"")
                .append(token).append('\"');
        return sb.toString();
    }

    /**
     * 获取token
     *
     * @param id
     * @return
     */
    private String getToken(String id) {
        return MD5Util.md5(id);
    }
}
