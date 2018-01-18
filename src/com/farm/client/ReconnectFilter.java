package com.farm.client;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.LoggerFactory;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description: 重连过滤器
 */
public class ReconnectFilter extends IoFilterAdapter {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ReconnectFilter.class);

    // MINA socket 连接工具
    private  NioSocketConnector connector;

    public ReconnectFilter(NioSocketConnector connector) {
        this.connector = connector;
    }

    @Override
    public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws Exception {
        for (; ;) {
            try {
                Thread.sleep(CmdOptionHandler.getReconnect() * 1000);
                ConnectFuture future = connector.connect();
                future.awaitUninterruptibly();// 等待连接创建成功
                IoSession session = future.getSession();// 获取会话
                if (session.isConnected()) {
                    LOGGER.info("断线重连[" + connector.getDefaultRemoteAddress().getHostName() + ":" + connector.getDefaultRemoteAddress().getPort() + "]成功");
                    break;
                }
            } catch (Exception ex) {
                LOGGER.info("重连服务器 " + connector.getDefaultRemoteAddress().getHostName() + ":" + connector.getDefaultRemoteAddress().getPort() + " 登录失败,3秒再连接一次");
            }
        }
    }
}
