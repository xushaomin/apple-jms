package com.appleframework.jms.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;

public class RocketMQPushConsumer extends DefaultMQPushConsumer {

	private String namesrvAddr;

	private String consumerGroup;
	
	private String consumeFromWhere;

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}
	
	public void setConsumeFromWhere(String consumeFromWhere) {
		this.consumeFromWhere = consumeFromWhere;
	}

	public void close() {
		this.shutdown();
	}

	public void init() {
		ConsumeFromWhere consumeFrom = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;
		if("CONSUME_FROM_FIRST_OFFSET".equals(consumeFromWhere)) {
			consumeFrom = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
		}
		super.setNamesrvAddr(namesrvAddr);
		super.setConsumerGroup(consumerGroup);
		this.setConsumeFromWhere(consumeFrom);
	}

}
