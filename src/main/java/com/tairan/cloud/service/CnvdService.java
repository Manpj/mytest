package com.tairan.cloud.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.tairan.cloud.common.ErrorCode;
import com.tairan.cloud.common.FundException;
import com.tairan.cloud.util.HttpUtils;

public class CnvdService {

	public static String excuteGetWrappedString(HttpClient client, String url, Map<String, String> headers,
			String resCharset) {
		HttpResponse response = excuteGet(client, url, headers);
		if (StringUtils.isBlank(resCharset)) {
			resCharset = "utf-8";
		}
		if (response == null) {
			throw new FundException(ErrorCode.GOAL_CONNECTION_ERROR.getCode(),
					ErrorCode.GOAL_CONNECTION_ERROR.getMessage());
		} else {
			try {
				return EntityUtils.toString(response.getEntity(), resCharset);
			} catch (IOException e) {
				throw new FundException(ErrorCode.GOAL_RESPONSE_ERROR.getCode(),
						ErrorCode.GOAL_RESPONSE_ERROR.getMessage());
			}
		}
	}

	public static HttpResponse excuteGet(HttpClient client, String url, Map<String, String> headers) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new IllegalArgumentException("url and client can not be null.");
		}
		try {
			HttpHost proxy = new HttpHost("172.17.18.84", 8080, "http");
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			RequestBuilder requestBuilder = RequestBuilder.get().setUri(url).setConfig(config);
			HttpUriRequest request = requestBuilder.build();
			if (MapUtils.isNotEmpty(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse response = client.execute(request);
			return response;
		} catch (IOException e) {

		}
		return null;
	}

	public static void main(String[] args) {
		HttpClient client = HttpClients.createDefault();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie",
				"__jsluid=1586bcff3632b5ea0e039de9dc0f39bd; __jsl_clearance=1474179963.799|0|2cKlGAPDDCZVaZtH2DECq5RLWPE%3D; JSESSIONID=AF93F81B911F809964EA103915F3AD15; bdshare_firstime=1474180049202");
		// headers.put("Accept-Language","zh-CN,zh;q=0.8");
		// headers.put("Accept-Encoding","gzip, deflate, sdch");
		// headers.put("Upgrade-Insecure-Requests","1");
		// headers.put("Referer","http://www.cnvd.org.cn/flaw/list.htm");
		// headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
		String html = excuteGetWrappedString(client, "http://www.cnvd.org.cn/flaw/list.htm", headers, "utf-8");
		System.out.println(html);

	}
}
