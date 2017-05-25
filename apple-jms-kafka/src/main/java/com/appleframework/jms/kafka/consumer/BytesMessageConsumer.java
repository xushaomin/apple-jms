package com.appleframework.jms.kafka.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class BytesMessageConsumer extends BaseMessageConsumer implements IMessageConusmer<byte[]> {

	@Override
	public void processByteMessage(byte[] message) {
		processMessage(message);
	}
	
}