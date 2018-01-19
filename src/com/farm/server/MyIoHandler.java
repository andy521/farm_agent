package com.farm.server;

import com.farm.protocol.MyAdditionalData;
import com.farm.protocol.impl.FarmMessage;
import com.farm.protocol.impl.PayLoad_00;
import com.farm.protocol.impl.PayLoad_01;
import com.farm.protocol.impl.PayLoad_05;
import com.farm.redis.JedisConnectionPool;
import com.farm.redis.Publisher;
import com.farm.util.ConfigUtil;
import com.farm.util.IoSessionUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

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
        // super.exceptionCaught(session, cause);
        LOGGER.error(cause.getMessage());
        session.close(true);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        FarmMessage farmMessage = (FarmMessage) message;
        byte type = farmMessage.getHeadMessage().getMessageType();

//        // 在发送注册包前,不对其他包进行回应,也不做任何业务处理
//        if (session.getAttribute("localMacAddress") == null) {
////            if (type == 0x00) {
////                registerHandler(session, message);
////            }
////            throw new IllegalStateException("connect without token : " + IoSessionUtil.getAddressPort(session));
////        }
        switch (type) {
            case 0x00:
                registerHandler(session, message);
                break;
            case 0x05:
                temperatureHandler(session, message);
                break;
            default:
                break;
        }
    }

    // 注册包
    private void registerHandler(IoSession session, Object message) {
        FarmMessage farmMessage = (FarmMessage) message;
        PayLoad_00 registerData = (PayLoad_00) farmMessage.getMessageBody();
        // todo 注册认证
        session.setAttribute("localMacAddress", registerData.getMacAddress());
        session.setAttribute("model", registerData.getModel());
        Publisher.sendStatus(farmMessage.toString());
        LOGGER.info("Message received: {}", farmMessage);
    }

    // 温度包
    private void temperatureHandler(IoSession session, Object message) {
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
