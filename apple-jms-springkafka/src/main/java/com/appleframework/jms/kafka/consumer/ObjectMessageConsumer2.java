package com.appleframework.jms.kafka.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public class ObjectMessageConsumer2 extends BaseMessageConsumer<byte[]> {

	private IMessageConusmer<Object> messageConusmer;

	public void setMessageConusmer(IMessageConusmer<Object> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processMessage(byte[] message) {
		messageConusmer.onMessage(ByteUtils.fromByte(message));		
	}

}
