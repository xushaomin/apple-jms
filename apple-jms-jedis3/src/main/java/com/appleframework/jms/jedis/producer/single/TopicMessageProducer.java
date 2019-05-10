package com.appleframework.jms.jedis.producer.single;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicMessageProducer implements MessageProducer {
	
	private static Logger logger = Logger.getLogger(TopicMessageProducer.class);

	private JedisPool jedisPool;
	
	private String topic;

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	public Jedis getJedis() {
		return jedisPool.getResource();
	}

	@Override
	public void sendByte(byte[] message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic, message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}