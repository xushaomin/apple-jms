package com.appleframework.jms.jedis.consumer;

public class TopicMessageConsumerImpl extends TopicBytesMessageConsumer {

	@Override
	public void onMessage(byte[] message) {
		System.out.println(new String(message));
	}

}
