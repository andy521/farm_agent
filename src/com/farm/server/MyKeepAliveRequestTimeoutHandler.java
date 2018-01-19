package com.farm.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description: 心跳包超时以后的处理
 */
public class MyKeepAliveRequestTimeoutHandler implements KeepAliveRequestTimeoutHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyKeepAliveRequestTimeoutHandler.class);

    @Override
    public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
        String macAddress;
        if (session.getAttribute("macAddress") != null) {
            macAddress = (String) session.getAttribute("macAddress");
        }
        LOGGER.error("macAddress:  心跳包超时, 关闭连接.");
        session.close(true);
    }
}
