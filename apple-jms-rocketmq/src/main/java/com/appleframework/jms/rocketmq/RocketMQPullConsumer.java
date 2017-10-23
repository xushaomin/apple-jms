package com.appleframework.jms.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;

public class RocketMQPullConsumer extends DefaultMQPullConsumer {

	public void close() {
		this.shutdown();
	}

}
