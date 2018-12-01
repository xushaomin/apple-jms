package com.appleframework.jms.jedis.consumer.master;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		String[] topics = topic.split(",");
		final ExecutorService executor = Executors.newFixedThreadPool(topics.length);
		for (int i = 0; i < topics.length; i++) {
			final String topicc = prefix + topics[i];
			executor.submit(new Runnable() {
				@Override
				public void run() {
					try {
						JedisPool jedisPool = poolFactory.getWritePool();
						Jedis jedis = jedisPool.getResource();
						try {
							logger.warn("subscribe the topic ->" + topicc);
							jedis.psubscribe(pubSub, topicc.getBytes());
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					} catch (Exception e) {
						logger.error("Subscribing failed.", e);
					}
				}
			});
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
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
