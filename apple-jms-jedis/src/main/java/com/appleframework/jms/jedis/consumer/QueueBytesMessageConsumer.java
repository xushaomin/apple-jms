package com.appleframework.jms.jedis.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueBytesMessageConsumer extends QueueBaseMessageConsumer implements IMessageConusmer<byte[]> {

	@Override
	public void processByteMessage(byte[] message) {
		processMessage(message);
	}
}