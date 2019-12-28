package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.core.utils.TraceUtils;


/**
 * @author Cruise.Xu
 * 
 */
@Component
public class TransactionMessageProducer implements MessageProducer {

	@Resource
	private KafkaTemplate<String, byte[]> kafkaTemplate;
		
	private String topic;
		
	public void setKafkaTemplate(KafkaTemplate<String, byte[]> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
		 
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void init() {
		kafkaTemplate.inTransaction();
	}

	public void sendByte(byte[] message) throws JmsException {
		try {
			kafkaTemplate.send(topic, TraceUtils.getTraceId(), message);
		} catch (Exception e) {
			throw new MQException(e);
		}
		
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		try {
			kafkaTemplate.send(topic, TraceUtils.getTraceId(), ByteUtils.toBytes(message));
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		try {
			kafkaTemplate.send(topic, TraceUtils.getTraceId(), message.getBytes());
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