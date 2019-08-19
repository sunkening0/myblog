package com.skn.MyBlog.config;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.skn.MyBlog.domain.Blog;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis测试
 * @author skn
 *
 */
@Configuration
public class Redisconfig {
	private RedisConnectionFactory redisConnectionFactory = null;
	
	@Bean(name="RedisConnectionFactory")
	public RedisConnectionFactory initRedisConnectionFactory(){
		if(this.redisConnectionFactory != null){
			return redisConnectionFactory;
		}
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//最大空闲数
		poolConfig.setMaxIdle(30);
		//最大连接数
		poolConfig.setMaxTotal(50);
		//最大等待毫秒数
		poolConfig.setMaxWaitMillis(2000);
		//创建jedis连接工场
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
		//获取单机redis配置
		
		connectionFactory.setHostName("127.0.0.1");
		connectionFactory.setPort(6379);
		//connectionFactory.setPassword("");
		this.redisConnectionFactory = connectionFactory;
		return connectionFactory;
	}
	
	@Bean(name="redisTemplate")
	public RedisTemplate<Object, Object> initRedisTemplate(){
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		//
		RedisSerializer stringRedisSerializer = redisTemplate.getStringSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(stringRedisSerializer);
		//redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(redisTemplate.getHashValueSerializer());
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		
		redisTemplate.setConnectionFactory(initRedisConnectionFactory());
		return redisTemplate;
	}
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext con = new AnnotationConfigApplicationContext(Redisconfig.class);
		RedisTemplate redisTemplate = con.getBean(RedisTemplate.class);
		redisTemplate.opsForValue().set("key1", "123123123");
		redisTemplate.opsForHash().put("hash", "key2", "hahha");
		Blog blog = new Blog();
		blog.setCatalogId(new Integer(123).longValue());
		redisTemplate.opsForHash().put("hahah", "qqq", blog);
	}
}
