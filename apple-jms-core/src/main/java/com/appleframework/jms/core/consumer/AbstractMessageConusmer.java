package com.appleframework.jms.core.consumer;

/**
 * @author cruise.xu
 * 
 */
public abstract class AbstractMessageConusmer<Message> {
	
	protected static final String KEY_TRACE_ID = "traceId";
	
	public abstract void processMessage(Message message);

}