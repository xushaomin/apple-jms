package com.appleframework.jms.kafka;

import org.apache.log4j.Logger;

import com.appleframework.jms.kafka.consumer.BytesMessageConsumer;

public class KafkaMessageConsumer extends BytesMessageConsumer {

	private static Logger logger = Logger.getLogger(KafkaMessageConsumer.class.getName());

	
	@Override
	public void onMessage(byte[] message) {
		String object = new String(message);
		logger.error(object.toString());
		commitSync();
	}

}
