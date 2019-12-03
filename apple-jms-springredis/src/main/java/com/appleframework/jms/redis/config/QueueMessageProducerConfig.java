package com.appleframework.jms.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.redis.producer.QueueMessageProducer;
import com.appleframework.jms.redis.producer.QueueMessageProducer2;

@Configuration
public class QueueMessageProducerConfig {

	@Value("${spring.redis.producer.queue:null}")
	private String topic;

	@Bean
	@ConditionalOnMissingBean(MessageProducer.class)
	public MessageProducer messageProducerFactory(RedisTemplate<String, Object> redisTemplate) {
		if("null".equals(topic)) {
			return null;
		}
		QueueMessageProducer messageProducer = new QueueMessageProducer();
		messageProducer.setRedisTemplate(redisTemplate);
		messageProducer.setTopic(topic);
		return messageProducer;
	}

	@Bean
	@ConditionalOnMissingBean(MessageProducer2.class)
	public MessageProducer2 messageProducer2Factory(RedisTemplate<String, Object> redisTemplate) {
		QueueMessageProducer2 messageProducer = new QueueMessageProducer2();
		messageProducer.setRedisTemplate(redisTemplate);
		return messageProducer;
	}

}
