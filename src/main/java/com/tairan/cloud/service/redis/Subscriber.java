package com.tairan.cloud.service.redis;

import jxl.common.Logger;
import redis.clients.jedis.JedisPubSub;

/**
 * hzmpj 2017年7月31日 处理订阅和取消订阅某些channel的相关事件
 */
public class Subscriber extends JedisPubSub {

	private static final Logger logger = Logger.getLogger(Subscriber.class);

	@Override
	public void onMessage(String channel, String message) {
		logger.info(String.format("Message Channel:%s,Msg:%s", channel, message));
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		logger.info(String.format("PMessage. Pattern: %s, Channel: %s, Msg: %s", pattern, channel, message));
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		logger.info("onSubscribe");
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		logger.info("onUnsubscribe");
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		logger.info("onPUnsubscribe");
	}
	
	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		logger.info("onPSubscribe");
	}

}
