package com.appleframework.jms.activemq.consumer;

import javax.jms.Message;
import javax.jms.MessageListener;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class OriginalMessageConsumer extends AbstractMessageConusmer<Message> implements MessageListener {

	@Override
	public void onMessage(Message message) {
		processMessage(message);	
	}
	
}
