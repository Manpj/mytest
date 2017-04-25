package com.tairan.cloud.service.chsi;

import java.util.Map;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.tairan.cloud.common.HttpUtils;
import com.tairan.cloud.util.HttpClientFactory;

@Service
@Transactional
public class ChsiServiceV2 {
	private final static Logger logger = LoggerFactory.getLogger(ChsiServiceV2.class);

	public static void main(String[] args) {
		HttpClient client = HttpClientFactory.getInstance().create();
		String ltHtml = HttpUtils.excuteGetWrappedString(client,
				"https://account.chsi.com.cn/passport/login?service=https%3A%2F%2Fmy.chsi.com.cn%2Farchive%2Fj_spring_cas_security_check",
				null, null);
		Document ltDoc = Jsoup.parse(ltHtml);
		String lt = ltDoc.select("input[name='lt']").attr("value");
		Map<String, String> loginParams = Maps.newHashMap();
		loginParams.put("username", "15967189649");
		loginParams.put("password", "9695..mpj.");
		loginParams.put("lt", lt);
		loginParams.put("_eventId", "submit");
		loginParams.put("submit", "登  录");
		String loginHtml = HttpUtils.excutePostWrappedString(client,
				"https://account.chsi.com.cn/passport/login?service=https%3A%2F%2Fmy.chsi.com.cn%2Farchive%2Fj_spring_cas_security_check",
				null, loginParams, null, null);
		System.out.println(loginHtml);
		System.out.println("-------------------------");
		String indexHtml=HttpUtils.excuteGetWrappedString(client, "https://my.chsi.com.cn/archive/index.action", null, null);
		//System.out.println(indexHtml);
	}

}
