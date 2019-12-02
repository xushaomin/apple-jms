package com.appleframework.jms.redis.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.core.thread.NamedThreadFactory;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class QueueBaseMessageConsumer extends AbstractMessageConusmer<Object> {

	private static Logger logger = LoggerFactory.getLogger(QueueBaseMessageConsumer.class);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Value("${spring.redis.consumer.queues}")
	protected String topic;
	
	protected Long sleepMillis = 10L;

	private boolean poolRunning = true;
	
	private void fetchMessage(String topic) {
		try {
			Object value = redisTemplate.opsForList().rightPop(topic);
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

	@PostConstruct
	protected void init() {
		String[] topics = topic.split(",");
		final ExecutorService executor = Executors.newFixedThreadPool(topics.length, 
				new NamedThreadFactory("apple-jms-redis-queue-cosnumer"));

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

	public void destroy() {
		poolRunning = false;
	}
	
	public void setSleepMillis(Long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
