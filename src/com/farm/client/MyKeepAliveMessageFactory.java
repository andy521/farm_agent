package com.farm.client;

import com.farm.protocol.impl.FarmMessage;
import com.farm.protocol.impl.HeadMessage;
import com.farm.protocol.impl.PayLoad_01;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import static com.farm.util.IoSessionUtil.MINA_HEAD_COUNT_MAX;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description: 心跳包判断
 * 双向心跳包判断, 由客户端发起, 服务器响应, 客户端未及时上报, 服务端将主动断开
 */
public class MyKeepAliveMessageFactory implements KeepAliveMessageFactory {

    @Override
    public boolean isRequest(IoSession session, Object message) {
        return false;
    }

    @Override
    public boolean isResponse(IoSession session, Object message) {
        return isKeepAlive(message);
    }

    @Override
    public Object getRequest(IoSession session) {
        heartCountAdd(session);
        return getFarmMessage();
    }

    @Override
    public Object getResponse(IoSession session, Object request) {
        return null;
    }

    // 心跳包判断
    private boolean isKeepAlive(Object message) {
        FarmMessage farmMessage = (FarmMessage) message;
        if (farmMessage.getMessageBody() instanceof PayLoad_01) {
            return true;
        }
        return false;
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

    /**
     * 心跳包 计数器
     *
     * @param session
     */
    private void heartCountAdd(IoSession session) {
        int heartCounter = (int) session.getAttribute(MINA_HEAD_COUNT_MAX) + 1;
        session.setAttribute(MINA_HEAD_COUNT_MAX, heartCounter);

        if (heartCounter > CmdOptionHandler.getHeadCountMax()) {
            session.setAttribute(MINA_HEAD_COUNT_MAX, 0);
            heartCounterHandler(session);
        }
    }

    // 定时查询任务
    private void heartCounterHandler(IoSession session) {
        // 发送温度
        MyIoHandler.sendTemperatureMessage(session);
    }

}
