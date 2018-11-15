package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.kafka.utils.StringUtils;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;


/**
 * @author Cruise.Xu
 * 
 */
public class KafkaMessageProducer implements MessageProducer {

	private Producer<String, byte[]> producer;
	
	private String topic;
	
	private String key = "-1";
	
	public void setProducer(Producer<String, byte[]> producer) {
		this.producer = producer;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setKey(String key) {
		if(StringUtils.isEmpty(key)) {
			this.key = null;
		}
		else {
			this.key = key;
		}
	}

	public void sendByte(byte[] message) throws JmsException {
		try {
			ProducerRecord<String, byte[]> producerData 
				= new ProducerRecord<String, byte[]>(topic, key, message);
				producer.send(producerData);
		} catch (Exception e) {
			throw new MQException(e);
		}
		
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		try {
			ProducerRecord<String, byte[]> producerData 
			= new ProducerRecord<String, byte[]>(topic, key, ByteUtils.toBytes(message));
		producer.send(producerData);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
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