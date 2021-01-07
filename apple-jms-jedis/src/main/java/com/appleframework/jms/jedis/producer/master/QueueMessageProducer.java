package com.appleframework.jms.jedis.producer.master;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.cache.jedis.factory.PoolFactory;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueMessageProducer implements MessageProducer {
	
	private static Logger logger = LoggerFactory.getLogger(QueueMessageProducer.class);

	private PoolFactory poolFactory;
	
	private String topic;

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setPoolFactory(PoolFactory poolFactory) {
		this.poolFactory = poolFactory;
	}

	@Override
	public void sendByte(byte[] message) throws JmsException {
		JedisPool jedisPool = poolFactory.getWritePool();
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		JedisPool jedisPool = poolFactory.getWritePool();
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		JedisPool jedisPool = poolFactory.getWritePool();
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}