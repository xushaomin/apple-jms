package com.appleframework.jms.jedis.consumer.shard;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public class QueueObjectMessageConsumer2 extends QueueBaseMessageConsumer {

	private IMessageConusmer<Object> messageConusmer;

	public void setMessageConusmer2(IMessageConusmer<Object> messageConusmer2) {
		this.messageConusmer = messageConusmer2;
	}

	@Override
	public void processMessage(byte[] message) {
		messageConusmer.onMessage(ByteUtils.toBytes(message));
	}

}
