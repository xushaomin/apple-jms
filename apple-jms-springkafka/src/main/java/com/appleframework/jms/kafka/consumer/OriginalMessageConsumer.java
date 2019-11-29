package com.appleframework.jms.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
@Deprecated
public abstract class OriginalMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {

	private static Logger logger = LoggerFactory.getLogger(OriginalMessageConsumer.class);

	@KafkaListener(topics = "#{'${spring.kafka.consumer.topics}'.split(',')}")
	public void run(ConsumerRecord<String, byte[]> record) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}
			processMessage(record);
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
