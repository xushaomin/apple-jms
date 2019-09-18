package com.appleframework.jms.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.kafka.consumer.multithread.group.BytesMessageConsumer;

public class KafkaMessageConsumer2 extends BytesMessageConsumer {

	private static Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer2.class.getName());
	
	@Override
	public void onMessage(byte[] message) {
		String object = new String(message);
		logger.error(object.toString());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//commitSync();
	}

}
