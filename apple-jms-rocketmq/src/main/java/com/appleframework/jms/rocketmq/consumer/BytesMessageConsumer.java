package com.appleframework.jms.rocketmq.consumer;

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