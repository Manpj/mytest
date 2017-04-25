package com.tairan.cloud.service.redis;

import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

public class RedisDemo1 {
	/**
	 * 静态代码段中完成初始化
	 */
	private static JedisPool pool;
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException("[redis.properties] is not found!");
		}
		JedisPoolConfig config = new JedisPoolConfig();
		/**
		 * 低版本中 config.setMaxActive(Integer.valueOf(bundle.getString(
		 * "redis.pool.maxActive")));
		 * config.setMaxWait(Long.valueOf(bundle.getString("redis.pool.maxWait")
		 * )); 高版本中废弃了这两个方法 该为
		 * config.setMaxTotal(Integer.valueOf(bundle.getString(
		 * "redis.pool.maxTotal")));
		 * config.setMaxWaitMillis(Long.valueOf(bundle.getString(
		 * "redis.pool.maxWaitMillis")));
		 */
		config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
		config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWaitMillis")));
		config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
		/**
		 * 添加这个配置会报错
		 */
//		config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
		
//		config.setMaxTotal(1024);
//		config.setMaxWaitMillis(1000);
//		config.setMaxIdle(200);
//		config.setTestOnReturn(true);
//		config.setTestOnBorrow(true);
		
		pool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));
	}

	public static void main(String[] args) {
		// Jedis jedis = new Jedis("127.0.0.1", 6379);
		Jedis jedis = pool.getResource();
		jedis.auth("mpj123456");
		String keys = "name1";
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
