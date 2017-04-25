package com.tairan.cloud.service.guava;

import java.util.Optional;

public class Demo1 {
	/**
	 * java8 optional
	 */
	private void test() {
		/**
		 * of 为非null的值创建一个Optional
		 * 
		 */
		Optional<String> name = Optional.of("ss");
		// Optional<String> someNull = Optional.of(null);
		/**
		 * ofNullable 为指定的值创建一个Optional,如果指定的值为null，则返回一个空的Optional.
		 * 
		 */
		Optional empty = Optional.ofNullable(null);
		/**
		 * isPresent 如果值存在返回true，否则返回false
		 */
		if (name.isPresent()) {
			System.out.println(name.get());
		}
		/**
		 * get 如果Optional有值则将其返回，否则抛出NoSuchElementException
		 */
		// System.out.println(empty.get());
		/**
		 * ifPresent 如果Optional实例有值则为其调用consumer.否则不做处理
		 * 
		 * consumer类：包含一个抽象方法，该抽象方法对传入的值进行处理，但没有返回值。
		 * 
		 * java8支持不用接口直接通过lambda表达式传入参数
		 */
		name.ifPresent((value) -> {
			System.out.println(value.length());
		});
		/**
		 * orElse 如果有值则将其返回，否则返回指定的其他值
		 */
		System.out.println(empty.orElse("没值"));
		System.out.println(name.orElse("没值"));
		/**
		 * orElseGet 与orElse方法类似，区别在于得到的默认值，orElse方法将传入的字符串作为默认值，
		 * orElseGet方法可以接收Supplier接口的实现用来生成默认值
		 */
		System.out.println(empty.orElseGet(() -> "没值"));
		System.out.println(name.orElseGet(() -> "没值"));
		/**
		 * orElseThrow 如果有值则将其返回，否则抛出supplier接口创建的异常
		 */
		try {
			empty.orElseThrow(ValueAbsentException::new);
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Demo1 d = new Demo1();
		d.test();
	}

}

class ValueAbsentException extends Throwable {

	public ValueAbsentException() {
		super();
	}

	public ValueAbsentException(String message) {
		super(message);
	}
	
	@Override
	public String getMessage() {
		return "No value present in the Optional instance";
	}
}
