package com.farm.redis;


import com.farm.server.CmdOptionHandler;
import com.farm.server.MinaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: taoroot
 * @date: 2017/11/4
 * @description: Jidis 连接池
 */
public class JedisConnectionPool {

    private final static Logger LOGGER = LoggerFactory.getLogger(MinaServer.class);

    private static JedisPool pool = null;

    public static JedisPool getInstance() {
        if(pool== null) {
            pool = new JedisPool(new JedisPoolConfig(), CmdOptionHandler.getHost());
        }
        return pool;
    }
}
