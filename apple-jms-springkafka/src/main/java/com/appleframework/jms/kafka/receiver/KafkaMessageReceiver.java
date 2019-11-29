package com.appleframework.jms.kafka.receiver;

import java.io.Serializable;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import com.appleframework.jms.core.receiver.MessageReceiver;
import com.appleframework.jms.core.sender.MessageObject;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class KafkaMessageReceiver extends MessageReceiver<Serializable> {

	private static Logger logger = LoggerFactory.getLogger(KafkaMessageReceiver.class);
	
	@SuppressWarnings("unchecked")
	@KafkaListener(topics = "#{'${spring.kafka.consumer.topics}'.split(',')}")
	public void run(ConsumerRecord<String, byte[]> record) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}
			String topic = record.topic();
			logger.info("topic=" + topic);
			MessageObject<Serializable> object = (MessageObject<Serializable>) ByteUtils.fromByte(record.value());
			logger.info("msgId=" + object.getMsgId());
			logger.info("trackId=" + object.getTrackId());
			processMessage(object.getObject());
		} catch (WakeupException e) {
			throw e;
		}
	}


	public void destroy() {
	}

	public void commitSync() {
	}

	public void commitAsync() {
	}
	
}
