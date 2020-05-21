package com.appleframework.jms.kafka.consumer.multithread.group;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class RecordMessageConsumer<Message> extends BaseRecordMessageConsumer<Message> implements IMessageConusmer<ConsumerRecord<Object, Message>> {

	@Override
	public void processMessage(ConsumerRecord<Object, Message> message) {
		onMessage(message);
	}
	
	
}