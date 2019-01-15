package com.appleframework.jms.kafka.consumer.multithread.group;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class RecordMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {
	
	protected String topic;
	
	protected String prefix = "";
	
	private Properties properties;

	private ErrorMessageProcessor<ConsumerRecord<String, byte[]>> errorProcessor;

	protected Boolean errorProcessorLock = true;
	
	private long timeout = Long.MAX_VALUE;
		
	protected Integer threadsNum;
	
	private Boolean mixConsumer = true;

	public void init() {
		if (mixConsumer) {
			for (int i = 0; i < threadsNum; i++) {
				startThread(topic);
			}
		} else {
			String[] topics = topic.split(",");
			for (String tp : topics) {
				for (int i = 0; i < threadsNum; i++) {
					startThread(tp);
				}
			}
		}
	}
	
	private void startThread(String topicc) {
		OriginalMessageConsumerThread item = new OriginalMessageConsumerThread();
		item.setProperties(properties);
		item.setErrorProcessor(errorProcessor);
		item.setErrorProcessorLock(errorProcessorLock);
		item.setMessageConusmer(this);
		item.setPrefix(prefix);
		item.setTimeout(timeout);
		item.setTopic(topicc);
		Thread thread = new Thread(item);
		thread.start();
	}

	protected void processErrorMessage(ConsumerRecord<String, byte[]> message) {
		if (!errorProcessorLock) {
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
	
	public void setThreadsNum(Integer threadsNum) {
		this.threadsNum = threadsNum;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
