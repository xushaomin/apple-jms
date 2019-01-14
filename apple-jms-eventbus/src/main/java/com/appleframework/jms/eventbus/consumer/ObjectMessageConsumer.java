package com.appleframework.jms.eventbus.consumer;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.google.common.eventbus.Subscribe;

/**
 * @author Cruise.Xu
 */
public abstract class ObjectMessageConsumer implements IMessageConusmer<Object> {

	@Subscribe  
    public void consume(Object value) {
		onMessage(value);
    }
	
}