package com.appleframework.jms.kafka.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public class ObjectMessageConsumer2 extends BaseMessageConsumer<Object> {

	private IMessageConusmer<Object> messageConusmer;

	public void setMessageConusmer(IMessageConusmer<Object> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processMessage(Object message) {
		messageConusmer.onMessage(message);		
	}

}
