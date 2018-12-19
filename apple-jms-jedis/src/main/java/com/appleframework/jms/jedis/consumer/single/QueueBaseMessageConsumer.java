package com.appleframework.jms.jedis.consumer.single;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

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

	protected JedisPool jedisPool;

	protected String topic;

	private boolean poolRunning = true;
	
	protected Long sleepMillis = 10L;
	
	private void fetchMessage(String topic) {
		Jedis jedis = jedisPool.getResource();
		try {
			byte[] value = jedis.rpop(topic.getBytes());
			if (null != value) {
				processMessage(value);
			}
			else {
				Thread.sleep(sleepMillis);
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

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void destroy() {
		poolRunning = false;
	}
	
	public void setSleepMillis(Long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}

}
