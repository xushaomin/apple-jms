package com.appleframework.jms.jedis.consumer.master;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicBytesMessageConsumer2 extends TopicBaseMessageConsumer {

	private IMessageConusmer<byte[]> messageConusmer;

	public void setMessageConusmer2(IMessageConusmer<byte[]> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processMessage(byte[] message) {
		messageConusmer.onMessage(message);

	}

}
