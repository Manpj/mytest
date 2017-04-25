package com.tairan.cloud.service.zhixing;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import com.tairan.cloud.util.HttpClientFactory;
import com.tairan.cloud.util.HttpUtils;

public class CookieDemo {
	
	
	public static void main(String[] args){
		HttpClient client=HttpClientFactory.getInstance().create();
		//String html=HttpUtils.excuteGetWrappedString(client, "http://zhixing.court.gov.cn/search/", null, null);
		HttpResponse response=HttpUtils.excuteGet(client, "http://zhixing.court.gov.cn/search/", null);
		Header[] headers=response.getAllHeaders();
		for(Header header:headers){
			System.out.println(header.getName()+"-------"+header.getValue());
		}
	}
}
          