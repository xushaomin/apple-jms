package com.appleframework.jms.jedis.consumer.cluster;

import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TopicTextMessageConsumer extends TopicBaseMessageConsumer implements IMessageConusmer<String> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(new String(message));
	}

}
