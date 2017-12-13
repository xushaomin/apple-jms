package com.appleframework.jms.activemq.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;


/**
 * @author Cruise.Xu
 * 
 */
public class TextMessageConsumer2 extends BaseMessageConsumer {

	private IMessageConusmer<String> messageConusmer;

	public void setMessageConusmer2(IMessageConusmer<String> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processByteMessage(byte[] message) {
		try {
			messageConusmer.processMessage(new String(message));
		} catch (Exception e) {
		}
	}
}