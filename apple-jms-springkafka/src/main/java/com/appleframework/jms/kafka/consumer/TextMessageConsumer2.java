package com.appleframework.jms.kafka.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;


/**
 * @author Cruise.Xu
 * 
 */
public class TextMessageConsumer2 extends BaseMessageConsumer<String> {

	private IMessageConusmer<String> messageConusmer;

	public void setMessageConusmer2(IMessageConusmer<String> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processMessage(String message) {
		messageConusmer.onMessage(message);
	}

}