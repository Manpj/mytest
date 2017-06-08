package com.tairan.cloud.service;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * hzmpj 2017年5月19日
 */
public class Test1 {
	// public static void main(String[] args) {
	// boolean flag=false;
	// Thread t = new Thread(new Runnable(){
	// public void run(){
	// int i = 0;
	//
	// while(i>50){
	// System.out.println(i++);
	// }
	// flag = false;
	// }});
	// t.start();
	// if(!flag){
	// t.interrupt();
	// }
	// }

	public static void main(String[] args) {
		Map<String, String> m = Maps.newHashMap();
		m.put("1", "9");
		m.put("1", "10");
		System.out.println(m.get("1"));
	}
}
