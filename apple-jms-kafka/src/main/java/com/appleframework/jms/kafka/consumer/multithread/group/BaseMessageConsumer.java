package com.appleframework.jms.kafka.consumer.multithread.group;

import java.util.Properties;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class BaseMessageConsumer extends AbstractMessageConusmer<byte[]> {

	protected String topic;

	protected String prefix = "";

	private ErrorMessageProcessor<byte[]> errorProcessor;

	protected Boolean errorProcessorLock = true;

	private Properties properties;

	private long timeout = Long.MAX_VALUE;

	private Integer threadsNum = 1;

	public void init() {
    	for (int i = 0; i < threadsNum; i++) {
			MessageConsumerThread item = new MessageConsumerThread();
			item.setProperties(properties);
			item.setErrorProcessor(errorProcessor);
			item.setErrorProcessorLock(errorProcessorLock);
			item.setMessageConusmer(this);
			item.setPrefix(prefix);
			item.setTimeout(timeout);
			item.setTopic(topic);
			Thread thread = new Thread(item);
			thread.start();
		}
	}

	protected void processErrorMessage(byte[] message) {
		if (!errorProcessorLock && null != errorProcessor) {
			errorProcessor.processErrorMessage(message, this);
		}
	}

	public void setTopic(String topic) {
		this.topic = topic.trim().replaceAll(" ", "");
	}

	public void setErrorProcessorLock(Boolean errorProcessorLock) {
		this.errorProcessorLock = errorProcessorLock;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void destroy() {
		if (null != errorProcessor) {
			errorProcessor.close();
		}
	}

	public void setErrorProcessor(ErrorMessageProcessor<byte[]> errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setThreadsNum(Integer threadsNum) {
		this.threadsNum = threadsNum;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
