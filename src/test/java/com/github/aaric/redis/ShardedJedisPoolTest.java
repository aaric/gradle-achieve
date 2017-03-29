package com.github.aaric.redis;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * ShardedJedisPool测试类
 * 
 * @author Aaric
 * @since 2017-03-25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-Test.xml" })
public class ShardedJedisPoolTest {

	@Autowired
	private ShardedJedisPool shardedJedisPool;

	@Test
	@Ignore
	public void testString() throws Exception {
		// 初始化
		String keyName = "hello";
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		// 设置
		shardedJedis.set(keyName, "hello");
		// 拼接
		shardedJedis.append(keyName, " world");
		// 读取
		System.out.println(shardedJedis.get(keyName));
		// 删除
		shardedJedis.del(keyName);
		// 进行加1操作
		shardedJedis.set("number", "1");
		shardedJedis.incr("number");
	}

	@Test
	@Ignore
	public void testMap() throws Exception {
		// 初始化
		String keyName = "helloMap";
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		// 设置
		Map<String, String> helloMap = new HashMap<String, String>();
		helloMap.put("hello1", "hello1");
		helloMap.put("hello2", "hello2");
		helloMap.put("hello3", "hello3");
		shardedJedis.hmset(keyName, helloMap);
		// 删除
		shardedJedis.hdel(keyName, "hello1");
		// 键集合
		System.out.println(shardedJedis.hkeys(keyName));
		// 值集合
		System.out.println(shardedJedis.hvals(keyName));
		// 长度
		System.out.println(shardedJedis.hlen(keyName));
		// 读取
		System.out.println(shardedJedis.hmget(keyName, "hello1", "hello2"));
	}

	@Test
	public void testList() throws Exception {
		// 初始化
		String keyName = "helloList";
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		// 设置-顶部
		shardedJedis.lpush(keyName, "hello1");
		shardedJedis.lpush(keyName, "hello2");
		shardedJedis.lpush(keyName, "hello3");
		// 设置-尾部
		shardedJedis.rpush(keyName, "hello4");
	}

}
