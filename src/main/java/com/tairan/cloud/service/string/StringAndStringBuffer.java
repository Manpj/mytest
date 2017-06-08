package com.tairan.cloud.service.string;

import org.apache.commons.lang3.StringUtils;
//http://blog.csdn.net/qingmengwuhen1/article/details/52175303
public class StringAndStringBuffer {
	
	public static void addString(String str) {
		str=str.replace("j", "1");
	}
	
	public static void addStringBuffer(StringBuffer sb) {
		sb.append(" hello");
	}
	
	public static void addInt(int i) {
		i=i+1;
	}
	
	//String不可变性
	public static void main(String[] args) {
		String str="java";
		StringBuffer sb=new StringBuffer("my");
		int i=8;
		addString(str);
		addStringBuffer(sb);
		System.out.println(str);
		System.out.println(sb);
		System.out.println(i);
		
		String sss="1r2r3r4r5r";
		System.out.println(StringUtils.ordinalIndexOf(sss, "r",2));
	}
}
