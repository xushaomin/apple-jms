package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import javax.annotation.PreDestroy;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.kafka.utils.StringUtils;


/**
 * @author Cruise.Xu
 * 
 */
@Component
public class KafkaMessageProducer implements MessageProducer {

	private KafkaTemplate<String, byte[]> kafkaTemplate;
	
	public void setKafkaTemplate(KafkaTemplate<String, byte[]> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	private String topic;
	
	private String key = "-1";
		 
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
			kafkaTemplate.send(topic, key, message);
		} catch (Exception e) {
			throw new MQException(e);
		}
		
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		try {
			kafkaTemplate.send(topic, key, ByteUtils.toBytes(message));
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		try {
			kafkaTemplate.send(topic, key, message.getBytes());
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@PreDestroy
	public void destory() {
		try {
			kafkaTemplate.flush();
		} catch (Exception e) {
		}
	}
}