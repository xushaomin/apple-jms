package com.appleframework.jms.datacarrier.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a.eye.datacarrier.consumer.IConsumer;
import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 */
public abstract class ObjectMessageConsumer implements IConsumer<Object>, IMessageConusmer<Object> {

	private static Logger logger = LoggerFactory.getLogger(ObjectMessageConsumer.class);

	@Override
	public void consume(List<Object> data) {
		for (Object value : data) {
			onMessage(value);
		}
	}

	@Override
	public void onError(List<Object> data, Throwable t) {
		logger.warn(t.getMessage());
		for (Object value : data) {
			onMessage(value);
		}
	}

	@Override
	public void onExit() {

	}

	public void destroy() {
	}

}
