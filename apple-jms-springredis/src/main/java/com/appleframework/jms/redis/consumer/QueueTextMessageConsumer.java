package com.appleframework.jms.redis.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueTextMessageConsumer extends QueueBaseMessageConsumer implements IMessageConusmer<String> {

	@Override
	public void processMessage(Object message) {
		onMessage(message.toString());
	}

}
