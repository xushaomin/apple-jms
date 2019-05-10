package com.appleframework.jms.jedis.consumer.sentinel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.JedisSentinelFactory;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class TopicBaseMessageConsumer2 extends AbstractMessageConusmer<String> {

	private static Logger logger = Logger.getLogger(TopicBaseMessageConsumer2.class);

	private JedisSentinelFactory connectionFactory;

	protected String topic;
	
	protected String prefix = "";
	
	protected Long sleepMillis = 10L;
		
	private JedisPubSub pubSub = new JedisPubSub() {
		@Override
		public void onMessage(String channel, String message) {
			processMessage(message);
		}

		@Override
		public void onPMessage(String pattern, String channel,String message) {
			processMessage(message);
		}

		@Override
		public void punsubscribe() {
			super.punsubscribe();
		}

		@Override
		public void punsubscribe(String... patterns) {
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
					while (true) {
						Jedis jedis = null;
						try {
							jedis = connectionFactory.getJedisConnection();
							logger.warn("subscribe the topic ->" + topicc);
							jedis.subscribe(pubSub, topicc);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}  finally {
							if (jedis != null) {
								jedis.close();
							}
						}
						try {
							Thread.sleep(sleepMillis);
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

	public void setConnectionFactory(JedisSentinelFactory connectionFactory) {
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
