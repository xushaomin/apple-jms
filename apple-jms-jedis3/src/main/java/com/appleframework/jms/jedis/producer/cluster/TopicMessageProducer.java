package com.appleframework.jms.jedis.producer.cluster;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.JedisClusterFactory;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.JedisCluster;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicMessageProducer implements MessageProducer {
	
	private static Logger logger = Logger.getLogger(TopicMessageProducer.class);

	private JedisClusterFactory connectionFactory;
	
	private String topic;

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setConnectionFactory(JedisClusterFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public JedisCluster getJedis() {
		return connectionFactory.getClusterConnection();
	}

	@Override
	public void sendByte(byte[] message) throws JmsException {
		JedisCluster jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		JedisCluster jedis = this.getJedis();
		try {
			jedis.publish(topic.getBytes(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		JedisCluster jedis = this.getJedis();
		try {
			jedis.publish(topic, message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}