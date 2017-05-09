package com.tairan.cloud.service.test;

/**
 * hzmpj 2017年5月9日
 */
public class FactorialTest {
	/**
	 * 
	 * 输入n， 求y1=1!+3!+...m!(m是小于等于n的最大奇数） y2=2!+4!+...p!(p是小于等于n的最大偶数)。
	 * 
	 * @param n
	 */
	public static void factorial(int n) {
		int oddNumberSum = 0;
		for (int i = 1; i <= n; i++) {
			if (i % 2 != 0) {
				int factorialResult = 0;
				for (int j = 1; j <= i; j++) {
					if (j == 1) {
						factorialResult = j;
					} else {
						factorialResult = factorialResult * j;
					}
				}
				oddNumberSum = factorialResult + oddNumberSum;
			}
		}
		System.out.println("奇数阶乘：" + oddNumberSum);
		
		int evenNumberSum = 0;
		for (int i = 1; i <= n; i++) {
			if (i % 2 == 0) {
				int factorialResult = 0;
				for (int j = 1; j <= i; j++) {
					if (j == 1) {
						factorialResult = j;
					} else {
						factorialResult = factorialResult * j;
					}
				}
				evenNumberSum = factorialResult+evenNumberSum;
			}
		}
		System.out.println("偶数阶乘：" + evenNumberSum);
	}
	
	public static void main(String[] args) {
		factorial(4);
	}
}
