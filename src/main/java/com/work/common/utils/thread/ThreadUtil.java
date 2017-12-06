package com.work.common.utils.thread;

import java.util.concurrent.ExecutorService;


public class ThreadUtil {
	
	/**
	 * 当前线程安静入睡
	 * @param millis
	 */
	public static void sleepQuietly(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * 等待线程池终结
	 * @param threadPool
	 * @param millis
	 */
	public static void join(ExecutorService threadPool, long sleepMillis){
		while (true) {
			if (threadPool.isTerminated()) {
				break;
			}
			sleepQuietly(sleepMillis);
		}
	}
}
