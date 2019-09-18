package com.appleframework.jms.kafka.consumer.multithread.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {

	private static Logger logger = LoggerFactory.getLogger(OriginalMessageConsumer.class);

	protected String topic;

	private Properties properties;

	protected String prefix = "";

	private long timeout = Long.MAX_VALUE;

	protected Integer threadsNum = 1;

	private Boolean mixConsumer = true;
	
	private ExecutorService executor;
	
	private List<OriginalMessageConsumerThread> threadList = new ArrayList<>();

	public void init() {
		executor = Executors.newFixedThreadPool(threadsNum);
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
		threadList.add(item);
		executor.submit(item);
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
	
	public void destroy() {
		for (OriginalMessageConsumerThread thread : threadList) {
			thread.destroy();
		}
		executor.shutdown();
		try {
			executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}

}
