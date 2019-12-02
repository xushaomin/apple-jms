package com.appleframework.jms.redis.producer;

import java.io.Serializable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer;


/**
 * @author Cruise.Xu
 * 
 */
@Component
public class QueueMessageProducer implements MessageProducer {
	
	private static Logger logger = LoggerFactory.getLogger(QueueMessageProducer.class);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	private String topic;
	
		 
	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public void sendByte(byte[] message) throws JmsException {
		try {
			redisTemplate.opsForList().leftPush(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		try {
			redisTemplate.opsForList().leftPush(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		try {
			redisTemplate.opsForList().leftPush(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}