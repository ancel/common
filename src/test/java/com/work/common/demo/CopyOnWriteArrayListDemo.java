package com.work.common.demo;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * CopyOnWriteArrayList即将读写分离，修改数据时会拷贝一份list出来，每个线程都独立维护自己的list，修改完毕之后将原先的引用指向已修改的list；
 * 这样可以防止多线程访问list时，某个线程正在遍历数据，某个线程在改数据的并发情况的发生。
 * 适用于数据频繁遍历但是很少被修改的情况
 * @author admin
 *
 */
public class CopyOnWriteArrayListDemo {
	private static final CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<Integer>();

	public static void main(String... args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			cowList.add(i);
		}

		new Thread() {
			@Override
			public void run() {
				System.out.println("---------begin--------");

				Iterator<Integer> it = cowList.iterator();
				while (it.hasNext()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(it.next());
				}
				System.out.println("---------end--------");
			};
		}.start();

		//清楚list数据并不会对正在进行的遍历造成影响
		new Thread() {
			@Override
			public void run() {
				cowList.clear();
			};
		}.start();
	}

}
