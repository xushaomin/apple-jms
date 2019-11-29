package com.appleframework.jms.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;

/**
 * 
 * @author Cruise.Xu
 *
 * 
 * 
 */
public abstract class BaseMessageConsumer extends AbstractMessageConusmer<byte[]> {

	private static Logger logger = LoggerFactory.getLogger(BaseMessageConsumer.class);
	
	private ErrorMessageProcessor<byte[]> errorProcessor;

	protected Boolean errorProcessorLock = true;
	
	@KafkaListener(topics = "#{'${spring.kafka.consumer.topics}'.split(',')}", concurrency = "${spring.kafka.consumer.concurrency:1}")
	public void run(ConsumerRecord<String, byte[]> record) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}
			byte[] message = record.value();
			if (errorProcessorLock) {
				processMessage(message);
			} else {
				try {
					processMessage(message);
				} catch (Exception e) {
					processErrorMessage(message);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected void processErrorMessage(byte[] message) {
		if (!errorProcessorLock && null != errorProcessor) {
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

	public void setErrorProcessor(ErrorMessageProcessor<byte[]> errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
	
	public void commitSync() {
	}
	
	public void commitAsync() {
	}

}
