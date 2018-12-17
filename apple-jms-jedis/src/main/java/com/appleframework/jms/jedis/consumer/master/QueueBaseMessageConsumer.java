package com.appleframework.jms.jedis.consumer.master;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.PoolFactory;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
@SuppressWarnings("deprecation")
public abstract class QueueBaseMessageConsumer extends AbstractMessageConusmer<byte[]> {

	private static Logger logger = Logger.getLogger(QueueBaseMessageConsumer.class);

	protected PoolFactory poolFactory;

	protected String topic;

	private boolean poolRunning = true;
	
	private void fetchMessage(String topic) {
		JedisPool jedisPool = poolFactory.getReadPool();
		Jedis jedis = jedisPool.getResource();
		try {
			byte[] value = jedis.rpop(topic.getBytes());
			if (null != value) {
				processMessage(value);
			}
			else {
				Thread.sleep(10L);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	protected void init() {
		String[] topics = topic.split(",");
		final ExecutorService executor = Executors.newFixedThreadPool(topics.length);

		for (int i = 0; i < topics.length; i++) {
			final String topicc = topics[i];
			executor.submit(new Runnable() {
				@Override
				public void run() {
					while(poolRunning) {
						fetchMessage(topicc);
					}
				}
			});
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				executor.shutdown();
			}
		}));
	}

	public void setTopic(String topic) {
		this.topic = topic.trim().replaceAll(" ", "");
	}

	public void setPoolFactory(PoolFactory poolFactory) {
		this.poolFactory = poolFactory;
	}

	public void destroy() {
		poolRunning = false;
	}

}
