package com.tairan.cloud.service.redis;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * hzmpj 2017年7月31日
 */
public class Program {
	private final static Logger LOGGER = Logger.getLogger(Program.class);

	public static final String CHANNEL_NAME = "MyChannel";

	public static final String REDIS_HOST = "127.0.0.1";

	public static final int REDIS_PORT = 6379;

	private final static JedisPoolConfig POOL_CONFIG = new JedisPoolConfig();

	private final static JedisPool JEDIS_POOL = new JedisPool(POOL_CONFIG, REDIS_HOST, REDIS_PORT, 0);

	public static void main(String[] args) {
		final Jedis subscriberJedis = JEDIS_POOL.getResource();
		final Jedis publisherJedis = JEDIS_POOL.getResource();
		subscriberJedis.auth("mpj123456");
		publisherJedis.auth("mpj123456");
		final Subscriber subscriber = new Subscriber();

		// 订阅线程：接收消息
		// .之所以使用线程，因为subscribe这个方法中有个循环，同时用这个线程作为一个订阅者，而不影响主线程（即消息发布者）的运行
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LOGGER.info("Subscribing to \"MyChannel\". This thread will be blocked.");
					// 使用subscriber订阅CHANNEL_NAME上的消息，这一句之后，线程进入订阅模式，阻塞。
					subscriberJedis.subscribe(subscriber, CHANNEL_NAME);

					// 当unsubscribe()方法被调用时，才执行以下代码
					LOGGER.info("Subscription ended.");
				} catch (Exception e) {
					LOGGER.error("Subscribing failed.", e);
				}
			}

		}).start();
		// 主线程：发布消息到CHANNEL_NAME频道上
		new Publisher(publisherJedis, CHANNEL_NAME).startPublish();
		publisherJedis.close();

		// Unsubscribe
		subscriber.unsubscribe();
		subscriberJedis.close();
	}
}
