package com.appleframework.jms.core.consumer;

/**
 * @author cruise.xu
 * 
 */
public interface IMessageConusmer<Message> {
	
	public void onMessage(Message message);

}
