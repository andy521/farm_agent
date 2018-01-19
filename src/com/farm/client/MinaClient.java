package com.farm.client;

import com.farm.server.MyProtocolCodecFactory;
import com.farm.util.ConfigUtil;
import org.apache.commons.cli.*;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description:
 */
public class MinaClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(MinaClient.class);

    // MINA socket 连接工具
    private static NioSocketConnector connector;

    public MinaClient() {
        // 创建连接客户端
        connector = new NioSocketConnector();
        // 设置连接超时
        connector.setConnectTimeoutMillis(3000);
        // 过滤器 - 重连
        connector.getFilterChain().addFirst("reconnect", new ReconnectFilter(connector));
        // 过滤器 - io日志
        connector.getFilterChain().addLast("logging", new LoggingFilter());
        // 过滤器 - 协议解析
        connector.getFilterChain().addLast("coder", new ProtocolCodecFilter(new MyProtocolCodecFactory()));
        // 过滤器 - 心跳包
        KeepAliveFilter keepAliveFilter = new MyKeepAliveFilter(new MyKeepAliveMessageFactory());
        // 设置心跳频率
        keepAliveFilter.setRequestInterval(CmdOptionHandler.getInterval());
        connector.getFilterChain().addLast("keepAlive", keepAliveFilter);
        // 设置业务处理器
        connector.setHandler(new MyIoHandler());
        // 设置默认访问地址
        connector.setDefaultRemoteAddress(new InetSocketAddress(CmdOptionHandler.getHost(), CmdOptionHandler.getPort()));
    }

    public static void main(String[] args) throws Exception {
        // 获取参数
        CmdOptionHandler.CmdOptionHandler(args);
        // 初始化mina客户端
        new MinaClient();
        // 连接服务器
        connectHandler();
    }

    // 服务器连接
    private static void connectHandler() throws Exception {
        for (; ; ) {
            try {
                ConnectFuture future = connector.connect();
                // 等待连接创建成功
                future.awaitUninterruptibly();
                // 获取会话
                IoSession session = future.getSession();
                LOGGER.info("连接服务端 " + CmdOptionHandler.getHost() + ":" + CmdOptionHandler.getPort() + " [成功]");
                break;
            } catch (RuntimeIoException e) {
                LOGGER.error("连接服务端 " + CmdOptionHandler.getHost() + ":" + CmdOptionHandler.getPort() + " [失败]");
                Thread.sleep(CmdOptionHandler.getReconnect() * 1000);
            }
        }
    }

}
