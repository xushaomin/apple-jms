package com.appleframework.jms.kafka.consumer.multithread.group;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.utils.Contants;
import com.appleframework.jms.core.utils.UuidUtils;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {

	private static Logger logger = LoggerFactory.getLogger(OriginalMessageConsumer.class);

	@KafkaListener(topics = "#{'${spring.kafka.consumer.topics}'.split(',')}", 
			concurrency = "${spring.kafka.consumer.concurrency:1}")
	public void run(ConsumerRecord<String, byte[]> record) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}
			if(null != record.key()) {
				MDC.put(Contants.KEY_TRACE_ID, record.key());
			}
			else {
				MDC.put(Contants.KEY_TRACE_ID, UuidUtils.genUUID());
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
