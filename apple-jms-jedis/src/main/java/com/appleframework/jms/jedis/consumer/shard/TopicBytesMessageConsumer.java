package com.appleframework.jms.jedis.consumer.shard;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TopicBytesMessageConsumer extends TopicBaseMessageConsumer implements IMessageConusmer<byte[]> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(message);
	}	

}
