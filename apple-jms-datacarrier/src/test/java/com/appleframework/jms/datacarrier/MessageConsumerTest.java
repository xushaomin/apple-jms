package com.appleframework.jms.datacarrier;

import com.appleframework.jms.datacarrier.consumer.ObjectMessageConsumer;

public class MessageConsumerTest extends ObjectMessageConsumer {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processMessage(Object message) {
		System.out.println("--->>>" + message);
		
	}

	
}
