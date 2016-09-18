package com.work.common.enums;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具类
 * @author：wanghaibo 
 * @creattime：2016年9月18日 下午2:46:45 
 * 
 */  
public enum LocalThreadPool {
	
	CACHED,FIXED,SCHEDULED,SINGLE;
	
	//线程池大小
	private int nThreads = 30;
	public synchronized ExecutorService getThreadPool(){
		switch (this) {
		case CACHED:
			return Executors.newCachedThreadPool();
		case FIXED:
			return Executors.newFixedThreadPool(nThreads);
		case SCHEDULED:
			return Executors.newScheduledThreadPool(nThreads);
		case SINGLE:
			return Executors.newSingleThreadExecutor();
		default:
			return Executors.newCachedThreadPool(); 
		}
	}
	
	public int getnThreads() {
		return nThreads;
	}
	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	public static void main(String[] args) {
		System.out.println(FIXED.getnThreads());
		FIXED.setnThreads(6);
		System.out.println(FIXED.getnThreads());
		FIXED.setnThreads(7);
		System.out.println(FIXED.getnThreads());
		ExecutorService threadPool = LocalThreadPool.CACHED.getThreadPool();
		threadPool.submit(new Thread(){
			
		});
		threadPool.shutdown();
	}
}
