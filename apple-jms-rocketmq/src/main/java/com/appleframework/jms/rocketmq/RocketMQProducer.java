package com.appleframework.jms.rocketmq;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

public class RocketMQProducer extends DefaultMQProducer {
	
	private static Logger logger = Logger.getLogger(RocketMQProducer.class.getName());

	public void init() {
		try {
			start();
		} catch (MQClientException e) {
			logger.error(e.getMessage());
		}
	}

	public void close() {
		shutdown();
	}

}
