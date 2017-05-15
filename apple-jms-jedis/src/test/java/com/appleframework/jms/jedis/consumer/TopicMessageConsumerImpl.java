package com.appleframework.jms.jedis.consumer;

public class TopicMessageConsumerImpl extends TopicBytesMessageConsumer {

	@Override
	public void processMessage(byte[] message) {
		System.out.println(new String(message));
	}

}
