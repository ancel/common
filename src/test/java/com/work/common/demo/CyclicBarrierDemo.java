package com.work.common.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 多线程并发辅助类
 * @author admin
 *
 */
public class CyclicBarrierDemo {
	private static int inc;
	private static AtomicInteger inc2 = new AtomicInteger(0);
	private static volatile int inc3;
	
	public static synchronized void increment(){
		inc++;
	}
	
	public static void increment2(){
		inc2.addAndGet(1);
	}
	
	public static void increment3(){
		inc3++;
	}
	
	public static void incThread(final CyclicBarrier cb,int number){
		for (int i = 0; i < number; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(100);
						increment();
						increment2();
						increment3();
						cb.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}).start();
		}
	}
	
	public static void main(String[] args) {
		int number = 100;
		CyclicBarrier cb = new CyclicBarrier(number, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println(inc);
				System.out.println(inc2.get());
				System.out.println(inc3);
			}
		});
		incThread(cb, number);
	}
}
