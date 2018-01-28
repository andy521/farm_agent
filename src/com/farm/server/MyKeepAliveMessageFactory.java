package com.farm.server;

import com.farm.protocol.impl.FarmMessage;
import com.farm.protocol.impl.HeadMessage;
import com.farm.protocol.impl.PayLoad_01;
import com.farm.redis.JedisConnectionPool;
import com.farm.redis.Publisher;
import com.farm.util.ConfigUtil;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description: 心跳包判断
 */
public class MyKeepAliveMessageFactory implements KeepAliveMessageFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyKeepAliveMessageFactory.class);
    private final static String FARM_STATUS_TOP = ConfigUtil.get("RedisFarmStatusTopic");

    @Override
    public boolean isRequest(IoSession session, Object message) {
        return false;
    }

    @Override
    public boolean isResponse(IoSession session, Object message) {
        if (isKeepAlive(message)) {
            return true;
        }
        return false;
    }

    @Override
    public Object getRequest(IoSession session) {
        return getFarmMessage();
    }

    @Override
    public Object getResponse(IoSession session, Object request) {
        return null;
    }

    // 响应心跳包判断
    private boolean isKeepAlive(Object message) {
        FarmMessage farmMessage = (FarmMessage) message;
        if (farmMessage.getMessageBody() instanceof PayLoad_01) {
            return true;
        }
        return false;
    }


    // 心跳包
    private void keepAliveHandler(Object message) {
        FarmMessage farmMessage = (FarmMessage) message;
        if (farmMessage != null) {
            Publisher.sendStatus(farmMessage.toString());
        }
    }

    /**
     * 获取一个心跳包
     *
     * @return
     */
    private FarmMessage getFarmMessage() {
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


}
