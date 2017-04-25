package com.tairan.cloud.service.thread;

import java.util.ArrayList;
import java.util.List;

public class FanXingDemo {

	/**
	 * 泛型的好处
	 */
	// 开始版本
	public void write(Integer i, Integer[] ia) {
	};

	public void write(Double d, Double[] da) {
	};

	// 泛型版本
	public <T> void write(T t, T[] ta) {
	};

	/**
	 * 定义泛型
	 */
	// 紧跟类名后面,定义泛型T|S,且S继承T  类中定义的泛型使用情况：当类中操作的引用数据类型不确定的时候
	public class TestClassDefine<T, S extends T> {
	}

	// 定义在方法修饰符后面(public),定义泛型T|S,且S继承T
	public <T, S extends T> T testGenericMethodDefine(T t, S s) {
		return null;
	};

	/**
	 * 实例化泛型
	 */
	// 实例化定义在类上的泛型
	List<String> list = new ArrayList<String>();

	// 继承或者实现接口时
	public class MyList<E> extends ArrayList<E> implements List<E> {
	}

	/**
	 * 通配符？
	 * 
	 * 上面是泛型的定义和赋值，当在赋值的时候，上面说赋值的都是具体类型，当赋值的类型不确定的时候， 可以用通配符？替代
	 */
	List<?> unknowList;
	List<? extends Number> nuknowNumberList;
	List<? extends Integer> unknowBaseLineIntegerList;
}
