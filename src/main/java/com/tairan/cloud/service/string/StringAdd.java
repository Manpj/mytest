package com.tairan.cloud.service.string;

/**
 * hzmpj 2017年6月22日
 */
public class StringAdd {

	public static void addEmpty(String s) {
		String type = "1";
		String s1 = "1234" + null;
		String s2 = "1234" + "";
		String s3 = "1234#" + type == "1" ? "像首歌" : "1234";
		String s4 = "1234#";
		String s5 = "12345" + type == "1" ? "像首歌" : "1234";
		/**
		 * 将前面看组一个整体：： "123456" + type == "1"
		 */
		String s6 = "123456" + type == "1" ? "像首歌" : "1234";
		/**
		 * 注意这里的 type=="1" 是正确的
		 */
		String s7 = "1234" + (type == "1" ? "像首歌" : "1234");
		String type1 = type;
		String s8 = "1234" + (type1 == "1" ? "像首歌" : "1234");
		/**
		 * ==比较的是值，对于8种基本类型。不是基本类型，比较的是地址
		 */
		s = type;
		String s9 = "1234" + (s == "1" ? "像首歌" : "1234");
		type = "2";
		type = "1";
		String s10 = "1234" + (type == "1" ? "想首歌" : "1234");

		System.out.println("s1:" + s1);
		System.out.println("s2:" + s2);
		System.out.println("s3:" + s3);
		System.out.println("s4:" + s4);
		System.out.println("s5:" + s5);
		System.out.println("s6:" + s6);
		System.out.println("s7:" + s7);
		System.out.println("s8:" + s8);
		System.out.println("s9:" + s9);
		System.out.println("s10:"+s10);
	}

	public static void main(String[] args) {
		addEmpty("1");
	}

}
