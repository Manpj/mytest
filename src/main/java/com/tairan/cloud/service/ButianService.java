package com.tairan.cloud.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;

import com.tairan.cloud.util.HttpClientFactory;
import com.tairan.cloud.util.HttpUtils;

public class ButianService {
	public static void main(String[] args) {
		HttpClient client = HttpClientFactory.getInstance().create();
		Map<String,String> headers=new HashMap<String,String>();
		//headers.put("Cookie","__jsluid=1586bcff3632b5ea0e039de9dc0f39bd; __jsl_clearance=1474179963.799|0|2cKlGAPDDCZVaZtH2DECq5RLWPE%3D; JSESSIONID=AF93F81B911F809964EA103915F3AD15; bdshare_firstime=1474180049202");
//		headers.put("Accept-Language","zh-CN,zh;q=0.8");
//		headers.put("Accept-Encoding","gzip, deflate, sdch");
//		headers.put("Upgrade-Insecure-Requests","1");
//		headers.put("Referer","http://www.cnvd.org.cn/flaw/list.htm");
//		headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		//headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
		String html=HttpUtils.excuteGetWrappedString(client, "https://butian.360.cn/vul/list/page/1", headers, null);
		System.out.println(html);
	}
}
