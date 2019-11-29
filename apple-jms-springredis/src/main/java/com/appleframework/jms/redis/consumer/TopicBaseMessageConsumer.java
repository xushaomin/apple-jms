package com.appleframework.jms.redis.consumer;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TopicBaseMessageConsumer extends AbstractMessageConusmer<byte[]> implements MessageListener {
		
	@Override
	public void onMessage(Message message, byte[] pattern) {
		processMessage(message.getBody());
	}

}
