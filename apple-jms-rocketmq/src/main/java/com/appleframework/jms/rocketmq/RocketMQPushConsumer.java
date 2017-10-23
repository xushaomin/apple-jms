package com.appleframework.jms.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

public class RocketMQPushConsumer extends DefaultMQPushConsumer {

	private String consumeFrom;

	public void setConsumeFrom(String consumeFrom) {
		this.consumeFrom = consumeFrom;
	}

	public void close() {
		this.shutdown();
	}

	public void init() {
		ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;
		if("CONSUME_FROM_FIRST_OFFSET".equals(consumeFrom)) {
			consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
		}
		super.setConsumeFromWhere(consumeFromWhere);
	}

}
