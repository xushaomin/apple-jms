package com.appleframework.jms.jedis.consumer.sentinel;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public class TopicStringMessageConsumer2 extends TopicBaseMessageConsumer2 {

	private IMessageConusmer<String> messageConusmer;

	public void setMessageConusmer(IMessageConusmer<String> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processMessage(String message) {
		messageConusmer.onMessage(message);
	}

}