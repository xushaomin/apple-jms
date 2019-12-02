package com.appleframework.jms.redis.producer;

import java.io.Serializable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer2;

/**
 * @author Cruise.Xu
 * 
 */
@Component
public class TopicMessageProducer2 implements MessageProducer2 {
	
	private static Logger logger = LoggerFactory.getLogger(TopicMessageProducer2.class);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void sendByte(String topic, byte[] message) throws JmsException {
		try {
			redisTemplate.convertAndSend(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		try {
			redisTemplate.convertAndSend(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
		try {
			redisTemplate.convertAndSend(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}