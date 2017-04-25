package com.tairan.cloud.service.thread;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

public class MapDemo {
	private final static Map<String, String> staticmap = Maps.newHashMap();;
	static {
		staticmap.put("a", "avalue");
		staticmap.put("b", "bvalue");
	}

	public static void main(String[] args) {
		/**
		 * Map.getOrDefault(Object,V) 没有找到值，返回默认的值
		 */
		final String cvalue = staticmap.getOrDefault("c", "unknow");
		System.out.println(cvalue);
		/**
		 * Map.putIfAbsent(K,V) 如果map中不存在map键或map值对应的map值为null 并添加新的值
		 */
		String dvalue = staticmap.putIfAbsent("d", "avalue");
		System.out.println(dvalue);
		// Map<String, Integer> map = Maps.newHashMap();
		// map.put("Java", 8);
		// map.put("Csharp", 5);
		// Integer value = map.putIfAbsent("Java", 10);
		// Integer value2 = map.putIfAbsent("Python", 3);
		// for (Map.Entry<String, Integer> entry : map.entrySet()) {
		// System.out.println(entry.getKey() + "——" + entry.getValue());
		// }
		/**
		 * Map.remove(Object,Object) 从map中移除map键对应的map键值对或移除对应的键值对
		 */
		// HashMap<String, Integer> map = new HashMap<String, Integer>();
		// map.put("Java", 8);
		// map.put("Csharp", 5);
		// map.remove("Java",9);
		// //或
		// //map.remove("Java");
		// for (Map.Entry<String, Integer> entry : map.entrySet()) {
		// System.out.println("key : " + entry.getKey() + ", value : " +
		// entry.getValue());
		// }
		/**
		 * Map.replace(K,V) ,,,,Map.replace(K,V,V) boolean replace(Object key,
		 * Object oldValue, Object newValue) Object replace(Object key, Object
		 * value)
		 */
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("Java", 8);
		map.put("Csharp", 5);
		int aa = map.replace("Java", 10);
		System.out.println(aa);
		// for (Map.Entry<String, Integer> entry : map.entrySet())
		// System.out.println("key : " + entry.getKey() + ", value : " +
		// entry.getValue());
	}

}
