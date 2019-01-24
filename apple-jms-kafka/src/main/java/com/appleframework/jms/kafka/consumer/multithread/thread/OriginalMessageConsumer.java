package com.appleframework.jms.kafka.consumer.multithread.thread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.kafka.utils.ExecutorUtils;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(OriginalMessageConsumer.class);

	protected String topic;
	
	protected String prefix = "";

	protected KafkaConsumer<String, byte[]> consumer;

	private AtomicBoolean closed = new AtomicBoolean(false);

	private long timeout = Long.MAX_VALUE;
	
	private ExecutorService executor;
	
	protected Integer threadsNum;
	
	protected Integer queueCapacity;

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
			executor = ExecutorUtils.newFixedThreadPool(threadsNum, queueCapacity);
			consumer.subscribe(topicSet);
			while (!closed.get()) {
				ConsumerRecords<String, byte[]> records = consumer.poll(timeout);
				for (final ConsumerRecord<String, byte[]> record : records) {
					executor.submit(new Runnable() {
						public void run() {
							logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
							processMessage(record);
						}
					});
				}
			}
		} catch (WakeupException e) {
			if (!closed.get())
				throw e;
		}
	}

	public void setTopic(String topic) {
		this.topic = topic.trim().replaceAll(" ", "");
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
		executor.shutdown();
		try {
			executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}

	public void commitSync() {
		consumer.commitSync();
	}
	
	public void commitAsync() {
		consumer.commitAsync();
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setThreadsNum(Integer threadsNum) {
		this.threadsNum = threadsNum;
	}
	
	public void setQueueCapacity(Integer queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
}
