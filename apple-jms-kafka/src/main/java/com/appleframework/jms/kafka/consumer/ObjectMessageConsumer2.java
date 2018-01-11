package com.appleframework.jms.kafka.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public class ObjectMessageConsumer2 extends BaseMessageConsumer {

	private IMessageConusmer<Object> messageConusmer;

	public void setMessageConusmer(IMessageConusmer<Object> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processByteMessage(byte[] message) {
		messageConusmer.processMessage(ByteUtils.fromByte(message));
	}

}
