package com.appleframework.jms.eventbus.consumer;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.google.common.eventbus.Subscribe;

/**
 * @author Cruise.Xu
 */
public abstract class ByteMessageConsumer extends AbstractMessageConusmer<byte[]> {

	@Subscribe  
    public void consume(byte[] value) {
		processMessage(value);
    }

}
