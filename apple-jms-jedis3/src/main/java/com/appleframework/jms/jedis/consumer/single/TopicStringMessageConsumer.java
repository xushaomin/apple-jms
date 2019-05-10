package com.appleframework.jms.jedis.consumer.single;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TopicStringMessageConsumer extends TopicBaseMessageConsumer2 implements IMessageConusmer<String> {

	@Override
	public void processMessage(String message) {
		onMessage(message);
	}

}
