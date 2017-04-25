package com.tairan.cloud.service.redis;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisDemo3 {

	private ApplicationContext app;
	private ShardedJedisPool pool;

	@Before
	public void before() {
		app = new ClassPathXmlApplicationContext("classpath:applicationContext-beans.xml");
		pool = (ShardedJedisPool) app.getBean("shardedJedisPool");
	}

	@Test
	public void test() {
		ShardedJedis jedis = pool.getResource();
		String keys = "name";
		String value = "demo3";
		// 删除数据
		//jedis.del(keys);
		// 存数据
		String v=jedis.set(keys, value);
		// 取数据
		//String v = jedis.get(keys);
		System.out.println(v);
		// 释放对象池
		jedis.close();      
	}
}
