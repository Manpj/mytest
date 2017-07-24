package com.tairan.cloud.service.clone;

/**
 * hzmpj 2017年7月24日
 */
public class User implements Cloneable {
	String name;
	int age;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}

class Account implements Cloneable {
	User user;
	long balance;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
