package com.appleframework.jms.kafka.consumer.multithread.thread;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.appleframework.jms.core.config.TraceConfig;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.thread.NamedThreadFactory;
import com.appleframework.jms.core.utils.ExecutorUtils;
import com.appleframework.jms.core.utils.UuidUtils;

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
	
	private ExecutorService messageExecutor;
	
	private ExecutorService mainExecutor;
	
	protected Integer threadsNum;
	
	protected boolean flowControl = false;
	
	protected Integer flowCapacity = Integer.MAX_VALUE;
		
	protected void init() {
		mainExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory("apple-jms-kafka-comsumer-main"));
		mainExecutor.execute(this);
	}

	@Override
	public void run() {
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
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        	messageExecutor = ExecutorUtils.newFixedThreadPool(threadsNum, workQueue, new NamedThreadFactory("apple-jms-kafka-comsumer-pool"));
			consumer.subscribe(topicSet);
			Duration duration = Duration.ofMillis(timeout);
			while (!closed.get()) {
				if (flowControl) {
					while (true) {
						if (workQueue.size() >= flowCapacity) {
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								logger.error("", e);
							}
						} else {
							break;
						}
					}
				}
				ConsumerRecords<String, byte[]> records = consumer.poll(duration);
				for (final ConsumerRecord<String, byte[]> record : records) {
					if(TraceConfig.isSwitchTrace()) {
						if(null != record.key()) {
							MDC.put(TraceConfig.getTraceIdKey(), record.key());
						}
						else {
							MDC.put(TraceConfig.getTraceIdKey(), UuidUtils.genUUID());
						}
					}
					messageExecutor.submit(new Runnable() {
						public void run() {
							if (logger.isDebugEnabled()) {
		    					logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
							}
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
		messageExecutor.shutdown();
		try {
			messageExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		mainExecutor.shutdown();
		try {
			mainExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("", e);
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
	
	public void setFlowControl(boolean flowControl) {
		this.flowControl = flowControl;
	}

	public void setFlowCapacity(Integer flowCapacity) {
		this.flowCapacity = flowCapacity;
	}
	
}
