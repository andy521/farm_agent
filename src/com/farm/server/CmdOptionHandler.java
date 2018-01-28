package com.farm.server;

import com.farm.util.ConfigUtil;
import org.apache.commons.cli.*;

/**
 * @author: taoroot
 * @date: 2018/1/18
 * @description:
 */
public class CmdOptionHandler {
    // 服务器地址
    private static String host = ConfigUtil.get("RedisHost");
    // 服务器端口
    private static int port = Integer.parseInt(ConfigUtil.get("MinaServerPort"));
    // 心跳包频率
    private static int interval = Integer.parseInt(ConfigUtil.get("MinaServerInterval"));
    // 心跳包接收超时时间
    private static int timeout = Integer.parseInt(ConfigUtil.get("MinaServerTimeout"));
    // 重连频率
    private static int reconnect = Integer.parseInt(ConfigUtil.get("MinaServerReconnect"));


    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static int getInterval() {
        return interval;
    }

    public static int getTimeout() {
        return timeout;
    }

    public static int getReconnect() {
        return reconnect;
    }

    /**
     * 参数解析
     *
     * @param args 参数数组
     */
    public static void CmdOptionHandler(String[] args) {
        // 参数配置容器
        Options options = new Options();
        // 添加参数
        options.addOption("h", "host", true, "Configuration host");
        options.addOption("r", "reconnect", true, "reconnection time");
        options.addOption("p", "port", true, "Configuration port");
        options.addOption("t", "timeout", true, "Configuration keepAlive timeout (s)");
        options.addOption("?", "help", false, "Print help for the command.");

        // 解析参数
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        String formatStr = "jar -jar farm_agent.jar [-h/--host][-p/--port][-t/--timeout][-v/--version][-h/--help]";
        HelpFormatter formatter = new HelpFormatter();

        // 处理Options和参数
        try {
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            formatter.printHelp(formatStr, options); // 如果发生异常，则打印出帮助信息
        }

        // 设置服务器地址
        if (cmd.hasOption("h")) {
            host = cmd.getOptionValue("h");
        }
        // 设置服务器端口
        if (cmd.hasOption("p")) {
            port = Integer.parseInt(cmd.getOptionValue("p"));
        }
        // 客户端重连时间
        if (cmd.hasOption("r")) {
            reconnect = Integer.parseInt(cmd.getOptionValue("r"));
        }
        // 如果包含有-p或--port，设置端口
        if (cmd.hasOption("t")) {
            timeout = Integer.parseInt(cmd.getOptionValue("t"));
        }
        // 如果包含有-h或--help，则打印出帮助信息
        if (cmd.hasOption("?")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatStr, "", options, "");
        }
    }
}
