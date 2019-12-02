package com.appleframework.jms.redis.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueObjectMessageConsumer extends QueueBaseMessageConsumer implements IMessageConusmer<Object> {

	@Override
	public void processMessage(Object message) {
		onMessage(message);
	}

}
