package com.appleframework.jms.jedis.consumer;

import com.appleframework.jms.jedis.consumer.QueueBytesMessageConsumer;

public class QueueMessageConsumerImpl extends QueueBytesMessageConsumer {

	@Override
	public void onMessage(byte[] message) {
		System.out.println(new String(message));
	}

}
