package com.appleframework.jms.rabbitmq.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class BytesMessageConsumer extends BaseMessageConsumer implements IMessageConusmer<byte[]> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(message);
	}
	
}