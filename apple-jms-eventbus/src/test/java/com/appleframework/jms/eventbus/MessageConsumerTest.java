package com.appleframework.jms.eventbus;

import com.appleframework.jms.eventbus.consumer.ObjectMessageConsumer;

public class MessageConsumerTest extends ObjectMessageConsumer {

	@Override
	public void onMessage(Object message) {
		System.out.println("--->>>" + message);
		
	}

	
}
