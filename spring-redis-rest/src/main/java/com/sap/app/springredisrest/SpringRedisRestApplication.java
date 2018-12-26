package com.sap.app.springredisrest;

import com.sap.app.springredisrest.model.Item;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class SpringRedisRestApplication {

	@Bean
	public StringRedisSerializer stringRedisSerializer(){
		StringRedisSerializer serializer = new StringRedisSerializer();
		return serializer;
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory(){
		return new JedisConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Item> redisTemplate() {
		final RedisTemplate<String, Item>  template = new RedisTemplate<String, Item>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return template;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringRedisRestApplication.class, args);
	}

}

