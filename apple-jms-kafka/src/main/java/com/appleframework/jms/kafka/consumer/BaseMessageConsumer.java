package com.appleframework.jms.kafka.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.appleframework.jms.core.consumer.BytesMessageConusmer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;


/**
 * @author Cruise.Xu
 * 
 */
public abstract class BaseMessageConsumer extends BytesMessageConusmer {
		
	@Resource
	private ConsumerConfig consumerConfig;
	
	protected String topic;
    
	protected Integer partitionsNum;
	
	private ConsumerConnector connector;
	
	private ExecutorService executor;
				
	protected void init() {
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();

		connector = Consumer.createJavaConsumerConnector(consumerConfig);
		
		String[] topics = topic.split(",");
		for (int i = 0; i < topics.length; i++) {
			// create 4 partitions of the stream for topic ¡°test-topic¡±, to allow 4 threads to consume
			// example: map.put("test-topic", 4);
			topicCountMap.put(topics[i], partitionsNum);
		}

		Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams 
			= connector.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = new ArrayList<KafkaStream<byte[], byte[]>>();
		for (int i = 0; i < topics.length; i++) {
			streams.addAll(topicMessageStreams.get(topics[i]));
		}
		
		// create list of 4 threads to consume from each of the partitions
		// ExecutorService executor = Executors.newFixedThreadPool(4);
		executor = Executors.newFixedThreadPool(partitionsNum * topics.length);
	    for (final KafkaStream<byte[], byte[]> stream : streams) {
	    	executor.submit(new Runnable() {
				public void run() {
                    ConsumerIterator<byte[], byte[]> it = stream.iterator();
					while (it.hasNext()) {
						byte[] message = it.next().message();
						processByteMessage(message);
					}
                }
            });
	    }
	    	    
	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	    	public void run() {
	    		executor.shutdown();
	    	}
	    }));
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
	
	public void destroy() {
		if (null != connector)
			connector.shutdown();
		if (null != executor)
			executor.shutdown();
	}
}
