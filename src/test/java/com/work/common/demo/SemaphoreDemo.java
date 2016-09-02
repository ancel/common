package com.work.common.demo;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.Executors;

import java.util.concurrent.Semaphore;
/**
 * Semaphore 通常用于限制可以访问某些资源（物理或逻辑的）的线程数目
 * @author admin
 *
 */
public class SemaphoreDemo {

	public static void main(String[] args) {
		// 线程池
		ExecutorService exec = Executors.newCachedThreadPool();
		//只能5个线程同时访问
		//true表示公平的，信号量保证对于任何调用获取信号量方法的线程而言按照FIFO来选择线程，获得许可。ps:默认为true
		//false表示不公平的，不对线程获取许可的顺序做任何保证
		final Semaphore semp = new Semaphore(5,false);
		// 模拟20个客户端访问
		for (int index = 0; index < 20; index++) {
			final int NO = index;
			Runnable run = new Runnable() {
				public void run() {
					try {
						// 获取许可
						semp.acquire();
						System.out.println("Accessing: " + NO);
						//Thread.sleep((long) (Math.random() * 10000));
						// 访问完后，释放
						semp.release();
						//打印还有多少信号量可用
						System.out.println("-----------------" + semp.availablePermits());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			exec.submit(run);
		}
		// 退出线程池
		exec.shutdown();
	}

}