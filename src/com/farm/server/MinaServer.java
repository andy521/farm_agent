package com.farm.server;

import com.farm.util.ConfigUtil;
import org.apache.commons.cli.*;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author: taoroot
 * @date: 2017/10/31
 * @description: 程序入口
 */
public class MinaServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(MinaServer.class);
    // MINA 监听端口
    private static int port = Integer.parseInt(ConfigUtil.get("MinaServerPort"));
    // 心跳包时间
    private static int timeout = Integer.parseInt(ConfigUtil.get("MinaServerPort"));
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
        keepAliveFilter.setRequestInterval(timeout);
        acceptor.getFilterChain().addLast("keepAlive", keepAliveFilter);
        // 设置业务处理器
        acceptor.setHandler(new MyIoHandler());
        // 绑定监听端口
        acceptor.bind(new InetSocketAddress(port));

        LOGGER.info("Farm agent is listening port: {}; the timeout interval: {}s", port, timeout);
    }

    public static void main(String[] args) throws Exception {
        // 获取参数
        CmdOptionHandler(args);
        // 启动服务
        new MinaServer();
    }

    /**
     * 参数解析
     *
     * @param args 参数数组
     */
    private static void CmdOptionHandler(String[] args) {
        // 参数配置容器
        Options options = new Options();
        // 添加参数
        options.addOption("p", "port", true, "Configuration port");
        options.addOption("t", "timeout", true, "Configuration keepAlive timeout (s)");
        options.addOption("h", "help", false, "Print help for the command.");

        // 解析参数
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        String formatStr = "jar -jar farm_agent.jar [-p/--port][-t/--timeout][-v/--version][-h/--help]";
        HelpFormatter formatter = new HelpFormatter();

        // 处理Options和参数
        try {
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            formatter.printHelp(formatStr, options); // 如果发生异常，则打印出帮助信息
        }

        // 如果包含有-p或--port，设置端口
        if (cmd.hasOption("p")) {
            port = Integer.parseInt(cmd.getOptionValue("p"));
        }
        // 如果包含有-p或--port，设置端口
        if (cmd.hasOption("t")) {
            timeout = Integer.parseInt(cmd.getOptionValue("t"));
        }
        // 如果包含有-h或--help，则打印出帮助信息
        if (cmd.hasOption("h")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatStr, "", options, "");
        }
    }
}
