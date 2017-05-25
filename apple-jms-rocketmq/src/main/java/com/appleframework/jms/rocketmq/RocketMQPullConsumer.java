package com.appleframework.jms.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;

public class RocketMQPullConsumer extends DefaultMQPullConsumer {

	public void close() {
		this.shutdown();
	}

}
