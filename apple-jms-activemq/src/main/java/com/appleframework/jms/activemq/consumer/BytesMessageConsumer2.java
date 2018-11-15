package com.appleframework.jms.activemq.consumer;

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
	public void processMessage(byte[] message) {
		try {
			messageConusmer.onMessage(message);
		} catch (Exception e) {
		}
	}

}