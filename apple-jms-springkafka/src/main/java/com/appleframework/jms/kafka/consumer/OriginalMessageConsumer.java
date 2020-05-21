package com.appleframework.jms.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer<Message> extends BaseOriginalMessageConsumer<ConsumerRecord<Object, Message>> implements IMessageConusmer<ConsumerRecord<Object, Message>> {

	public void processMessage(ConsumerRecord<Object, Message> message) {
		onMessage(message);
	}
	
	
}