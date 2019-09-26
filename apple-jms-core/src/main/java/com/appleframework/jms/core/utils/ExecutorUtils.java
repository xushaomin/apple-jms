package com.appleframework.jms.core.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {

	public static ThreadPoolExecutor newFixedThreadPool(int nThreads, Integer queueCapacity, ThreadFactory threadFactory) {
		BlockingQueue<Runnable> workQueue = null;
		if (null == queueCapacity || queueCapacity <= 0) {
			workQueue = new LinkedBlockingQueue<Runnable>();
		} else {
			workQueue = new LinkedBlockingQueue<Runnable>(queueCapacity);
		}
		if(null == threadFactory) {
			return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, workQueue);
		}
		else {
			return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, workQueue, threadFactory);
		}
	}
	
	public static ThreadPoolExecutor newFixedThreadPool(int nThreads, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		if(null == threadFactory) {
			return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, workQueue);
		}
		else {
			return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, workQueue, threadFactory);
		}
	}
}
