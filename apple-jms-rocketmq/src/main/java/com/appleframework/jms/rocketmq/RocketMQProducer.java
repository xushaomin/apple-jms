package com.appleframework.jms.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketMQProducer extends DefaultMQProducer {
	
	private static Logger logger = LoggerFactory.getLogger(RocketMQProducer.class.getName());

	public void init() {
		try {
			start();
		} catch (MQClientException e) {
			logger.error("", e);
		}
	}

	public void close() {
		shutdown();
	}

}
