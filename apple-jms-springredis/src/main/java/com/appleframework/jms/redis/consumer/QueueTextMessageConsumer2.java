package com.appleframework.jms.redis.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueTextMessageConsumer2 extends QueueBaseMessageConsumer {

	private IMessageConusmer<String> messageConusmer;

	public void setMessageConusmer(IMessageConusmer<String> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processMessage(Object message) {
		messageConusmer.onMessage(message.toString());
	}

}