package com.appleframework.jms.rabbitmq.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TextMessageConsumer extends BaseMessageConsumer implements IMessageConusmer<String> {

	@Override
	public void processByteMessage(byte[] message) {
		processMessage(new String(message));
	}
	
}