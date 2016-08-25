package com.appleframework.jms.core.receiver;

/**
 * @author cruise.xu
 * 
 */
public abstract class MessageReceiver<T> {
	
	public abstract void processMessage(T message);

}
