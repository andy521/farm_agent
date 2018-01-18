package com.farm.client;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;


/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description: 心跳包过滤
 */
public class MyKeepAliveFilter extends KeepAliveFilter {
    private static final int TIMEOUT = 10;

    public MyKeepAliveFilter(KeepAliveMessageFactory messageFactory) {
        super(messageFactory, IdleStatus.BOTH_IDLE, new MyKeepAliveRequestTimeoutHandler(), TIMEOUT, TIMEOUT);
        //此消息不会继续传递，不会被业务层看见
        this.setForwardEvent(false);
    }
}
