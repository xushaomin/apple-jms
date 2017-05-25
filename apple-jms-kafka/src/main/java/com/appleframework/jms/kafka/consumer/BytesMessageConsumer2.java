package com.appleframework.jms.kafka.consumer;

import javax.annotation.Resource;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public class BytesMessageConsumer2 extends BaseMessageConsumer {

	@Resource
	private IMessageConusmer<byte[]> messageConusmer;

	public void setMessageConusmer(IMessageConusmer<byte[]> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	@Override
	public void processByteMessage(byte[] message) {
		messageConusmer.processMessage(message);

	}

}