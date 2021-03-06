package com.appleframework.jms.kafka.consumer;

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
import org.slf4j.MDC;

import com.appleframework.jms.core.config.TraceConfig;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;
import com.appleframework.jms.core.thread.NamedThreadFactory;
import com.appleframework.jms.core.utils.UuidUtils;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class BaseMessageConsumer extends AbstractMessageConusmer<byte[]> implements Runnable {
		
	private static Logger logger = LoggerFactory.getLogger(BaseMessageConsumer.class);
	
	protected String topic;
	
	protected String prefix = "";
    	
	private ErrorMessageProcessor<byte[]> errorProcessor;
	
	protected Boolean errorProcessorLock = true;
	
	protected KafkaConsumer<String, byte[]> consumer;
	
    private AtomicBoolean closed = new AtomicBoolean(false);
    	
	private long timeout = 100;
	
	private ExecutorService executor;
	
	protected void init() {
		executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("apple-jms-kafka-cosnumer"));
		executor.execute(this);
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
     		consumer.subscribe(topicSet);
     		Duration duration = Duration.ofMillis(timeout);
        	while (!closed.get()) {
    			ConsumerRecords<String, byte[]> records = consumer.poll(duration);
    			for (ConsumerRecord<String, byte[]> record : records) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
					}
    				if(TraceConfig.isSwitchTrace()) {
    					if(null != record.key()) {
    						MDC.put(TraceConfig.getTraceIdKey(), record.key());
    					}
    					else {
    						MDC.put(TraceConfig.getTraceIdKey(), UuidUtils.genUUID());
    					}
    				}
    				byte[] message = record.value();
    				if(errorProcessorLock) {
    					processMessage(message);
    				}
    				else {
    					try {
    						processMessage(message);
    					} catch (Exception e) {
    						processErrorMessage(message);
    					}
    				}
    			}
             }
         } catch (WakeupException e) {
             if (!closed.get()) throw e;
         }
     }
	
	protected void processErrorMessage(byte[] message) {
		if(!errorProcessorLock && null != errorProcessor) {
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

	public void commitSync() {
		consumer.commitSync();
	}
	
	public void commitAsync() {
		consumer.commitAsync();
	}

	public void setErrorProcessor(ErrorMessageProcessor<byte[]> errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
