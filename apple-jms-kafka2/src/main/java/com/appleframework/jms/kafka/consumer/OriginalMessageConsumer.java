package com.appleframework.jms.kafka.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(OriginalMessageConsumer.class);

	protected String topic;

	protected KafkaConsumer<String, byte[]> consumer;

	private AtomicBoolean closed = new AtomicBoolean(false);

	private long timeout = 100;

	protected void init() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			String[] topics = topic.split(",");
			consumer.subscribe(Arrays.asList(topics));
			while (!closed.get()) {
				ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(timeout));
				for (ConsumerRecord<String, byte[]> record : records) {
					logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
					processMessage(record);
				}
			}
		} catch (WakeupException e) {
			if (!closed.get())
				throw e;
		} finally {
			consumer.close();
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
	}

	public void commitSync() {
		consumer.commitSync();
	}
	
	public void commitAsync() {
		consumer.commitAsync();
	}
}
