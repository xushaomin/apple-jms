package com.appleframework.jms.jedis.consumer.cluster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.cache.jedis.factory.JedisClusterFactory;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.thread.NamedThreadFactory;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.JedisCluster;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TopicBaseMessageConsumer extends AbstractMessageConusmer<byte[]> {

	private static Logger logger = LoggerFactory.getLogger(TopicBaseMessageConsumer.class);

	private JedisClusterFactory connectionFactory;

	protected String topic;
	
	protected String prefix = "";
	
	protected Long sleepMillis = 10L;
		
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
		final ExecutorService executor = Executors.newFixedThreadPool(topics.length, new NamedThreadFactory("apple-jms-redis-topic-cosnumer"));
		for (int i = 0; i < topics.length; i++) {
			final String topicc = prefix + topics[i];
			logger.warn("subscribe the topic -> " + topicc);
			executor.submit(new Runnable() {
				@Override
				public void run() {
					while (true) {
						JedisCluster jedis = null;
						try {
							jedis = connectionFactory.getClusterConnection();
							logger.warn("subscribe the topic ->" + topicc);
							jedis.psubscribe(pubSub, topicc.getBytes());
						} catch (Exception e) {
							logger.error("", e);
						} finally {
							if (jedis != null) {
								jedis.close();
							}
						}
						try {
							Thread.sleep(sleepMillis);
							connectionFactory.init();
						} catch (Exception unused) {
						}
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

	public void setConnectionFactory(JedisClusterFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void destroy() {
		unsubscribe();
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setSleepMillis(Long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}

}
