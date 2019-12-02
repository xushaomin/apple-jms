package com.appleframework.jms.redis.producer;

import java.io.Serializable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicMessageProducer implements MessageProducer {
	
	private static Logger logger = LoggerFactory.getLogger(TopicMessageProducer.class);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	private String topic;

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void sendByte(byte[] message) throws JmsException {
		try {
			redisTemplate.convertAndSend(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		try {
			redisTemplate.convertAndSend(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		try {
			redisTemplate.convertAndSend(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}