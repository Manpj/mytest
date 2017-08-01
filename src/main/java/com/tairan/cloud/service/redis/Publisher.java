package com.tairan.cloud.service.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

/**
 * hzmpj 2017年7月31日
 */
public class Publisher {
	private static final Logger logger = Logger.getLogger(Publisher.class);

	private final Jedis publisherJedis;

	private final String channel;

	public Publisher(Jedis publisherJedis, String channel) {
		super();
		this.publisherJedis = publisherJedis;
		this.channel = channel;
	}

	public void startPublish() {
		logger.info("Type your message (quit for terminate)");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String line = reader.readLine();
				if (!"quit".equals(line)) {
					publisherJedis.publish(channel, line);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			logger.error("IO failure while reading input", e);
		}
	}

}
