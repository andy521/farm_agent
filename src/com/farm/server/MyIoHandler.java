package com.farm.server;

import com.farm.protocol.MyAdditionalData;
import com.farm.protocol.impl.FarmMessage;
import com.farm.protocol.impl.HeadMessage;
import com.farm.protocol.impl.PayLoad_00;
import com.farm.protocol.impl.PayLoad_F0;
import com.farm.redis.Publisher;
import com.farm.util.IoSessionUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description:
 */


public class MyIoHandler extends IoHandlerAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyIoHandler.class);

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        session.setAttribute("MyAdditionalData", new MyAdditionalData());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage());
//        session.close(true);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        FarmMessage farmMessage = (FarmMessage) message;
        byte type = farmMessage.getHeadMessage().getMessageType();

        // 在发送注册包前,不对其他包进行回应,也不做任何业务处理
        if (session.getAttribute("localMacAddress") == null) {
            if (type == 0x00) {
                registerHandler(session, message);
            } else {
                throw new IllegalStateException("connect without token : " + IoSessionUtil.getAddressPort(session));
            }
        }
        switch (type) {
            case 0x00:
                registerHandler(session, message);
                break;
            case 0x05:
                temperatureHandler(session, message);
                break;
            case 0x06:
                humidityHandler(session, message);
                break;
            default:
                break;
        }
    }


    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        FarmMessage farmMessage = (FarmMessage) message;
        farmMessage.WriteToBytes();
//        LOGGER.info("messageSent: {}", farmMessage.toString());

    }

    // 注册包
    private void registerHandler(IoSession session, Object message) {
        FarmMessage farmMessage = (FarmMessage) message;
        PayLoad_00 registerData = (PayLoad_00) farmMessage.getMessageBody();
        // 保存信息到session
        session.setAttribute("localMacAddress", registerData.getMacAddress());
        session.setAttribute("model", registerData.getModel());
        Publisher.sendStatus(farmMessage.toString());
        LOGGER.info("Message received: {}", farmMessage);


        // todo 注册认证
        HeadMessage headMessage = new HeadMessage();
        headMessage.setMessageHead(HeadMessage.MAGIC);
        headMessage.setSequence(1);
        headMessage.setMessageType((byte) 0xF0);

        PayLoad_F0 resRegister = new PayLoad_F0(PayLoad_F0.success, registerData.getMacAddress());

        FarmMessage resMessage = new FarmMessage();
        resMessage.setHeadMessage(headMessage);
        resMessage.setMessageBody(resRegister);

        session.write(resMessage);
    }

    // 温度包
    private void temperatureHandler(IoSession session, Object message) {
        FarmMessage farmMessage = (FarmMessage) message;
        farmMessage.setObjectData(getDataBySession(session));
        Publisher.sendStatus(farmMessage.toString());
        LOGGER.info("Message received: {}", farmMessage);
    }

    // 温度包
    private void humidityHandler(IoSession session, Object message) {
        FarmMessage farmMessage = (FarmMessage) message;
        farmMessage.setObjectData(getDataBySession(session));
        Publisher.sendStatus(farmMessage.toString());
        LOGGER.info("Message received: {}", farmMessage);
    }


    // 从session获取设备信息
    private MyAdditionalData getDataBySession(IoSession session) throws IllegalStateException {
        MyAdditionalData myAdditionalData = new MyAdditionalData();
        // 查看有没有通过认证
        if (session.getAttribute("localMacAddress") == null
                || session.getAttribute("model") == null) {
            throw new IllegalStateException("connect without token : " + IoSessionUtil.getAddressPort(session));
        }
        myAdditionalData.setMacAddress((String) session.getAttribute("localMacAddress"));
        return myAdditionalData;
    }
}
