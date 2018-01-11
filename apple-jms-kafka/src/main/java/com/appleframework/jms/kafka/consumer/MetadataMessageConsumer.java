package com.appleframework.jms.kafka.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;


/**
 * @author Cruise.Xu
 * 
 */
@SuppressWarnings("deprecation")
public abstract class MetadataMessageConsumer extends AbstractMessageConusmer<MessageAndMetadata<byte[], byte[]>> {
		
	@Resource
	private ConsumerConfig consumerConfig;
	
	protected String topic;
    
	protected Integer partitionsNum;
	
	protected Integer errorProcessorPoolSize = 1;
	
	private ConsumerConnector connector;
	
	private ExecutorService executor;
	
	private ErrorMetadataMessageProcessor errorProcessor;
	
	protected Boolean errorProcessorLock = true;
			
	protected void init() {
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();

		connector = Consumer.createJavaConsumerConnector(consumerConfig);
		
		String[] topics = topic.split(",");
		for (int i = 0; i < topics.length; i++) {
			topicCountMap.put(topics[i], partitionsNum);
		}

		Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams 
			= connector.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = new ArrayList<KafkaStream<byte[], byte[]>>();
		for (int i = 0; i < topics.length; i++) {
			streams.addAll(topicMessageStreams.get(topics[i]));
		}

		executor = Executors.newFixedThreadPool(partitionsNum * topics.length);
	    for (final KafkaStream<byte[], byte[]> stream : streams) {
	    	executor.submit(new Runnable() {
				public void run() {
                    ConsumerIterator<byte[], byte[]> it = stream.iterator();
					while (it.hasNext()) {
						MessageAndMetadata<byte[], byte[]> message = it.next();
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
            });
	    }
	    if(!errorProcessorLock) {
		    errorProcessor = new ErrorMetadataMessageProcessor(errorProcessorPoolSize);
	    }
	}
	
	private void processErrorMessage(MessageAndMetadata<byte[], byte[]> message) {
		if(!errorProcessorLock) {
			errorProcessor.submit(message, this);
		}
	}

	public void setConsumerConfig(ConsumerConfig consumerConfig) {
		this.consumerConfig = consumerConfig;
	}

	public void setTopic(String topic) {
		this.topic = topic.trim().replaceAll(" ", "");
	}

	public void setPartitionsNum(Integer partitionsNum) {
		this.partitionsNum = partitionsNum;
	}
	
	public void setErrorProcessorPoolSize(Integer errorProcessorPoolSize) {
		this.errorProcessorPoolSize = errorProcessorPoolSize;
	}
	
	public void setErrorProcessorLock(Boolean errorProcessorLock) {
		this.errorProcessorLock = errorProcessorLock;
	}

	public void destroy() {
		if(null != connector) {
			connector.shutdown();
		}
		if (null != executor) {
			executor.shutdown();
		}
		if (null != errorProcessor) {
			errorProcessor.close();
		}
	}
	
}
