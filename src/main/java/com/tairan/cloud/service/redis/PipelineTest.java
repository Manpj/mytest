package com.tairan.cloud.service.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * hzmpj 2017年8月1日
 */
public class PipelineTest {

	public static void main(String[] args) {
		int count = 10000;
		long start = System.currentTimeMillis();
		withoutPipeline(count);
		long end = System.currentTimeMillis();
		System.out.println("withoutPipeline: " + (end - start));

		start = System.currentTimeMillis();
		usePipeline(count);
		end = System.currentTimeMillis();
		System.out.println("usePipeline: " + (end - start));
	}

	private static void withoutPipeline(int count) {
		Jedis jedis = null;
		try {
			jedis = new Jedis("127.0.0.1", 6379);
			jedis.auth("mpj123456");
			for (int i = 0; i < count; i++) {
				//是将存储的值递增加1
				jedis.incr("testKey1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	private static void usePipeline(int count) {
		Jedis jedis = null;
		try {
			jedis = new Jedis("127.0.0.1", 6379);
			jedis.auth("mpj123456");
			Pipeline pipeline = jedis.pipelined();
			for (int i = 0; i < count; i++) {
				pipeline.incr("testKey2");
			}
			pipeline.sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
}
