package com.tairan.cloud.service.chsi;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.tairan.cloud.common.HttpUtils;
import com.tairan.cloud.util.HttpClientFactory;

@Service
@Transactional
public class ChsiGetPasswordService {
	private final static Logger logger = LoggerFactory.getLogger(ChsiGetPasswordService.class);
	public static String ctoken = "";

	/**
	 * 请求验证码
	 * 
	 * @param client
	 */
	public Map<String, String> acquireCode(HttpClient client) {
		Map<String, String> map = Maps.newHashMap();
		String url = "https://account.chsi.com.cn/account/captchimagecreateaction.action?time="
				+ System.currentTimeMillis();
		byte[] image = HttpUtils.excuteGetWrappedByteArray(client, url, null);
		/** 存储图片用于测试 */
		try {
			FileUtils.writeByteArrayToFile(new File("D:\\test\\code\\" + System.currentTimeMillis() + ".png"), image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		// Base64 base64 = new Base64();
		// map.put("image", base64.encodeAsString(image));
		// return map;
	}

	public void usernameByPhone(HttpClient client, Map<String, String> params, String code) {
		String username = params.get("username");
		Map<String, String> retriveParams = Maps.newHashMap();
		retriveParams.put("loginName", username);
		retriveParams.put("captch", code);
		String retriveHtml = HttpUtils.excutePostWrappedString(client,
				"https://account.chsi.com.cn/account/password!retrive.action", null, retriveParams, null, null);
		// TODO 验证码错误 或者 用户名不存在捕获并抛出

		Document retriveDoc = Jsoup.parse(retriveHtml);
		if (retriveDoc.select("input[name='ctoken']").isEmpty()) {
			System.out.println("失败");
		} else {
			ctoken = retriveDoc.select("input[name='ctoken']").attr("value");
			logger.info(ctoken);
			Map<String, String> nextParams = Maps.newHashMap();
			nextParams.put("ctoken", ctoken);
			String nextHtml = HttpUtils.excutePostWrappedString(client,
					"https://account.chsi.com.cn/account/forgot/rtvbymphoneindex.action", null, nextParams, null, null);
		}
	}

	public void telephone(HttpClient client, Map<String, String> params, String code) {
		String telephone = params.get("telephone");
		String realname = params.get("realname");
		String idcard = params.get("idcard");
		Map<String, String> rtvbymphoneParams = Maps.newHashMap();
		rtvbymphoneParams.put("captch", code);
		rtvbymphoneParams.put("mphone", telephone);
		rtvbymphoneParams.put("ctoken", ctoken);
		rtvbymphoneParams.put("xm", realname);
		rtvbymphoneParams.put("sfzh", idcard);
		String rtvbymphoneHtml = HttpUtils.excutePostWrappedString(client,
				"https://account.chsi.com.cn/account/forgot/rtvbymphone.action", null, rtvbymphoneParams, null, null);
		System.out.println(rtvbymphoneHtml);
	}

	public String usernameByEmail(HttpClient client, Map<String, String> params, String code) {
		String username = params.get("username");
		Map<String, String> retriveParams = Maps.newHashMap();
		retriveParams.put("loginName", username);
		retriveParams.put("captch", code);
		String retriveHtml = HttpUtils.excutePostWrappedString(client,
				"https://account.chsi.com.cn/account/password!retrive.action", null, retriveParams, null, null);
		// TODO 验证码错误 或者 用户名不存在捕获并抛出

		Document retriveDoc = Jsoup.parse(retriveHtml);
		if (retriveDoc.select("input[name='ctoken']").isEmpty()) {
			System.out.println("失败");
			return null;
		} else {
			ctoken = retriveDoc.select("input[name='ctoken']").attr("value");
			logger.info(ctoken);
			Map<String, String> nextParams = Maps.newHashMap();
			nextParams.put("ctoken", ctoken);
			String nextHtml = HttpUtils.excutePostWrappedString(client,
					"https://account.chsi.com.cn/account/forgot/rtvbyemailindex.action", null, nextParams, null, null);
			System.out.println(nextHtml);
			// TODO 判断邮箱是否绑定 没有绑定 抛出错误

			Map<String, String> emailsMap = Maps.newHashMap();
			Document nextDoc = Jsoup.parse(nextHtml);
			Elements optionLinks = nextDoc.select("select#username option");
			for (Element optionLink : optionLinks) {
				String text = optionLink.text();
				String value = optionLink.attr("value");
				if (StringUtils.isNotBlank(value)) {
					emailsMap.put(value, text);
				}
			}
			return JSON.toJSONString(emailsMap);
		}
	}

	public void emial(HttpClient client, Map<String, String> params) {
		String username = params.get("username");
		String ctoken = params.get("ctoken");
		String realname = params.get("realname");
		String idcard = params.get("idcard");
		
		Map<String, String> rtvbyemailsendParams = Maps.newHashMap();
		rtvbyemailsendParams.put("username", username);
		rtvbyemailsendParams.put("ctoken", ctoken);
		rtvbyemailsendParams.put("xm", realname);
		rtvbyemailsendParams.put("sfzh", idcard);
		String rtvbyemailsendHtml = HttpUtils.excutePostWrappedString(client,
				"https://account.chsi.com.cn/account/forgot/rtvbyemailsend.action", null, rtvbyemailsendParams, null,
				null);
		System.out.println(rtvbyemailsendHtml);
	}

	// https://account.chsi.com.cn/account/password!retrive
	// https://account.chsi.com.cn/account/captchimagecreateaction.action?time=1487129932096
	// https://account.chsi.com.cn/account/password!retrive.action
	/**
	 * loginName:15967189649 captch:sqoh
	 * 
	 * @param args
	 */

	//
	// https://account.chsi.com.cn/account/captchimagecreateaction.action?time=1487130145851
	/**
	 * 感觉 密码区分大小写
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ChsiGetPasswordService s = new ChsiGetPasswordService();
		HttpClient client = HttpClientFactory.getInstance().create();
		s.acquireCode(client);
		Scanner s1 = new Scanner(System.in);
		System.out.print("验证码:");
		String code = s1.next();
		Map<String, String> usernameParams = Maps.newHashMap();
		usernameParams.put("username", "15967189649");
		s.usernameByPhone(client, usernameParams, code);
		s.acquireCode(client);
		System.out.print("验证码:");
		String code1 = s1.next();
		s1.close();
		Map<String, String> telephoneParams = Maps.newHashMap();
		telephoneParams.put("telephone", "15967189649");
		telephoneParams.put("realname", "满鹏举");
		telephoneParams.put("idcard", "220881199110254110");
		s.telephone(client, telephoneParams, code1);
	}

}
