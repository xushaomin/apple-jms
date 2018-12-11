package com.appleframework.jms.jedis.producer.cluster;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.JedisClusterFactory;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.JedisCluster;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueMessageProducer2 implements MessageProducer2 {
	
	private static Logger logger = Logger.getLogger(QueueMessageProducer2.class);

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
			jedis.lpush(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		JedisCluster jedis = this.getJedis();
		try {
			jedis.lpush(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
		JedisCluster jedis = this.getJedis();
		try {
			jedis.lpush(topic, message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}