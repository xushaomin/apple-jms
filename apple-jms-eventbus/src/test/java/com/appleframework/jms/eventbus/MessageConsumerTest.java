package com.appleframework.jms.eventbus;

import com.appleframework.jms.eventbus.consumer.ObjectMessageConsumer;

public class MessageConsumerTest extends ObjectMessageConsumer {

	@Override
	public void processMessage(Object message) {
		System.out.println("--->>>" + message);
		
	}

	
}
