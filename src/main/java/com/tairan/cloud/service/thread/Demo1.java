package com.tairan.cloud.service.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo1 {

	/**
	 * 
	 * 思路1： 创建10个线程
	 * 
	 * 将所有的url分成10份，每个线程查一份
	 */
	private void createTenThread() {
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName());
				}
			}, "thread" + i).start();
			;
		}
	}

	/**
	 * 思路2： 创建一个可缓存线程池， 如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
	 * 线程池为无限大，当执行第二个任务时第一个任务已经完成，会服用执行第一个任务的线程，而不用每次新建线程
	 * 
	 * @param args
	 */
	private void cachedThreadPool() {
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 1000; i++) {
			final int index = i;
			cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName());
				}
			});
		}
	}

	public static void main(String[] args) {
		Demo1 d = new Demo1();
		d.cachedThreadPool();
	}

}
