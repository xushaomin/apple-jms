package com.appleframework.jms.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.appleframework.jms.redis.consumer.TopicBaseMessageConsumer;

@Configuration
public class TopicMessageConsumerConfig {

	@Value("${spring.redis.consumer.topics}")
	private String topics;

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		String[] topicss = topics.split(",");
		for (String topic : topicss) {
			container.addMessageListener(listenerAdapter, new PatternTopic(topic));
		}
		return container;
	}
	
	@Bean
	MessageListenerAdapter listenerAdapter(TopicBaseMessageConsumer consumer) {
		return new MessageListenerAdapter(consumer, "onMessage");
	}

}