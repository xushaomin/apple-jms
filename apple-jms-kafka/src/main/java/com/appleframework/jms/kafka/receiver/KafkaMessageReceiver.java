package com.appleframework.jms.kafka.receiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.appleframework.jms.core.receiver.MessageReceiver;
import com.appleframework.jms.core.sender.MessageObject;
import com.appleframework.jms.core.utils.ByteUtils;

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
public abstract class KafkaMessageReceiver extends MessageReceiver<Serializable> {
	
	private static Logger logger = Logger.getLogger(KafkaMessageReceiver.class.getName());
	
	@Resource
	private ConsumerConfig consumerConfig;
	
	private String topic;
    
	private Integer partitionsNum;
	
	private ConsumerConnector connector;
			
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
		
	    //	    
		final ExecutorService executor = Executors.newFixedThreadPool(partitionsNum * topics.length);	    
	    for (final KafkaStream<byte[], byte[]> stream : streams) {
	    	executor.submit(new Runnable() {
				@SuppressWarnings("unchecked")
				public void run() {
					ConsumerIterator<byte[], byte[]> it = stream.iterator();
					while (it.hasNext()) {
						MessageAndMetadata<byte[], byte[]> item = it.next();
						String topic = item.topic();
						logger.info("topic=" + topic);
						MessageObject<Serializable> object = (MessageObject<Serializable>) ByteUtils.fromByte(item.message());
						logger.info("msgId=" + object.getMsgId());
						logger.info("trackId=" + object.getTrackId());
						processMessage(object.getObject());
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
		if(null != connector)
			connector.shutdown();
	}
}
