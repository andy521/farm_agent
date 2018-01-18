package com.farm.client;

import com.farm.protocol.impl.FarmMessage;
import com.farm.protocol.impl.HeadMessage;
import com.farm.protocol.impl.PayLoad_00;
import com.farm.protocol.impl.PayLoad_01;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description:
 */
public class MyIoHandler extends IoHandlerAdapter {
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        //  注册
        Thread.sleep(1000);
        sendRegister(session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

    }


    // 发送注册
    private void sendRegister(IoSession session) {
        HeadMessage headMessage = new HeadMessage();
        headMessage.setMessageHead(HeadMessage.MAGIC);
        headMessage.setSequence(1);
        headMessage.setMessageType((byte) 0x00);
        // 消息体
        PayLoad_00 payLoad_00 = new PayLoad_00();
        payLoad_00.setMacAddress("ac:bc:32:ba:62:03");
        payLoad_00.setModel((byte) 0);
        // 整合
        FarmMessage farmMessage = new FarmMessage();
        farmMessage.setHeadMessage(headMessage);
        farmMessage.setMessageBody(payLoad_00);
        session.write(farmMessage);
    }

    // 发送心跳包
    private void sendHeartBeat(IoSession session) {
        HeadMessage headMessage = new HeadMessage();
        headMessage.setMessageHead(HeadMessage.MAGIC);
        headMessage.setSequence(1);
        headMessage.setMessageType((byte) 0x01);
        // 消息体
        PayLoad_01 payLoad_01 = new PayLoad_01();
        // 整合
        FarmMessage farmMessage = new FarmMessage();
        farmMessage.setHeadMessage(headMessage);
        farmMessage.setMessageBody(payLoad_01);
        session.write(farmMessage);
    }
}
