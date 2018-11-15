package com.appleframework.jms.rabbitmq;

import com.appleframework.jms.rabbitmq.consumer.TextMessageConsumer;

/**
 * @author Cruise.Xu
 * 
 */
public class RabbitMessageConsumerTest extends TextMessageConsumer {

	@Override
	public void onMessage(String message) {
		System.out.println(message);
	}
	
}
