package com.appleframework.jms.kafka.sender;

import java.io.Serializable;
import java.util.UUID;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.sender.MessageSender;
import com.appleframework.jms.core.sender.MessageObject;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public class KafkaMessageSender implements MessageSender {

	private Producer<String, byte[]> producer;

	public void setProducer(Producer<String, byte[]> producer) {
		this.producer = producer;
	}

	@Override
	public String send(String topic, Serializable message, String trackId) throws JmsException {
		try {
			String msgId = UUID.randomUUID().toString();
			MessageObject<Serializable> sendObject = new MessageObject<Serializable>(message, trackId, msgId);
			KeyedMessage<String, byte[]> producerData 
				= new KeyedMessage<String, byte[]>(topic, ByteUtils.toBytes(sendObject));
			producer.send(producerData);
			return msgId;
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

}