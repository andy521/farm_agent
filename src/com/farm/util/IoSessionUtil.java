package com.farm.util;

import org.apache.mina.core.session.IoSession;

import java.io.IOException;
import java.net.*;

/**
 * @author: taoroot
 * @date: 2018/1/19
 * @description:
 */
public class IoSessionUtil {

    public static final String MINA_HEAD_COUNT_MAX = "MINA_HEAD_COUNT_MAX";

    /**
     * 从session中获取地址(格式为: address:port)
     *
     * @param session
     * @return
     */
    public static String getAddressPort(IoSession session) {
        InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
        if (socketAddress != null) {
            InetAddress inetAddress = socketAddress.getAddress();
            return inetAddress.getHostAddress() + ":" + socketAddress.getPort();
        }
        return "1.1.1.1:1";
    }

    /**
     * 检查端口有没有被占用
     *
     * @param host
     * @param port
     * @return
     */
    public static final int MIN_PORT_NUMBER = 1;
    public static final int MAX_PORT_NUMBER = 65536;

    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

}
