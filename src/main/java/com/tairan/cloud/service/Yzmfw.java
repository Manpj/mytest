package com.tairan.cloud.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 测试页面：http://172.30.249.17:7010/file
 * @author hzmpj
 *
 */
public class Yzmfw {
	
	public void begin(HttpClient http){
		String url="http://172.30.249.17:7010/byte";
		HttpPost post=new HttpPost(url);
		try {
			byte[] yzmByteArray=toByte();
			post.setEntity(new ByteArrayEntity(yzmByteArray));
			HttpResponse response=http.execute(post);
			String html=EntityUtils.toString(response.getEntity());
			System.out.println(html);
		} catch (IOException e) {
			
		}
	}
	
	public byte[] toByte(){
		HttpClient http=HttpClients.createDefault();
		int ran = (int) (Math.random() * 100 + 1);
		String url = "http://zhixing.court.gov.cn/search/security/jcaptcha.jpg?" + ran;
		HttpGet get=new HttpGet(url);
		get.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
		try {
			HttpResponse response = http.execute(get);
			byte[] yzmByteArray=EntityUtils.toByteArray(response.getEntity());
			/** 没有路径创建路径 */
//			File pfile = new File("D:\\test\\code\\");
////			File pfile = new File(imagePath);
//			if (!pfile.exists()) {
//				pfile.mkdirs();
//			}
//			File filename = new File(pfile.getPath() +"zhixing-"+ System.currentTimeMillis()+".png");
//			FileOutputStream out = new FileOutputStream(filename);
//			out.write(yzmByteArray);
//			out.close();
			return yzmByteArray;
		} catch (IOException e) {
			throw new RuntimeException("未知异常");
		}
	}
	
	
	public static void main(String[] args){
		HttpClient http=HttpClients.createDefault();
		Yzmfw y=new Yzmfw();
		y.begin(http);
	}
}
