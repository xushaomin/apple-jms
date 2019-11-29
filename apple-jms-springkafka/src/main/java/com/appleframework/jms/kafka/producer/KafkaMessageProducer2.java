package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.kafka.utils.StringUtils;

/**
 * @author Cruise.Xu
 * 
 */
@Component
public class KafkaMessageProducer2 implements MessageProducer2 {

	@Resource
	private KafkaTemplate<String, byte[]> kafkaTemplate;
	
	public void setKafkaTemplate(KafkaTemplate<String, byte[]> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	private String key = "-1";
	
	public void setKey(String key) {
		if(StringUtils.isEmpty(key)) {
			this.key = null;
		}
		else {
			this.key = key;
		}
	}
	
	@Override
	public void sendByte(String topic, byte[] message) throws JmsException {
		try {
			kafkaTemplate.send(topic, key, message);
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendObject(String topic, Serializable message) throws JmsException {
		try {
			kafkaTemplate.send(topic, key, ByteUtils.toBytes(message));
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String topic, String message) throws JmsException {
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
