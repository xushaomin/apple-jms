package com.appleframework.jms.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;

/**
 * @author Cruise.Xu
 * 
 */
@Deprecated
public abstract class RecordMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {

	private static Logger logger = LoggerFactory.getLogger(BaseMessageConsumer.class);

	private ErrorMessageProcessor<ConsumerRecord<String, byte[]>> errorProcessor;

	protected Boolean errorProcessorLock = true;

	@KafkaListener(topics = "#{'${spring.kafka.consumer.topics}'.split(',')}")
	public void run(ConsumerRecord<String, byte[]> record) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}
			if (errorProcessorLock) {
				processMessage(record);
			} else {
				try {
					processMessage(record);
				} catch (Exception e) {
					processErrorMessage(record);
				}
			}	
		} catch (Exception e) {
			throw e;
		}
	}

	protected void processErrorMessage(ConsumerRecord<String, byte[]> message) {
		if (!errorProcessorLock) {
			errorProcessor.processErrorMessage(message, this);
		}
	}

	public void setErrorProcessorLock(Boolean errorProcessorLock) {
		this.errorProcessorLock = errorProcessorLock;
	}

	public void destroy() {
		if (null != errorProcessor) {
			errorProcessor.close();
		}
	}

	public void commit() {
	}
}
