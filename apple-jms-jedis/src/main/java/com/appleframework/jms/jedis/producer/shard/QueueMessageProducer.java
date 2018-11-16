package com.appleframework.jms.jedis.producer.shard;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.JedisShardInfoFactory;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueMessageProducer implements MessageProducer {
	
	private static Logger logger = Logger.getLogger(QueueMessageProducer.class);

	private JedisShardInfoFactory connectionFactory;
	
	private String topic;

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setConnectionFactory(JedisShardInfoFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Jedis getJedis() {
		return connectionFactory.getJedisConnection();
	}

	@Override
	public void sendByte(byte[] message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.lpush(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.lpush(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		Jedis jedis = this.getJedis();
		try {
			jedis.lpush(topic, message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}