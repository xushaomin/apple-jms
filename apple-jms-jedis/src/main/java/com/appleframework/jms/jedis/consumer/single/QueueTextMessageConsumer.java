package com.appleframework.jms.jedis.consumer.single;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueTextMessageConsumer extends QueueBaseMessageConsumer implements IMessageConusmer<String> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(new String(message));
	}

}
