package com.appleframework.jms.jedis.producer.single;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
@SuppressWarnings("deprecation")
public class TopicMessageProducer2 implements MessageProducer2 {
	
	private static Logger logger = LoggerFactory.getLogger(TopicMessageProducer2.class);

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
			logger.error("", e);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.publish(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

}