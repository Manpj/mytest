package com.tairan.cloud.service.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * hzmpj 2017年7月20日
 */
public class MapSort {

	public static Map<String, String> hashMap = new HashMap<String, String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("431300", "1");
			put("513200", "2");
			put("320000", "3");
			put("511300", "4");
		}
	};

	public static Map<String, String> treeMap = new TreeMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("431300", "1");
			put("513200", "2");
			put("320000", "3");
			put("511300", "4");
		}
	};

	public static void HashMapSort() {
		/**
		 * 通过Collections的sort方法::::注意只能输出entry，如果输出map，任然是原来的顺序，因为HashMap就是无序的
		 */
		List<Entry<String, String>> list = new ArrayList<Entry<String, String>>(hashMap.entrySet());
		Collections.sort(list, new Comparator<Entry<String, String>>() {
			@Override
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {
				// return o1.getKey().compareTo(o2.getKey());// key升序
				return o2.getKey().compareTo(o1.getKey());// key降序
			}
		});
		list.forEach(entry -> {
			System.out.println(entry.getKey() + "---" + entry.getValue());
		});
		System.out.println("--------------------------------------");
		/**
		 * 使用流失处理的排序方法
		 */
		List<Entry<String, String>> entrylist = hashMap.entrySet().stream()
				.sorted(new Comparator<Entry<String, String>>() {
					@Override
					public int compare(Entry<String, String> o1, Entry<String, String> o2) {
						// return o1.getKey().compareTo(o2.getKey());// key升序
						return o1.getValue().compareTo(o2.getValue());// 也可以按照value升序
					}
				}).collect(Collectors.toList());
		entrylist.forEach(entry -> {
			System.out.println(entry.getKey() + "---" + entry.getValue());
		});
	}

	public static void TreeMapSort() {
		/**
		 * treeMap默认是升序的
		 */
		treeMap.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + "---" + entry.getValue());
		});
		System.out.println("-----------------------------------");
		treeMap = new TreeMap<String, String>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);//降序
			}
		});
		treeMap.put("431300", "1");
		treeMap.put("513200", "2");
		treeMap.put("320000", "3");
		treeMap.put("511300", "4");
		treeMap.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + "---" + entry.getValue());
		});
		System.out.println("-----------------------------------");
		/**
		 * 按照value排序可以使用HashMap的排序方法
		 */
	}

	public static void main(String[] args) {
		// HashMapSort();
		TreeMapSort();
	}

}
