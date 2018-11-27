package com.appleframework.jms.core.consumer;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.thread.StandardThreadExecutor.StandardThreadFactory;

/**
 * 消费者端处理错误消息重试处理器
 * 
 * @description <br>
 * @date 2016年10月25日
 */
public class ErrorByteMessageProcessor implements Closeable, ErrorMessageProcessor<byte[]> {

	private static final Logger logger = LoggerFactory.getLogger(ErrorByteMessageProcessor.class);

	// 重试时间间隔单元（毫秒）
	private static final long RETRY_PERIOD_UNIT = 15 * 1000;

	private final PriorityBlockingQueue<PriorityTask> taskQueue = new PriorityBlockingQueue<PriorityTask>(1000);

	private ExecutorService executor;

	private AtomicBoolean closed = new AtomicBoolean(false);

	public ErrorByteMessageProcessor() {
		this(1);
	}

	public ErrorByteMessageProcessor(int poolSize) {
		executor = Executors.newFixedThreadPool(poolSize, new StandardThreadFactory("ErrorByteMessageProcessor"));
		executor.submit(new Runnable() {
			@Override
			public void run() {
				while (!closed.get()) {
					try {
						PriorityTask task = taskQueue.take();
						// 空任务跳出循环
						if (null == task.getMessage()) {
							break;
						}
						if (task.nextFireTime - System.currentTimeMillis() > 0) {
							TimeUnit.MILLISECONDS.sleep(1000);
							taskQueue.put(task);
							continue;
						}
						task.run();
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
		});
	}

	@Override
	public void processErrorMessage(byte[] message, AbstractMessageConusmer<byte[]> messageConusmer) {
		int taskCount;
		if ((taskCount = taskQueue.size()) > 1000) {
			logger.warn("ErrorByteMessageProcessor queue task count over:" + taskCount);
		}
		taskQueue.add(new PriorityTask(message, messageConusmer));
	}

	public void close() {
		closed.set(true);
		// taskQueue里面没有任务会一直阻塞，所以先add一个新任务保证执行
		taskQueue.add(new PriorityTask(null, null));
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
		executor.shutdown();
		logger.info("ErrorByteMessageProcessor closed");
	}

	class PriorityTask implements Runnable, Comparable<PriorityTask> {

		final byte[] message;
		final AbstractMessageConusmer<byte[]> messageConusmer;

		int retryCount = 0;
		long nextFireTime;

		public PriorityTask(byte[] message, AbstractMessageConusmer<byte[]> messageConusmer) {
			this(message, messageConusmer, System.currentTimeMillis() + RETRY_PERIOD_UNIT);
		}

		public PriorityTask(byte[] message, AbstractMessageConusmer<byte[]> messageConusmer, long nextFireTime) {
			super();
			this.message = message;
			this.messageConusmer = messageConusmer;
			this.nextFireTime = nextFireTime;
		}

		public byte[] getMessage() {
			return message;
		}

		@Override
		public void run() {
			try {
				messageConusmer.processMessage(message);
			} catch (Exception e) {
				retryCount++;
				retry();
			}
		}

		private void retry() {
			if (retryCount == 3) {
				logger.warn("retry_skip ,skip!!!");
				return;
			}
			nextFireTime = nextFireTime + retryCount * RETRY_PERIOD_UNIT;
			// 重新放入任务队列
			taskQueue.add(this);
		}

		@Override
		public int compareTo(PriorityTask o) {
			return (int) (this.nextFireTime - o.nextFireTime);
		}

	}
	
}
