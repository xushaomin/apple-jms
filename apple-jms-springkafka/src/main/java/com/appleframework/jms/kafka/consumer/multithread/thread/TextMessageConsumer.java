package com.appleframework.jms.kafka.consumer.multithread.thread;

import com.appleframework.jms.core.consumer.IMessageConusmer;


/**
 * @author Cruise.Xu
 * 
 */
public abstract class TextMessageConsumer extends BaseMessageConsumer<String> implements IMessageConusmer<String> {

	@Override
	public void processMessage(String message) {
		onMessage(new String(message));
	}
	
}
