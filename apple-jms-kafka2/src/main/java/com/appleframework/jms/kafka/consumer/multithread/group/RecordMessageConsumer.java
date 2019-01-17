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
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class RecordMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {
	
	private static Logger logger = LoggerFactory.getLogger(RecordMessageConsumer.class);
	
	protected String topic;
	
	protected String prefix = "";
	
	private Properties properties;

	private ErrorMessageProcessor<ConsumerRecord<String, byte[]>> errorProcessor;

	protected Boolean errorProcessorLock = true;
	
	private long timeout = Long.MAX_VALUE;
		
	protected Integer threadsNum;
	
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
		item.setErrorProcessor(errorProcessor);
		item.setErrorProcessorLock(errorProcessorLock);
		item.setMessageConusmer(this);
		item.setPrefix(prefix);
		item.setTimeout(timeout);
		item.setTopic(topicc);
		threadList.add(item);
		executor.submit(item);
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
		for (OriginalMessageConsumerThread thread : threadList) {
			thread.destroy();
		}
		executor.shutdown();
		try {
			executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void setThreadsNum(Integer threadsNum) {
		this.threadsNum = threadsNum;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
