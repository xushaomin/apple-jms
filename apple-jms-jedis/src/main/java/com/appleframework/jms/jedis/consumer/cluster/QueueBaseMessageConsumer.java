package com.appleframework.jms.jedis.consumer.cluster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.JedisClusterFactory;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

import redis.clients.jedis.JedisCluster;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueBaseMessageConsumer extends AbstractMessageConusmer<byte[]> {

	private static Logger logger = Logger.getLogger(QueueBaseMessageConsumer.class);

	private JedisClusterFactory connectionFactory;

	protected String topic;
	
	protected String prefix = "";
	
	protected Long sleepMillis = 10L;

	private boolean poolRunning = true;
	
	private void fetchMessage(String topic) {
		JedisCluster jedis = connectionFactory.getClusterConnection();
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
		}
	}

	protected void init() {
		String[] topics = topic.split(",");
		final ExecutorService executor = Executors.newFixedThreadPool(topics.length);
		for (int i = 0; i < topics.length; i++) {
			final String topicc = prefix + topics[i];
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

	public void setConnectionFactory(JedisClusterFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void destroy() {
		poolRunning = false;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSleepMillis(Long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}

}
