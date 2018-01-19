package com.farm.protocol.impl;

import com.farm.protocol.IMessage;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 心跳包/空包
 */
public class PayLoad_01 implements IMessage {
    private String type = "keepAlive";

    @Override
    public byte[] WriteToBytes() {
        return new byte[0];
    }

    @Override
    public void ReadFromBytes(byte[] messageBytes) {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("\"type\":\"")
                .append(type).append('\"');
        return sb.toString();
    }


}
