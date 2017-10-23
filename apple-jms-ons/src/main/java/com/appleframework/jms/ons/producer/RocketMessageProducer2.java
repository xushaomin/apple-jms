package com.appleframework.jms.ons.producer;

import java.io.Serializable;

import com.aliyun.openservices.ons.api.Message;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.ons.RocketMQProducer;

/**
 * @author Cruise.Xu
 * 
 */
public class RocketMessageProducer2 implements MessageProducer2 {

	private RocketMQProducer producer;
	private String tag, key;

	public void setProducer(RocketMQProducer producer) {
		this.producer = producer;
	}

	public void sendByte(String topic, byte[] message) throws MQException {
		Message msg = new Message(topic, tag, key, message);
		try {
			producer.send(msg);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	public void sendObject(String topic, Serializable message) throws MQException {
		Message msg = new Message(topic, tag, key, ByteUtils.toBytes(message));
		try {
			producer.send(msg);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	public void sendText(String topic, String message) throws MQException {
		Message msg = new Message(topic, tag, key, ByteUtils.toBytes(message));
		try {
			producer.send(msg);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public void destory() {
		try {
			producer.close();
		} catch (Exception e) {
		}
	}

}
