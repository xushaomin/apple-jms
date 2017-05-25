package com.appleframework.jms.core.consumer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class BytesMessageConusmer {
	
	public abstract void processByteMessage(byte[] message);

}
