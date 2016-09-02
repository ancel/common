package com.work.common.demo.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * 
 * 创建人：wanghaibo <br>
 * 创建时间：2015-3-14 下午6:45:05 <br>
 * 功能描述： <br>
 * CountDownLatch类位于java.util.concurrent包下，
 * 利用它可以实现类似计数器的功能。
 * 比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行
 * ps：不能重复使用
 * 版本： <br>
 * 版权拥有：wanghaibo <br>
 * ====================================== <br>
 * 修改记录 <br>
 * ====================================== <br>
 * 序号 姓名 日期 版本 简单描述 <br>
 * 
 */  
public class CountDownLatchTest {
	public static void main(String[] args) {
		final CountDownLatch latch = new CountDownLatch(2);
		new Thread(){
			public void run() {
				try {
					System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
					Thread.sleep(3000);
					System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			};
		}.start();
		
		
		new Thread(){
			public void run() {
				try {
					System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
					Thread.sleep(3000);
					System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		
		new Thread(){
			public void run() {
				try {
					System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
					Thread.sleep(3000);
					System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		
		try {
			System.out.println("等待2个子线程执行完毕...");
			latch.await();
			System.out.println("2个子线程已经执行完毕");
			System.out.println("继续执行主线程");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}