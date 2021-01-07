package com.appleframework.jms.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.kafka.consumer.BytesMessageConsumer;

public class KafkaMessageConsumer extends BytesMessageConsumer {

	private static Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class.getName());

	@Override
	public void onMessage(byte[] message) {
		try {
			String object = new String(message);
			logger.error(object.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
