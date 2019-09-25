package com.appleframework.jms.kafka.consumer.multithread.thread;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;
import com.appleframework.jms.core.thread.NamedThreadFactory;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class RecordMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {

	private static Logger logger = LoggerFactory.getLogger(BaseMessageConsumer.class);

	protected String topic;
	
	protected String prefix = "";

	private ErrorMessageProcessor<ConsumerRecord<String, byte[]>> errorProcessor;

	protected Boolean errorProcessorLock = true;

	protected KafkaConsumer<String, byte[]> consumer;

	private AtomicBoolean closed = new AtomicBoolean(false);

	private long timeout = Long.MAX_VALUE;
	
	private ExecutorService executor;
	
	protected Integer threadsNum;
	
	protected void init() {
		try {
			String[] topics = topic.split(",");
			Set<String> topicSet = new HashSet<String>();
			for (String tp : topics) {
				String topicc = prefix + tp;
				topicSet.add(topicc);
				logger.warn("subscribe the topic -> " + topicc);
			}
			if (null == threadsNum) {
				threadsNum = topics.length;
			}
			executor = Executors.newFixedThreadPool(threadsNum, new NamedThreadFactory("apple-jms-kafka-comsumer-pool"));
			consumer.subscribe(topicSet);
			Duration duration = Duration.ofMillis(timeout);
			while (!closed.get()) {
				ConsumerRecords<String, byte[]> records = consumer.poll(duration);
				for (final ConsumerRecord<String, byte[]> record : records) {
					executor.submit(new Runnable() {
						public void run() {
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
						}
					});
				}
			}
		} catch (WakeupException e) {
			if (!closed.get())
				throw e;
		}
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

	public void setConsumer(KafkaConsumer<String, byte[]> consumer) {
		this.consumer = consumer;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void destroy() {
		closed.set(true);
		consumer.wakeup();
		if (null != errorProcessor) {
			errorProcessor.close();
		}
		executor.shutdown();
		try {
			executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}

	public void commit() {
		consumer.commitSync();
	}
	
	public void setThreadsNum(Integer threadsNum) {
		this.threadsNum = threadsNum;
	}
	
}
