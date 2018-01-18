package com.farm.test;

import com.farm.protocol.impl.HeadMessage;
import com.farm.util.ByteUtil;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description:
 */
public class TestHead {
    public static void main(String[] args) {
        HeadMessage headMessage = new HeadMessage();
        headMessage.setMessageHead(HeadMessage.MAGIC);
        headMessage.setSequence(1);
        headMessage.setMessageType((byte) 0x00);
        headMessage.setMessageLength(1);
        byte[] bytes = headMessage.WriteToBytes();
        String hexStr = ByteUtil.bytesToHexString(bytes);
        System.out.println(hexStr);
    }
}
