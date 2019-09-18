package com.appleframework.jms.jedis.producer.master;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.cache.jedis.factory.PoolFactory;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueMessageProducer2 implements MessageProducer2 {
	
	private static Logger logger = LoggerFactory.getLogger(QueueMessageProducer2.class);

	private PoolFactory poolFactory;
		
	public void setPoolFactory(PoolFactory poolFactory) {
		this.poolFactory = poolFactory;
	}

	@Override
	public void sendByte(String topic, byte[] message) throws JmsException {
		JedisPool jedisPool = poolFactory.getWritePool();
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		JedisPool jedisPool = poolFactory.getWritePool();
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
		JedisPool jedisPool = poolFactory.getWritePool();
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}