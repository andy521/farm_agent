package com.farm.test;

import com.farm.protocol.impl.FarmMessage;
import com.farm.protocol.impl.HeadMessage;
import com.farm.protocol.impl.PayLoad_00;
import com.farm.util.ByteUtil;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description:
 */
public class Test00 {
    public static void main(String[] args) {
        byte[] bytes;
        String hexStr;
        // 头
        HeadMessage headMessage = new HeadMessage();
        headMessage.setMessageHead(HeadMessage.MAGIC);
        headMessage.setSequence(1);
        headMessage.setMessageType((byte) 0x00);
        bytes = headMessage.WriteToBytes();
        hexStr = ByteUtil.bytesToHexString(bytes);
        System.out.println("headMessage: " + hexStr);
        // 消息体
        PayLoad_00 payLoad_00 = new PayLoad_00();

        // 整合
        FarmMessage farmMessage = new FarmMessage();
        farmMessage.setHeadMessage(headMessage);
        farmMessage.setMessageBody(payLoad_00);

        bytes = farmMessage.WriteToBytes();
        hexStr = ByteUtil.bytesToHexString(bytes);
        System.out.println(hexStr);

    }
}
