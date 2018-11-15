package com.appleframework.jms.rocketmq;

import com.appleframework.jms.rocketmq.consumer.ObjectMessageConsumer;

/**
 * @author Cruise.Xu
 * 
 */
public class RocketMessageConsumerTest extends ObjectMessageConsumer {

	@Override
	public void onMessage(Object message) {
		System.out.println(message);
	}
	
}
