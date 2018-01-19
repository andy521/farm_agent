package com.farm.util;

import org.apache.mina.core.session.IoSession;

import java.net.InetAddress;
import java.net.InetSocketAddress;

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
}
