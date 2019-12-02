package com.appleframework.jms.redis.serialize;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.appleframework.jms.core.utils.ByteUtils;

public class DefaultObjectSerializer implements RedisSerializer<Object> {

	@Override
	public byte[] serialize(Object o) throws SerializationException {
		return ByteUtils.toBytes(o);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		return ByteUtils.fromByte(bytes);
	}
}