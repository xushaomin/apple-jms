package com.appleframework.jms.jedis.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueObjectMessageConsumer extends QueueBaseMessageConsumer implements IMessageConusmer<Object> {

	@Override
	public void processByteMessage(byte[] message) {
		processMessage(ByteUtils.fromByte(message));
	}

}
