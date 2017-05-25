package com.appleframework.jms.core.consumer;

/**
 * @author cruise.xu
 * 
 */
public abstract class AbstractMessageConusmer<Message> {
	
	public abstract void processMessage(Message message);

}
