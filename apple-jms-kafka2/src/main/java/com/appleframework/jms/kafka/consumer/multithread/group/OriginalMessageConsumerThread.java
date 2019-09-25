package com.appleframework.jms.kafka.consumer.multithread.group;

import java.time.Duration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;

/**
 * @author Cruise.Xu
 * 
 */
public class OriginalMessageConsumerThread implements Runnable {
		
	private static Logger logger = LoggerFactory.getLogger(OriginalMessageConsumerThread.class);
	
	private String topic;
	
	private String prefix = "";
    	
	private ErrorMessageProcessor<ConsumerRecord<String, byte[]>> errorProcessor;
	
	private AbstractMessageConusmer<ConsumerRecord<String, byte[]>> messageConusmer;
	
	private Boolean errorProcessorLock = true;
	
	private KafkaConsumer<String, byte[]> consumer;
	
	private AtomicBoolean closed = new AtomicBoolean(false);
    	
	private long timeout = Long.MAX_VALUE;
	
	private Properties properties;
	
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
        	consumer = new KafkaConsumer<String, byte[]>(properties);
     		consumer.subscribe(topicSet);
     		Duration duration = Duration.ofMillis(timeout);
        	while (!closed.get()) {
    			ConsumerRecords<String, byte[]> records = consumer.poll(duration);
    			for (ConsumerRecord<String, byte[]> record : records) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
					}
    				if(errorProcessorLock) {
    					messageConusmer.processMessage(record);
    				}
    				else {
    					try {
    						messageConusmer.processMessage(record);
    					} catch (Exception e) {
    						processErrorMessage(record);
    					}
    				}
    			}
             }
         } catch (WakeupException e) {
             if (!closed.get()) throw e;
         }
     }
	
	protected void processErrorMessage(ConsumerRecord<String, byte[]> message) {
		if(!errorProcessorLock && null != errorProcessor) {
			errorProcessor.processErrorMessage(message, messageConusmer);
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
		closed.set(true);
		consumer.wakeup();
		if (null != errorProcessor) {
			errorProcessor.close();
		}
	}

	public void commitSync() {
		consumer.commitSync();
	}
	
	public void commitAsync() {
		consumer.commitAsync();
	}

	public void setErrorProcessor(ErrorMessageProcessor<ConsumerRecord<String, byte[]>> errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setMessageConusmer(AbstractMessageConusmer<ConsumerRecord<String, byte[]>> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
