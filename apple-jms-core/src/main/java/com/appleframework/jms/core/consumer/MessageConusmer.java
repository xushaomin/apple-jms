package com.appleframework.jms.core.consumer;

/**
 * @author cruise.xu
 * 
 */
public abstract class MessageConusmer<T> {
	
	public abstract void processMessage(T message);

}
