package com.appleframework.jms.core.consumer;

/**
 * @author cruise.xu
 * 
 */
public interface IMessageConusmer<Message> {
	
	public void processMessage(Message message);

}
