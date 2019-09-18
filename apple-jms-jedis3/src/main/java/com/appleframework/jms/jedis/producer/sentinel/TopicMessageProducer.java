package com.appleframework.jms.jedis.producer.sentinel;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.cache.jedis.factory.JedisSentinelFactory;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicMessageProducer implements MessageProducer {
	
	private static Logger logger = LoggerFactory.getLogger(TopicMessageProducer.class);

	private JedisSentinelFactory connectionFactory;
	
	private String topic;

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setConnectionFactory(JedisSentinelFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Jedis getJedis() {
		return connectionFactory.getJedisConnection();
	}

	@Override
	public void sendByte(byte[] message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}