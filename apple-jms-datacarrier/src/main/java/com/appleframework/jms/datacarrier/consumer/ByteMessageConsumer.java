package com.appleframework.jms.datacarrier.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a.eye.datacarrier.consumer.IConsumer;
import com.appleframework.jms.core.consumer.IMessageConusmer;

/**
 * @author Cruise.Xu
 */
public abstract class ByteMessageConsumer implements IConsumer<byte[]>, IMessageConusmer<byte[]> {

	private static Logger logger = LoggerFactory.getLogger(ByteMessageConsumer.class);

	@Override
	public void consume(List<byte[]> data) {
		for (byte[] value : data) {
			onMessage(value);
		}
	}

	@Override
	public void onError(List<byte[]> data, Throwable t) {
		logger.warn(t.getMessage());
		for (byte[] value : data) {
			onMessage(value);
		}
	}

	@Override
	public void onExit() {

	}

	public void destroy() {
	}

}
