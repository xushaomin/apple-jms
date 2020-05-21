package com.appleframework.jms.kafka.consumer.multithread.thread;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class ObjectMessageConsumer extends BaseMessageConsumer<Object> implements IMessageConusmer<Object> {

	@Override
	public void processMessage(Object message) {
		onMessage(message);
	}
	
	
}