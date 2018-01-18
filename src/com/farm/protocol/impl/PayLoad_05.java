package com.farm.protocol.impl;

import com.farm.protocol.IMessage;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 温度包
 */
public class PayLoad_05 implements IMessage {
    private String type = "温度包";

    // 整数部分
    private byte temperature0;
    // 小数部分
    private byte temperature1;

    @Override
    public byte[] WriteToBytes() {
        return new byte[]{temperature1, temperature0};
    }

    @Override
    public void ReadFromBytes(byte[] messageBytes) {
        temperature0 = messageBytes[1];
        temperature1 = messageBytes[0];
    }


    public byte getTemperature0() {
        return temperature0;
    }

    public PayLoad_05 setTemperature0(byte temperature0) {
        this.temperature0 = temperature0;
        return this;
    }

    public byte getTemperature1() {
        return temperature1;
    }

    public PayLoad_05 setTemperature1(byte temperature1) {
        this.temperature1 = temperature1;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"temperature0\":")
                .append(temperature0);
        sb.append(",\"temperature1\":")
                .append(temperature1);
        return sb.toString();
    }
}
