package com.farm.server;

import com.farm.protocol.impl.FarmMessage;
import com.farm.protocol.impl.HeadMessage;
import com.farm.protocol.impl.PayLoad_01;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description: 心跳包判断
 */
public class MyKeepAliveMessageFactory implements KeepAliveMessageFactory {

    @Override
    public boolean isRequest(IoSession session, Object message) {
        return isKeepAlive(message);
    }

    @Override
    public boolean isResponse(IoSession session, Object message) {
        return isKeepAlive(message);
    }

    @Override
    public Object getRequest(IoSession session) {
        HeadMessage headMessage = new HeadMessage();
        headMessage.setMessageHead(HeadMessage.MAGIC);
        headMessage.setSequence(1);
        headMessage.setMessageType((byte) 0x01);

        PayLoad_01 payLoad_01 = new PayLoad_01();
        FarmMessage farmMessage = new FarmMessage();
        farmMessage.setHeadMessage(headMessage);
        farmMessage.setMessageBody(payLoad_01);
        return farmMessage;
    }

    @Override
    public Object getResponse(IoSession session, Object request) {
        HeadMessage headMessage = new HeadMessage();
        headMessage.setMessageHead(HeadMessage.MAGIC);
        headMessage.setSequence(1);
        headMessage.setMessageType((byte) 0x01);

        PayLoad_01 payLoad_01 = new PayLoad_01();
        FarmMessage farmMessage = new FarmMessage();
        farmMessage.setHeadMessage(headMessage);
        farmMessage.setMessageBody(payLoad_01);
        return farmMessage;
    }


    // 心跳包判断
    private boolean isKeepAlive(Object message) {
        FarmMessage farmMessage = (FarmMessage) message;
        if (farmMessage.getMessageBody() instanceof PayLoad_01) {
            return true;
        }
        return false;
    }
}
