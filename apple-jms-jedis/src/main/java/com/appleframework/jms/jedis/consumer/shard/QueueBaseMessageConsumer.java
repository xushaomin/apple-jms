package com.appleframework.jms.jedis.consumer.shard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.cache.jedis.factory.JedisShardInfoFactory;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

import redis.clients.jedis.Jedis;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueBaseMessageConsumer extends AbstractMessageConusmer<byte[]> {

	private static Logger logger = LoggerFactory.getLogger(QueueBaseMessageConsumer.class);

	protected JedisShardInfoFactory connectionFactory;

	protected String topic;
	
	protected Long sleepMillis = 10L;

	private boolean poolRunning = true;
	
	private void fetchMessage(String topic) {
		Jedis jedis = connectionFactory.getJedisConnection();
		try {
			byte[] value = jedis.rpop(topic.getBytes());
			if (null != value) {
				processMessage(value);
			}
			else {
				Thread.sleep(sleepMillis);
			}
		} catch (Exception e) {
			logger.error("", e);
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

	public void setConnectionFactory(JedisShardInfoFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void destroy() {
		poolRunning = false;
	}

	public void setSleepMillis(Long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}
}
