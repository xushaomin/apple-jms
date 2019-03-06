package com.appleframework.jms.kafka.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.appleframework.jms.core.utils.ByteUtils;


/**
 * @author Cruise.Xu
 * 
 */
@Deprecated
public abstract class ObjectMessageConsumer extends BaseMessageConsumer implements IMessageConusmer<Object> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(ByteUtils.fromByte(message));
	}
	
	
}