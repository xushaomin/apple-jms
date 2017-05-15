package com.appleframework.jms.jedis.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.appleframework.cache.jedis.factory.PoolFactory;
import com.appleframework.jms.core.consumer.MessageConusmer2;
import com.appleframework.jms.core.utils.ByteUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Cruise.Xu
 * 
 */
@SuppressWarnings("deprecation")
public class QueueObjectMessageConsumer2 {
	
	private static Logger logger = Logger.getLogger(QueueObjectMessageConsumer2.class);
	
	@Resource
	private MessageConusmer2<Object> messageConusmer2;
	
	@Resource
	private PoolFactory poolFactory;

	protected String topic;

	private boolean poolRunning = true;
		
	private void fetchMessage(String topic) {
		JedisPool jedisPool = poolFactory.getReadPool();
		Jedis jedis = jedisPool.getResource();
		try {
			byte[] value = jedis.rpop(topic.getBytes());
			if (null != value) {
				messageConusmer2.processMessage(ByteUtils.fromByte(value));
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
					if(poolRunning)
						fetchMessage(topicc);
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

	}
}
