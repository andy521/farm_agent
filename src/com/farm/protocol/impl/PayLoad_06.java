package com.farm.protocol.impl;

import com.farm.protocol.IMessage;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 温度包
 */
public class PayLoad_06 implements IMessage {
    private String type = "湿度包";

    // 整数部分
    private byte humidity0;
    // 小数部分
    private byte humidity1;

    @Override
    public byte[] WriteToBytes() {
        return new byte[]{humidity1, humidity0};
    }

    @Override
    public void ReadFromBytes(byte[] messageBytes) {
        humidity0 = messageBytes[1];
        humidity1 = messageBytes[0];
    }

    public String getType() {
        return type;
    }

    public PayLoad_06 setType(String type) {
        this.type = type;
        return this;
    }

    public byte getHumidity0() {
        return humidity0;
    }

    public PayLoad_06 setHumidity0(byte humidity0) {
        this.humidity0 = humidity0;
        return this;
    }

    public byte getHumidity1() {
        return humidity1;
    }

    public PayLoad_06 setHumidity1(byte humidity1) {
        this.humidity1 = humidity1;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"humidity\":")
                .append(humidity0);
        sb.append(".")
                .append(humidity1);
        return sb.toString();
    }
}
