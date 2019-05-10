package com.appleframework.jms.jedis.producer.single;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicMessageProducer2 implements MessageProducer2 {
	
	private static Logger logger = Logger.getLogger(TopicMessageProducer2.class);

	private JedisPool jedisPool;
	
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public Jedis getJedis() {
		return jedisPool.getResource();
	}

	@Override
	public void sendByte(String topic, byte[] message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic, message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}