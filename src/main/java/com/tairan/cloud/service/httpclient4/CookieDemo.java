package com.tairan.cloud.service.httpclient4;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

public class CookieDemo {

	public void testCookieStore() {
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("ant_stream_522475cb1bfd1", "1482492814/2784874171");
		cookie.setDomain(".fushun.gov.cn");
		cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
		cookieStore.addCookie(cookie);
		CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		System.out.println("cookie store:"+cookieStore.getCookies());
		HttpGet httpGet=new HttpGet("url");
//		try{
//			HttpResponse httpResponse=client.execute(httpGet);
//			
//		}catch(IOException e){
//			e.printStackTrace();
//		}
	}
	
	public static void main(String[] args){
		CookieDemo demo=new CookieDemo();
		demo.testCookieStore();
	}
	
}
