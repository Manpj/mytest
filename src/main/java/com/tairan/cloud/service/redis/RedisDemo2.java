package com.tairan.cloud.service.redis;

import java.util.List;
import java.util.ResourceBundle;

import com.google.common.collect.Lists;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisDemo2 {
	/**
	 * 静态代码段中完成初始化
	 */
	private static ShardedJedisPool pool;
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException("[redis.properties] is not found!");
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
		config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWaitMillis")));
		config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
		/**
		 * 添加这个配置会报错
		 */
		// config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));

		JedisShardInfo hedisShardInfo1 = new JedisShardInfo(bundle.getString("redis.ip"),
				Integer.valueOf(bundle.getString("redis.port")));
		hedisShardInfo1.setPassword("mpj123456");
		JedisShardInfo hedisShardInfo2 = new JedisShardInfo(bundle.getString("redis2.ip"),
				Integer.valueOf(bundle.getString("redis2.port")));
		hedisShardInfo2.setPassword("mpj123456");

		List<JedisShardInfo> list = Lists.newArrayList();
		list.add(hedisShardInfo1);
		list.add(hedisShardInfo2);
		pool = new ShardedJedisPool(config, list);

	}

	public static void main(String[] args) {
		ShardedJedis jedis = pool.getResource();
		String keys = "name2";
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
