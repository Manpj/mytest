package com.tairan.cloud.service.sort;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MapKeySort {
	/**
	 * 问题：按照key a，b，c，d排序；
	 * treeMap ,hashMap线程安全等问题；
	 *  熟悉Map常见操作.
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * 比较器可以实现：Comparator,Comparator默认是升序的
		 */
//		Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
//			@Override
//			public int compare(String o1, String o2) {
//				return o2.compareTo(o1);
//			}
//		});
		
		Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		
		map.put("b", "bbbbb");
		map.put("d", "ddddd");
		map.put("c", "ccccc");
		map.put("a", "aaaaa");
		
		Set<String> keySet=map.keySet();
		Iterator<String> iter=keySet.iterator();
		while(iter.hasNext()){
			String key=iter.next();
			System.out.println(key);
		}
		
		
		/**
		 * 死锁原因：：：：
		 * Collections的sort方法进行排序，前提条件是，所有的元素都必须能够根据所提供的比较器进行比较
		 */
		
	}

}
