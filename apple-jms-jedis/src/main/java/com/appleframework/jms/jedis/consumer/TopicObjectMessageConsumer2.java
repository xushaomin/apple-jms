package com.appleframework.jms.jedis.consumer;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.PoolFactory;
import com.appleframework.jms.core.consumer.MessageConusmer2;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
@SuppressWarnings("deprecation")
public class TopicObjectMessageConsumer2 {

	private static Logger logger = Logger.getLogger(TopicObjectMessageConsumer2.class);

	private MessageConusmer2<Object> messageConusmer2;
	
	protected PoolFactory poolFactory;

	protected String topic;
	
	private BinaryJedisPubSub pubSub = new BinaryJedisPubSub() {
		@Override
		public void onMessage(byte[] channel, byte[] message) {
			messageConusmer2.processMessage(ByteUtils.fromByte(message));
		}

		@Override
		public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
			messageConusmer2.processMessage(ByteUtils.fromByte(message));
		}

		@Override
		public void punsubscribe() {
			super.punsubscribe();
		}

		@Override
		public void punsubscribe(byte[]... patterns) {
			super.punsubscribe(patterns);
		}
	};

	protected void init() {
		String[] topics = topic.split(",");
		JedisPool jedisPool = poolFactory.getWritePool();
		Jedis jedis = jedisPool.getResource();
		try {
			for (int i = 0; i < topics.length; i++) {
				final String topicc = topics[i];
				jedis.psubscribe(pubSub, topicc.getBytes());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			jedisPool.returnResource(jedis);
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				unsubscribe();
			}
		}));
	}
	
	private void unsubscribe() {
		pubSub.unsubscribe();
	}

	public void setTopic(String topic) {
		this.topic = topic.trim().replaceAll(" ", "");
	}

	public void setPoolFactory(PoolFactory poolFactory) {
		this.poolFactory = poolFactory;
	}

	public void destroy() {
		unsubscribe();
	}

}
