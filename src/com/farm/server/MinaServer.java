package com.farm.server;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

/**
 * @author: taoroot
 * @date: 2017/10/31
 * @description: 程序入口
 */
public class MinaServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(MinaServer.class);
    // MINA TCP 初始化工具
    private static NioSocketAcceptor acceptor;

    public MinaServer() throws IOException {
        acceptor = new NioSocketAcceptor();
        // 设置缓存大小
        acceptor.getSessionConfig().setReadBufferSize(2048);
        // 解决端口占用
        acceptor.getSessionConfig().setReuseAddress(true);
        // 过滤器 - io日志层
        acceptor.getFilterChain().addLast("logging", new LoggingFilter());
        // todo 过滤器 - 协议解析层
        acceptor.getFilterChain().addLast("coder", new ProtocolCodecFilter(new MyProtocolCodecFactory()));
        // todo 过滤器 - 心跳包
        // 超时过滤层 (对TCP在线,心跳包超时的设备主动断开连接)
        KeepAliveFilter keepAliveFilter = new MyKeepAliveFilter(new MyKeepAliveMessageFactory());
        // 设置心跳频率
        keepAliveFilter.setRequestInterval(CmdOptionHandler.getInterval());
        acceptor.getFilterChain().addLast("keepAlive", keepAliveFilter);
        // 设置业务处理器
        acceptor.setHandler(new MyIoHandler());
        // 绑定监听端口
        acceptor.bind(new InetSocketAddress(CmdOptionHandler.getPort()));

        LOGGER.info("Farm agent is listening port: {};", CmdOptionHandler.getPort());
    }

    public static void main(String[] args) throws Exception {
        // 获取参数
        CmdOptionHandler.CmdOptionHandler(args);
        boolean flag = false;
        // 启动服务
        while(!flag) {
            try {
                new MinaServer();
                flag = true;
            } catch (BindException e) {
                flag = false;
                LOGGER.error(e.getMessage());
            }
            Thread.sleep(10000);
        }
    }

}
