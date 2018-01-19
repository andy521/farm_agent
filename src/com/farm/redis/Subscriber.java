package com.farm.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;


/**
 * @author: taoroot
 * @date: 2017/11/4
 * @description: 订阅者
 */
public class Subscriber extends JedisPubSub {
    private final static Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);

    @Override
    public void onMessage(String channel, String message) {
        LOGGER.info(channel + " : " + message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }

}
