package com.appleframework.jms.redis.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueBytesMessageConsumer2 extends QueueBaseMessageConsumer {

	private IMessageConusmer<byte[]> messageConusmer;

	public void setMessageConusmer2(IMessageConusmer<byte[]> messageConusmer2) {
		this.messageConusmer = messageConusmer2;
	}

	@Override
	public void processMessage(byte[] message) {
		messageConusmer.onMessage(message);
	}

}
