package com.tairan.cloud.service.redis;

import redis.clients.jedis.Jedis;

public class RedisDemo {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		jedis.auth("mpj123456");
		String keys = "name";
		// 删数据
		jedis.del(keys);
		// 存数据
		jedis.set(keys, "demo1");
		// 取数据
		String value = jedis.get(keys);
		System.out.println(value);
		// 释放对象资源
		/**
		 * jedisPool.returnResource()遭弃用，官方重写了Jedis的close方法用以代替
		 */
		jedis.close();
	}
}
