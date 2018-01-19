package com.farm.protocol.impl;

import com.farm.protocol.IMessage;
import com.farm.protocol.MyBuffer;

import java.util.Arrays;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 注册包
 */
public class PayLoad_00 implements IMessage {
    private String type = "register";
    // 设备型号
    private byte model;
    // 设备Mac地址
    private String macAddress;

    @Override
    public byte[] WriteToBytes() {
        MyBuffer buffer = new MyBuffer();
        buffer.putByte(model);
        buffer.putString(macAddress);
        return buffer.array();
    }

    @Override
    public void ReadFromBytes(byte[] messageBytes) {
        
        // 获取型号
        model = messageBytes[0];

        // 获取mac地址
        messageBytes = Arrays.copyOfRange(messageBytes, 1, messageBytes.length);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageBytes.length; i++) {
            stringBuilder.append((char) messageBytes[i]);
        }
        macAddress = stringBuilder.toString();
    }

    public String getMacAddress() {
        return macAddress;
    }

    public PayLoad_00 setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public byte getModel() {
        return model;
    }

    public PayLoad_00 setModel(byte model) {
        this.model = model;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"model\":")
                .append(model);
        sb.append(",\"macAddress\":\"")
                .append(macAddress).append('\"');
        return sb.toString();
    }
}
