package com.farm.protocol.impl;

import com.farm.protocol.IMessage;
import com.farm.protocol.MessageBodyFactory;
import com.farm.protocol.MyBuffer;
import com.farm.util.Crc16Util;
import org.apache.mina.core.buffer.IoBuffer;

import java.util.Arrays;

import static com.farm.util.ByteUtil.bytesToHexString;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 将字节数组转为对象, 或将对象转为字节数组
 */

public class FarmMessage implements IMessage {
    // 原始数据包
    private String packetDescr;
    // 消息头部(公共部分)
    private HeadMessage headMessage;
    // 消息内容(数据部分)
    private IMessage messageBody;

    @Override
    public byte[] WriteToBytes() {
        byte[] arrayBody = messageBody.WriteToBytes();
        // 计算 CRC
        headMessage.setCrc16(Crc16Util.crc16(arrayBody));
        headMessage.setMessageLength(arrayBody.length);

        // 拼接数据
        MyBuffer buffer = new MyBuffer();
        buffer.putArrayNoChange(headMessage.WriteToBytes());
        buffer.putArrayNoChange(arrayBody);
        buffer.putShort(headMessage.getCrc16());
        // 转义
        return Escape(buffer.array());
    }

    @Override
    public void ReadFromBytes(byte[] messageBytes) {
        packetDescr = bytesToHexString(messageBytes);

        // 解析头部
        headMessage = new HeadMessage();
        headMessage.ReadFromBytes(messageBytes);

        // 截取消息体
        messageBytes = Arrays.copyOfRange(messageBytes, HeadMessage.LENGTH - 2, messageBytes.length - 2);

        // CRC 16 校验
        if (headMessage.getCrc16() != Crc16Util.crc16(messageBytes)) {
            throw new IllegalStateException("bad crc");
        }

        // 解析消息体
        messageBody = MessageBodyFactory.Create(headMessage.getMessageType(), messageBytes);
    }


    /**
     * 转义
     *
     * @param data
     * @return
     */
    private byte[] Escape(byte[] data) {
        IoBuffer buffer = IoBuffer.allocate(data.length * 2);
        buffer.setAutoExpand(true);

        for (int j = 0; j < data.length; j++) {
            if (data[j] == 0x23) {          // '#' ==> '\#'
                buffer.put((byte) 0x5C);
                buffer.put((byte) 0x23);
            } else if (data[j] == 0x5C) {   // '\' ==> '\\'
                buffer.put((byte) 0x5C);
                buffer.put((byte) 0x5C);
            } else if (data[j] == 0x2A) {   // '*' ==> '\*'
                buffer.put((byte) 0x5C);
                buffer.put((byte) 0x2A);
            } else {
                buffer.put(data[j]);
            }
        }

        // 去除自动扩展的空间
        byte[] tmp = new byte[buffer.position()];
        System.arraycopy(buffer.array(), 0, tmp, 0, buffer.position());
        return tmp;
    }


    /**
     * 还原转义
     *
     * @param data
     * @return
     */
    private byte[] UnEscape(byte[] data) {

        IoBuffer buff = IoBuffer.allocate(data.length);
        buff.setAutoExpand(true);

        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0x5C) {              // '\7e' ==> '7e'
                if (data[i + 1] == 0x23) {
                    buff.put((byte) 0x23);
                    i++;
                } else if (data[i + 1] == 0x5C) { // '\\' ==> '\'
                    buff.put((byte) 0x5C);
                    i++;
                }
            } else {
                buff.put(data[i]);
            }
        }

        byte[] a = buff.array();

        return a;
    }

    public String getPacketDescr() {
        return packetDescr;
    }

    public HeadMessage getHeadMessage() {
        return headMessage;
    }

    public IMessage getMessageBody() {
        return messageBody;
    }

    public FarmMessage setHeadMessage(HeadMessage headMessage) {
        this.headMessage = headMessage;
        return this;
    }

    public FarmMessage setMessageBody(IMessage messageBody) {
        this.messageBody = messageBody;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"packetDescr\":\"")
                .append(packetDescr).append('\"');
        sb.append(",")
                .append(headMessage);
        sb.append(",")
                .append(messageBody);
        sb.append("}");
        return sb.toString();
    }
}
