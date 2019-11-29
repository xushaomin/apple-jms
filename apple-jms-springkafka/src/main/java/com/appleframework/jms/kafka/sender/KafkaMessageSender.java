package com.appleframework.jms.kafka.sender;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.kafka.core.KafkaTemplate;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.sender.MessageObject;
import com.appleframework.jms.core.sender.MessageSender;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public class KafkaMessageSender implements MessageSender {

	@Resource
	private KafkaTemplate<String, byte[]> kafkaTemplate;

	public void setKafkaTemplate(KafkaTemplate<String, byte[]> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public String send(String topic, Serializable message, String trackId) throws JmsException {
		try {
			String msgId = UUID.randomUUID().toString();
			MessageObject<Serializable> sendObject = new MessageObject<Serializable>(message, trackId, msgId);
			kafkaTemplate.send(topic, ByteUtils.toBytes(sendObject));
			return msgId;
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

}