package com.appleframework.jms.kafka.consumer.multithread.group;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {

	protected String topic;

	private Properties properties;

	protected String prefix = "";

	private long timeout = Long.MAX_VALUE;

	protected Integer threadsNum = 1;

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
		item.setErrorProcessor(null);
		item.setErrorProcessorLock(false);
		item.setMessageConusmer(this);
		item.setPrefix(prefix);
		item.setTimeout(timeout);
		item.setTopic(topicc);
		Thread thread = new Thread(item);
		thread.start();
	}

	public void setTopic(String topic) {
		this.topic = topic.trim().replaceAll(" ", "");
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
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

	public void setMixConsumer(Boolean mixConsumer) {
		this.mixConsumer = mixConsumer;
	}

}
