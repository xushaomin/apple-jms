package com.appleframework.jms.jedis.consumer.cluster;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueBytesMessageConsumer extends QueueBaseMessageConsumer implements IMessageConusmer<byte[]> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(message);
	}
}