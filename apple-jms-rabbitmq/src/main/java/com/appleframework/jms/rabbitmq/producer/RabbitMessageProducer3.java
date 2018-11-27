package com.appleframework.jms.rabbitmq.producer;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer3;
import com.appleframework.jms.core.utils.ByteUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

/**
 * @author Cruise.Xu
 * 
 */
public class RabbitMessageProducer3 implements MessageProducer3 {

	private final static Logger logger = LoggerFactory.getLogger(RabbitMessageProducer3.class);

	private Channel channel;

	private BasicProperties props = null;

	public void sendByte(String topic, String exchange, byte[] message) throws JmsException {
		try {
			channel.basicPublish(exchange, topic, props, message);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new MQException(e);
		}
	}

	@Override
	public void sendObject(String topic, String exchange, Serializable message) throws JmsException {
		try {
			channel.basicPublish(exchange, topic, props, ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String topic, String exchange, String message) throws JmsException {
		try {
			channel.basicPublish(exchange, topic, props, message.getBytes());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new MQException(e);
		}
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public void setProps(BasicProperties props) {
		this.props = props;
	}

}