package com.tairan.cloud.service.clone;

import org.junit.Assert;
import org.junit.Test;

/**
 * hzmpj 2017年7月24日
 */
public class TestClone {
	@Test
	public void test1() throws CloneNotSupportedException {
		// user
		User user = new User();
		user.name = "user";
		user.age = 20;
		// copy
		User copy = (User) user.clone();
		// age因为是primitive,所以copy和原型是相等且独立的
		Assert.assertEquals(copy.age, user.age);
		copy.age = 30;
		// 改变copy不影响原型
		Assert.assertTrue(copy.age != user.age);

		// name因为是引用类型，所以copy和原型的引用是同一的
		Assert.assertTrue(copy.name == user.name);
		// String为不可以变类。没有办法可以通过对copy.name的字符串的操作改变这个字符串。
		// 改变引用新的对象不会影响原型.
		copy.name = "newname"; 
		Assert.assertEquals("newname", copy.name);
		System.out.println(user.name);
		Assert.assertEquals("user", copy.name);
	}

	// @Test
	public void test() throws CloneNotSupportedException {
		// user
		User user = new User();
		user.name = "user";
		user.age = 20;
		// account
		Account account = new Account();
		account.user = user;
		account.balance = 10000;
		// copy
		Account copy = (Account) account.clone();

		// balance因为是primitive,所以copy和原型是相等且独立的.
		Assert.assertEquals(copy.balance, account.balance);// 不相等会报错
		copy.balance = 20000;
		// 改变copy不影响原型
		Assert.assertTrue(copy.balance != account.balance);
	}
}
