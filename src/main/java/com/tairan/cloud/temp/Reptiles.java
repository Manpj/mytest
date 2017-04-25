package com.tairan.cloud.temp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Reptiles {
	
	//返回object
	public void nextText(String html,String tablecss){
		try{
			Document doc = Jsoup.parse(html);
			Elements links = doc.select(tablecss+" tr");
			for(Element link:links){
				String text=link.text();
				
			}
		}catch(Exception e){
			throw new RuntimeException("jsoup解析错误");
		}
	}
}
