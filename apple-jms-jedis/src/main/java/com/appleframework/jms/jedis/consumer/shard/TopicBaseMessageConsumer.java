package com.appleframework.jms.jedis.consumer.shard;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.PoolFactory;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
@SuppressWarnings("deprecation")
public abstract class TopicBaseMessageConsumer extends AbstractMessageConusmer<byte[]> {

	private static Logger logger = Logger.getLogger(TopicBaseMessageConsumer.class);

	protected PoolFactory poolFactory;

	protected String topic;
	
	protected String prefix = "";
		
	private BinaryJedisPubSub pubSub = new BinaryJedisPubSub() {
		@Override
		public void onMessage(byte[] channel, byte[] message) {
			processMessage(message);
		}

		@Override
		public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
			processMessage(message);
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String[] topics = topic.split(",");
					JedisPool jedisPool = poolFactory.getWritePool();
					Jedis jedis = jedisPool.getResource();
					try {
						for (int i = 0; i < topics.length; i++) {
							final String topicc = prefix + topics[i];
							logger.warn("subscribe the topic :" + topicc);
							jedis.psubscribe(pubSub, topicc.getBytes());
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					} finally {
						jedisPool.returnResource(jedis);
					}
				} catch (Exception e) {
					logger.error("Subscribing failed.", e);
				}
			}
		}).start();
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
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
