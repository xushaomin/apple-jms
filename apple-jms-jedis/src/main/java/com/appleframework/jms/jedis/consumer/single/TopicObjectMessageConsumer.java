package com.appleframework.jms.jedis.consumer.single;

import com.appleframework.jms.core.consumer.IMessageConusmer;
import com.appleframework.jms.core.utils.ByteUtils;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TopicObjectMessageConsumer<T> extends TopicBaseMessageConsumer implements IMessageConusmer<Object> {

	@Override
	public void processMessage(byte[] message) {
		onMessage(ByteUtils.fromByte(message));
	}

}
