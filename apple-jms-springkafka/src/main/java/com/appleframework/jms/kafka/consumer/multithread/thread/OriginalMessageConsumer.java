package com.appleframework.jms.kafka.consumer.multithread.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;

import com.appleframework.jms.core.config.TraceConfig;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.thread.NamedThreadFactory;
import com.appleframework.jms.core.utils.ExecutorUtils;
import com.appleframework.jms.core.utils.UuidUtils;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer extends AbstractMessageConusmer<ConsumerRecord<String, byte[]>> {

	private static Logger logger = LoggerFactory.getLogger(OriginalMessageConsumer.class);
		
	private ExecutorService messageExecutor;
		
	protected Integer threadsNum;
	
	protected boolean flowControl = false;
	
	protected int flowCapacity = Integer.MAX_VALUE;
	
	private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();;
	
	@PostConstruct
	protected void init() {
    	if(null == threadsNum) {
    		threadsNum = 1;
    	}
    	if(null == messageExecutor) {
    		messageExecutor = ExecutorUtils.newFixedThreadPool(threadsNum, workQueue, 
    			new NamedThreadFactory("apple-jms-kafka-comsumer-pool"));
    	}
	}
	
	@KafkaListener(topics = "#{'${spring.kafka.consumer.topics}'.split(',')}", concurrency = "${spring.kafka.consumer.concurrency:1}")
	public void run(final ConsumerRecord<String, byte[]> record) {
		try {
			if(TraceConfig.isSwitchTrace()) {
				if(null != record.key()) {
					MDC.put(TraceConfig.getTraceIdKey(), record.key());
				}
				else {
					MDC.put(TraceConfig.getTraceIdKey(), UuidUtils.genUUID());
				}
			}
			if (flowControl) {
				while (true) {
					int queueSize = workQueue.size();
					if (queueSize >= flowCapacity) {
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
			messageExecutor.submit(new Runnable() {
				public void run() {
					if (logger.isDebugEnabled()) {
						logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(),
								record.value());
					}
					processMessage(record);
				}
			});
		} catch (WakeupException e) {
			throw e;
		}
	}
	
	public void destroy() {
		messageExecutor.shutdown();
		try {
			messageExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}

	public void commitSync() {
	}
	
	public void commitAsync() {
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
