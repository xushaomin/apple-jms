package com.appleframework.jms.kafka.consumer.multithread.thread;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.appleframework.jms.kafka.consumer.BaseOriginalMessageConsumer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer<Message> extends BaseOriginalMessageConsumer<ConsumerRecord<Object, Message>> implements IMessageConusmer<ConsumerRecord<Object, Message>> {

	public void processMessage(ConsumerRecord<Object, Message> message) {
		onMessage(message);
	}
	
	
}