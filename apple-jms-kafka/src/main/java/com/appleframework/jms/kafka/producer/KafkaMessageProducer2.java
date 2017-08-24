package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.kafka.utils.StringUtils;

/**
 * @author Cruise.Xu
 * 
 */
@SuppressWarnings("deprecation")
public class KafkaMessageProducer2 implements MessageProducer2 {

	private Producer<String, byte[]> producer;
	
	private String key = "-1";

	public void setProducer(Producer<String, byte[]> producer) {
		this.producer = producer;
	}
	
	public void setKey(String key) {
		if(StringUtils.isEmpty(key))
			this.key = null;
		else
			this.key = key;
	}
	
	@Override
	public void sendByte(String topic, byte[] message) throws JmsException {
		try {
			KeyedMessage<String, byte[]> producerData 
				= new KeyedMessage<String, byte[]>(topic, key, message);
			producer.send(producerData);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		try {
			KeyedMessage<String, byte[]> producerData 
				= new KeyedMessage<String, byte[]>(topic, key, ByteUtils.toBytes(message));
			producer.send(producerData);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
		try {
			KeyedMessage<String, byte[]> producerData 
				= new KeyedMessage<String, byte[]>(topic, key, message.getBytes());
			producer.send(producerData);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}
	
	public void destory() {
		if(null != producer)
			producer.close();
	}

}
