package com.appleframework.jms.kafka.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {

	public static ThreadPoolExecutor newFixedThreadPool(int nThreads, Integer queueCapacity) {
		BlockingQueue<Runnable> workQueue = null;
		if (null == queueCapacity || queueCapacity <= 0) {
			workQueue = new LinkedBlockingQueue<Runnable>();
		} else {
			workQueue = new LinkedBlockingQueue<Runnable>(queueCapacity);
		}
		return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, workQueue);
	}
}
