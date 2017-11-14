package com.appleframework.jms.rabbitmq.producer;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

/**
 * @author Cruise.Xu
 * 
 */
public class RabbitMessageProducer implements MessageProducer {

	private final static Logger logger = Logger.getLogger(RabbitMessageProducer.class);

	private Channel channel;

	private String exchange = "";

	private String topic;

	private BasicProperties props = null;

	public void sendByte(byte[] message) throws JmsException {
		try {
			channel.basicPublish(exchange, topic, props, message);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new MQException(e);
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		try {
			channel.basicPublish(exchange, topic, props, ByteUtils.toBytes(message));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
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

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public void setProps(BasicProperties props) {
		this.props = props;
	}

}