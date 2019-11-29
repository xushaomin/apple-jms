package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer3;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
@Component
public class KafkaMessageProducer3 implements MessageProducer3 {

	@Resource
	private KafkaTemplate<String, byte[]> kafkaTemplate;
	
	public void setKafkaTemplate(KafkaTemplate<String, byte[]> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@Override
	public void sendByte(String topic, String key, byte[] message) throws JmsException {
		try {
			kafkaTemplate.send(topic, key, message);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendObject(String topic, String key, Serializable message) throws JmsException {
		try {
			kafkaTemplate.send(topic, key, ByteUtils.toBytes(message));
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String topic, String key, String message) throws JmsException {
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
