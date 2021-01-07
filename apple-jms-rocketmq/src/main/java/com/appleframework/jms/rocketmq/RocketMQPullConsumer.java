package com.appleframework.jms.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;

@SuppressWarnings("deprecation")
public class RocketMQPullConsumer extends DefaultMQPullConsumer {

	public void close() {
		this.shutdown();
	}

}
