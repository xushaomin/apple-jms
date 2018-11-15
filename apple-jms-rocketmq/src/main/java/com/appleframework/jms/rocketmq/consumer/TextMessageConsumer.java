package com.appleframework.jms.rocketmq.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TextMessageConsumer extends BaseMessageConsumer implements IMessageConusmer<String> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(new String(message));
	}
	
}