package com.appleframework.jms.kafka.consumer.multithread.group;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import com.appleframework.jms.core.config.TraceConfig;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;
import com.appleframework.jms.core.utils.UuidUtils;

/**
 * 
 * @author Cruise.Xu
 *
 * 
 * 
 */
public abstract class BaseMessageConsumer<Message> extends AbstractMessageConusmer<Message> {

	private static Logger logger = LoggerFactory.getLogger(BaseMessageConsumer.class);
	
	private ErrorMessageProcessor<Message> errorProcessor;
	
	@Value("${spring.kafka.consumer.error.processor.lock:true}")
	protected Boolean errorProcessorLock = true;
	
	@KafkaListener(topics = "#{'${spring.kafka.consumer.topics}'.split(',')}", 
			concurrency = "${spring.kafka.consumer.concurrency:1}")
	public void run(ConsumerRecord<Object, Message> record) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}
			if(TraceConfig.isSwitchTrace()) {
				if(null != record.key()) {
					MDC.put(TraceConfig.getTraceIdKey(), record.key().toString());
				}
				else {
					MDC.put(TraceConfig.getTraceIdKey(), UuidUtils.genUUID());
				}
			}
			Message message = record.value();
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

	protected void processErrorMessage(Message message) {
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

	public void setErrorProcessor(ErrorMessageProcessor<Message> errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
	
	public void commitSync() {
	}
	
	public void commitAsync() {
	}

}
