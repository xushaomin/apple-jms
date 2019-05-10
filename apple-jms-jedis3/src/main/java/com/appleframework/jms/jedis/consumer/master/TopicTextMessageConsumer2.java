package com.appleframework.jms.jedis.consumer.master;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicTextMessageConsumer2 extends TopicBaseMessageConsumer {

	private IMessageConusmer<String> messageConusmer;

	public void setMessageConusmer(IMessageConusmer<String> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processMessage(byte[] message) {
		messageConusmer.onMessage(new String(message));
	}

}