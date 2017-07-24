package com.tairan.cloud.service.clone;
/**
* hzmpj
* 2017年7月24日
* 
* http://blog.csdn.net/leilu2008/article/details/6712535
* GOF设计模式：Gang of Four(四人组)，由四个作者编写的设计模式一书。
* 
* 有一个模式为原型模式，用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象。
* 简单的说就是clone一个对象实例，使得clone出来的copy和原有的对象一模一样。
* 
* 目的：如果一个对象内部有可变对象实例的话，public api不应该直接返回该对象的引用，以防止调用
* 方的code改变该对象的内部状态，这个时候可以返回该对象的clone.
* 
* 
* 浅coy和深copy：浅copy仅仅是复制了一下引用，copy和原型引用的东西是一样的。
* 深copy：即赋值原型中对象的内存copy，而不仅仅是一个引用，
* 
*/
public class RequestChainTest implements Cloneable{
	
}
