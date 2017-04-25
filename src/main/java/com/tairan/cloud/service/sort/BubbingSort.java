package com.tairan.cloud.service.sort;

public class BubbingSort {

	public static void swap(int a, int b, int[] nums) {
		int temp = nums[a];
		nums[a] = nums[b];
		nums[b] = temp;
	}

	/**
	 * 冒泡排序
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int[] nums = { 1, 3, 10, 8, 5, 20, 16, 18, 2 };
		for (int i = 0; i < nums.length - 1; i++) {
			for (int j = 0; j < nums.length - i - 1; j++) {
				if (nums[j] > nums[j + 1]) {
					swap(j, j + 1, nums);
				}
			}
		}
		for (int i = 0; i < nums.length; i++) {
			System.out.println(nums[i]);
		}
	}

}
