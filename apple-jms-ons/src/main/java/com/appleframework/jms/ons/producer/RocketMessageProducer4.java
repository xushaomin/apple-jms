package com.appleframework.jms.ons.producer;

import java.io.Serializable;

import com.aliyun.openservices.ons.api.Message;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer4;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.ons.RocketMQProducer;

/**
 * @author Cruise.Xu
 * 
 */
public class RocketMessageProducer4 implements MessageProducer4 {

	private RocketMQProducer producer;	

	public void setProducer(RocketMQProducer producer) {
		this.producer = producer;
	}

	public void sendByte(String topic, String tags, String keys, byte[] message) throws MQException {
        Message msg = new Message(topic, tags, keys, message);
        try {
			producer.send(msg);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	public void sendObject(String topic, String tags, String keys, Serializable message) throws MQException {		
		Message msg = new Message(topic, tags, ByteUtils.toBytes(message));
		try {
			producer.send(msg);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	public void sendText(String topic, String tags, String keys, String message) throws MQException {		
		Message msg = new Message(topic, tags, ByteUtils.toBytes(message));
		try {
			producer.send(msg);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}	

}
