package com.appleframework.jms.jedis.producer.cluster;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.cache.jedis.factory.JedisClusterFactory;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.JedisCluster;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicMessageProducer2 implements MessageProducer2 {
	
	private static Logger logger = LoggerFactory.getLogger(TopicMessageProducer2.class);

	private JedisClusterFactory connectionFactory;
	
	public void setConnectionFactory(JedisClusterFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public JedisCluster getJedis() {
		return connectionFactory.getClusterConnection();
	}

	@Override
	public void sendByte(String topic, byte[] message) throws JmsException {
		JedisCluster jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		JedisCluster jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
		JedisCluster jedis = this.getJedis();
		try {
			jedis.publish(topic, message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}