package com.tairan.cloud.service.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 使用Fork/Join框架 ，计算1+2+3+4
 * http://ifeve.com/talk-concurrency-forkjoin/#more-8436
 * 
 * 介绍： 第一步分割任务。首先需要一个fork类来把大任务分割成子任务，有可能子任务还是很大，所以还需要不停的分割，直到分割出的子任务足够小；
 * 第二步执行任务并合并结果。分割的子任务分别放在双端队列里，然后几个启动线程分别从双端队列里获取任务执行。子任务执行完的结果
 * 都统一放在一个队列里，启动一个线程从队列里拿数据，然后合并这些数据。
 * 
 * Fork/Join使用两个类来完成以上两件事情
 * ForkJoinTask:我们要使用ForkJoin框架，必须首先创建一个ForkJoin任务。它提供在任务中执行fork()和Join操作的机制，通常
 * 情况下我们不需要直接继承ForkJoinTask类，而只需要继承它的子类，Fork/Join框架提供了以下两个子类：
 * RecursiveAction:用于没有返回结果的任务。 RecursiveTask:用于有返回结果的任务。
 * ForkJoinPool:ForkJoinTask需要通过ForkJoinPool来执行，任务分割出的子任务会添加到当前工作线程所维护的双端队列中，
 * 进入队列的头部。当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列尾部获取一个任务
 * 
 * @author hzmpj
 *
 */
public class CountTask extends RecursiveTask {
	private static final int THRESHOLD = 2;// 阀值
	private int start;
	private int end;

	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		// 如果任务足够小就计算任务
		boolean canCompute = (end - start) <= THRESHOLD;
		if (canCompute) {
			for (int i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			// 如果任务大于阀值，就分裂成两个子任务计算
			int middle = (start + end) / 2;
			CountTask leftTask = new CountTask(start, middle);
			CountTask rightTask = new CountTask(middle + 1, end);
			// 执行子任务
			leftTask.fork();
			rightTask.fork();
			// 等待子任务执行完，并得到其结果
			int leftResult = (int) leftTask.join();
			int rightResult = (int) rightTask.join();
			// 合并子任务
			sum = leftResult + rightResult;
		}
		return sum;
	}

	public static void main(String[] args) {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		// 生成一个计算任务，负责计算1+2+3+4
		CountTask task = new CountTask(1, 4);
		// 执行一个任务
		Future result = forkJoinPool.submit(task);
		/**
		 * Fork/Join框架的异常处理
		 * isCompletedAbnormally方法来检查任务是否已经抛出异常或已经被取消了。
		 * getException方法返回Throwable对象，如果任务被取消了则返回CancellationException.
		 * 如果任务没有完成或者没有抛出异常则返回null
		 */
		if (task.isCompletedAbnormally()) {
			System.out.println(task.getException());
		}
		try {
			System.out.println(result.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
