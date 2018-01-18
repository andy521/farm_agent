package com.farm.server;

import com.farm.protocol.impl.FarmMessage;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
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
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        LOGGER.error(cause.getMessage());
        session.close(true);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        FarmMessage farmMessage = (FarmMessage) message;
        LOGGER.info("Message received: {}", farmMessage);
        switch (farmMessage.getHeadMessage().getMessageType()) {
            case 0x00:
                registerHandler(session);
                break;
            default:
                break;
        }
    }

    // 注册包
    private void registerHandler(IoSession session) {
        // todo 注册认证
        LOGGER.info("注册认证包");
    }


}
