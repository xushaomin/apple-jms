package com.appleframework.jms.core.consumer;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author xusm
 * 
 */
public abstract class JmsMessageConusmer2 implements MessageListener {
	
	private IMessageConusmer<Message> messageConusmer;
	
	public void setMessageConusmer(IMessageConusmer<Message> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	public void onMessage(Message message) {
		messageConusmer.processMessage(message);
	}
	
}
