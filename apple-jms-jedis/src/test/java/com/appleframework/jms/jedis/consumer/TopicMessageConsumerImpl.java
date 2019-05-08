package com.appleframework.jms.jedis.consumer;

import com.appleframework.jms.jedis.consumer.single.TopicBytesMessageConsumer;

public class TopicMessageConsumerImpl extends TopicBytesMessageConsumer {

	@Override
	public void onMessage(byte[] message) {
		System.out.println(new String(message));
	}

}
