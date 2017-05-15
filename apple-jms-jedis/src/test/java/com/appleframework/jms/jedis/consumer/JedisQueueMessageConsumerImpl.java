package com.appleframework.jms.jedis.consumer;

import com.appleframework.jms.jedis.consumer.BytesQueueMessageConsumer;

public class JedisQueueMessageConsumerImpl extends BytesQueueMessageConsumer {

	@Override
	public void processMessage(byte[] message) {
		System.out.println(new String(message));
	}

}
