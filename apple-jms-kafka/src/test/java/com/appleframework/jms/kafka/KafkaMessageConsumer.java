package com.appleframework.jms.kafka;

import org.apache.log4j.Logger;

import com.appleframework.jms.kafka.consumer.TextMessageConsumer;

public class KafkaMessageConsumer extends TextMessageConsumer {

	private static Logger logger = Logger.getLogger(KafkaMessageConsumer.class.getName());

	@Override
	public void processMessage(String message) {
		logger.error(message);
	}

}
