package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer3;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public class KafkaMessageProducer3 implements MessageProducer3 {

	private Producer<String, byte[]> producer;

	public void setProducer(Producer<String, byte[]> producer) {
		this.producer = producer;
	}
	
	@Override
	public void sendByte(String topic, String key, byte[] message) throws JmsException {
		try {
			ProducerRecord<String, byte[]> producerData 
				= new ProducerRecord<String, byte[]>(topic, key, message);
			producer.send(producerData);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendObject(String topic, String key, Serializable message) throws JmsException {
		try {
			ProducerRecord<String, byte[]> producerData 
				= new ProducerRecord<String, byte[]>(topic, key, ByteUtils.toBytes(message));
			producer.send(producerData);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String topic, String key, String message) throws JmsException {
		try {
			ProducerRecord<String, byte[]> producerData 
				= new ProducerRecord<String, byte[]>(topic, key, message.getBytes());
			producer.send(producerData);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}
	
	public void destory() {
		try {
			producer.close();
		} catch (Exception e) {
		}
	}
	
}
