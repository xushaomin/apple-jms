package com.appleframework.jms.jedis.producer.shard;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.JedisShardInfoFactory;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicMessageProducer2 implements MessageProducer2 {
	
	private static Logger logger = Logger.getLogger(TopicMessageProducer2.class);

	private JedisShardInfoFactory connectionFactory;
	
	public void setConnectionFactory(JedisShardInfoFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Jedis getJedis() {
		return connectionFactory.getJedisConnection();
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