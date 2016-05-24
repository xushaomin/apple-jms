package com.appleframework.jms.core.consumer;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author xusm
 * 
 */
public abstract class JmsMessageConusmer extends MessageConusmer<Message> implements MessageListener {
	
	public void onMessage(Message message) {
		processMessage(message);
	}
	
}
