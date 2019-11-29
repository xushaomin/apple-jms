package com.appleframework.jms.redis.producer;

import java.io.Serializable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueMessageProducer2 implements MessageProducer2 {
	
	private static Logger logger = LoggerFactory.getLogger(QueueMessageProducer2.class);

	@Resource
	private RedisTemplate<String, byte[]> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, byte[]> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void sendByte(String topic, byte[] message) throws JmsException {
		try {
			redisTemplate.opsForList().leftPush(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		try {
			redisTemplate.opsForList().leftPush(topic, ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
		try {
			redisTemplate.opsForList().leftPush(topic, message.getBytes());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}