package com.appleframework.jms.jedis.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueTextMessageConsumer extends QueueBaseMessageConsumer implements IMessageConusmer<String> {

	@Override
	public void processByteMessage(byte[] message) {
		processMessage(new String(message));
		
	}

}
