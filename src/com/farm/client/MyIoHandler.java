package com.farm.client;

import com.farm.protocol.impl.*;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.farm.util.IoSessionUtil.MINA_HEAD_COUNT_MAX;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description:
 */
public class MyIoHandler extends IoHandlerAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(com.farm.server.MyIoHandler.class);

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        session.setAttribute(MINA_HEAD_COUNT_MAX, 0);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        //  注册
        Thread.sleep(1000);
        sendRegister(session);
        sendTemperatureMessage(session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
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

    /**
     * 测试温度包
     *
     * @return
     */
    public static void sendTemperatureMessage(IoSession session) {
        HeadMessage headMessage = new HeadMessage();
        headMessage.setMessageHead(HeadMessage.MAGIC);
        headMessage.setSequence(1);
        headMessage.setMessageType((byte) 0x05);

        PayLoad_05 payLoad_05 = new PayLoad_05();
        payLoad_05.setTemperature0((byte) 02);
        payLoad_05.setTemperature1((byte) 05);

        FarmMessage farmMessage = new FarmMessage();
        farmMessage.setHeadMessage(headMessage);
        farmMessage.setMessageBody(payLoad_05);
        session.write(farmMessage);
    }
}
