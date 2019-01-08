package com.appleframework.jms.eventbus.consumer;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.google.common.eventbus.Subscribe;

/**
 * @author Cruise.Xu
 */
public abstract class ObjectMessageConsumer extends AbstractMessageConusmer<Object> {

	@Subscribe  
    public void consume(Object value) {
		processMessage(value);
    }
	
}