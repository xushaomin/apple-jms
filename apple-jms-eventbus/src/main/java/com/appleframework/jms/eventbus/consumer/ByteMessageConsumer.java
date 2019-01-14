package com.appleframework.jms.eventbus.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.google.common.eventbus.Subscribe;

/**
 * @author Cruise.Xu
 */
public abstract class ByteMessageConsumer implements IMessageConusmer<byte[]> {

	@Subscribe  
    public void consume(byte[] value) {
		onMessage(value);
    }

}
