package com.appleframework.jms.redis.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueObjectMessageConsumer2 extends QueueBaseMessageConsumer {

	private IMessageConusmer<Object> messageConusmer;

	public void setMessageConusmer2(IMessageConusmer<Object> messageConusmer2) {
		this.messageConusmer = messageConusmer2;
	}

	@Override
	public void processMessage(Object message) {
		messageConusmer.onMessage(message);
	}

}
