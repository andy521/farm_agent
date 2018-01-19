package com.farm.redis;

import com.farm.server.MyIoHandler;
import com.farm.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author: taoroot
 * @date: 2018/1/19
 * @description:
 */
public class Publisher {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyIoHandler.class);
    private final static String FARM_STATUS_TOP = ConfigUtil.get("RedisFarmStatusTopic");

    public static void sendStatus(String status) {
        Jedis jedis = null;
        try {
            jedis = JedisConnectionPool.getInstance().getResource();
            jedis.publish(FARM_STATUS_TOP, status);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
