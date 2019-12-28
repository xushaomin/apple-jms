package com.appleframework.jms.kafka.consumer.multithread.group;

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
import org.slf4j.MDC;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.consumer.ErrorMessageProcessor;
import com.appleframework.jms.core.utils.Contants;
import com.appleframework.jms.core.utils.UuidUtils;

/**
 * @author Cruise.Xu
 * 
 */
public class MessageConsumerThread implements Runnable {
		
	private static Logger logger = LoggerFactory.getLogger(MessageConsumerThread.class);
	
	private String topic;
	
	private String prefix = "";
    	
	private ErrorMessageProcessor<byte[]> errorProcessor;
	
	private AbstractMessageConusmer<byte[]> messageConusmer;
	
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
        	while (!closed.get()) {
    			ConsumerRecords<String, byte[]> records = consumer.poll(timeout);
    			for (ConsumerRecord<String, byte[]> record : records) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
					}
    				if(null != record.key()) {
						MDC.put(Contants.KEY_TRACE_ID, record.key());
					}
					else {
						MDC.put(Contants.KEY_TRACE_ID, UuidUtils.genUUID());
					}
    				byte[] message = record.value();
    				if(errorProcessorLock) {
    					messageConusmer.processMessage(message);
    				}
    				else {
    					try {
    						messageConusmer.processMessage(message);
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

	public void setErrorProcessor(ErrorMessageProcessor<byte[]> errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setMessageConusmer(AbstractMessageConusmer<byte[]> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
