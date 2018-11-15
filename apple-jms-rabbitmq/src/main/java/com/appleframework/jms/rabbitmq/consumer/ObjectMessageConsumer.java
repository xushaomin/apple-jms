package com.appleframework.jms.rabbitmq.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class ObjectMessageConsumer extends BaseMessageConsumer implements IMessageConusmer<Object> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(ByteUtils.fromByte(message));
	}
	
}