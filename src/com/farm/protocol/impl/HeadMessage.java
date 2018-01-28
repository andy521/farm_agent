package com.farm.protocol.impl;

import com.farm.protocol.IMessage;
import com.farm.protocol.MyBuffer;
import com.farm.util.ByteUtil;
import com.farm.util.Crc16Util;

import java.util.Arrays;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 对数据包的头部信息(公共部分)解析
 */
public class HeadMessage implements IMessage {
    // 包头
    public static final byte MAGIC = (byte) 0xe0;
    // 包头长度
    public static final int LENGTH = 1 + 4 + 1 + 4 + 2;
    // 头部
    private byte messageHead;
    // 流水号
    private int sequence;
    // 包类型
    private byte messageType;
    // 消息长度
    private int messageLength;
    // crc16
    private short crc16;

    @Override
    public byte[] WriteToBytes() {
        MyBuffer buff = new MyBuffer();
        buff.putByte(messageHead);
        buff.putInt(sequence);
        buff.putByte(messageType);
        buff.putInt(messageLength);
        return buff.array();
    }

    @Override
    public void ReadFromBytes(byte[] messageBytes) {
        MyBuffer buff = new MyBuffer(messageBytes);

        try {
            messageHead = buff.get();

            sequence = buff.getInt();

            messageType = buff.get();

            messageLength = buff.getInt();

            byte[] crc = Arrays.copyOfRange(messageBytes, messageBytes.length - 2, messageBytes.length);

            crc16 = ByteUtil.bytesToShort(crc);

        } catch (Exception e) {
            throw new IllegalStateException("head length too short");
        }

        // 判断头部是否正确
        if (messageHead != MAGIC) {
            throw new IllegalStateException("bad magic");
        }

        // 判断消息体长度 ( 总长 - 包头长度 - 最后两位的crc位)
        if (messageLength != (messageBytes.length - LENGTH)) {
            throw new IllegalStateException("bad payLoad length");
        }
    }

    public byte getMessageType() {
        return messageType;
    }

    public byte getMessageHead() {
        return messageHead;
    }

    public int getSequence() {
        return sequence;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public HeadMessage setMessageHead(byte messageHead) {
        this.messageHead = messageHead;
        return this;
    }

    public HeadMessage setSequence(int sequence) {
        this.sequence = sequence;
        return this;
    }

    public HeadMessage setMessageType(byte messageType) {
        this.messageType = messageType;
        return this;
    }

    public HeadMessage setMessageLength(int messageLength) {
        this.messageLength = messageLength;
        return this;
    }

    public short getCrc16() {
        return crc16;
    }

    public HeadMessage setCrc16(short crc16) {
        this.crc16 = crc16;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("\"messageHead\":")
                .append(ByteUtil.bytesToHexString(messageHead));
        sb.append(",\"sequence\":")
                .append(sequence);
        sb.append(",\"messageType\":")
                .append(ByteUtil.bytesToHexString(messageType));
        sb.append(",\"messageLength\":")
                .append(messageLength);
        sb.append(",\"crc16\":")
                .append(crc16);
        return sb.toString();
    }
}
