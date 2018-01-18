package com.farm.client;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description: 心跳包超时处理
 *
 */
public class MyKeepAliveRequestTimeoutHandler implements KeepAliveRequestTimeoutHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyKeepAliveRequestTimeoutHandler.class);
    @Override
    public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
        // 设备心跳包超时在线,关闭session
        LOGGER.info("session:  未在规定时间内是都心跳包, 关闭连接");
        session.close(true);
    }
}
