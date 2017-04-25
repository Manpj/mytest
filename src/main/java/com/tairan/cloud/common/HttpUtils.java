package com.tairan.cloud.common;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
	
	private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	public static HttpResponse excuteGet(HttpClient client, String url, Map<String, String> headers) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new RuntimeException("url and client can not be null.");
		}
		try {
			RequestBuilder requestBuilder = RequestBuilder.get().setUri(url);
			HttpUriRequest request = requestBuilder.build();
			if (MapUtils.isNotEmpty(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse response = client.execute(request);
			return response;
		} catch (IOException e) {
			logger.error("IO异常", e);
		}
		return null;
	}
	
	public static byte[] excuteGetWrappedByteArray(HttpClient client, String url, Map<String, String> headers) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new RuntimeException("url and client can not be null.");
		}
		try {
			RequestBuilder requestBuilder = RequestBuilder.get().setUri(url);
			HttpUriRequest request = requestBuilder.build();
			if (MapUtils.isNotEmpty(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse response = client.execute(request);
			if (response == null) {
				logger.info("目标网站连接异常");
				throw new RuntimeException("目标网站连接异常");
			} else {
				try {
					return EntityUtils.toByteArray(response.getEntity());
				} catch (IOException e) {
					logger.info("目标网站响应异常");
					throw new RuntimeException("目标网站响应异常");
				}
			}
		} catch (IOException e) {
			logger.error("IO异常", e);
			throw new RuntimeException("IO异常");
		}
	}

	public static String excuteGetWrappedString(HttpClient client, String url, Map<String, String> headers,
			String resCharset) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new RuntimeException("url and client can not be null.");
		}
		try {
			RequestBuilder requestBuilder = RequestBuilder.get().setUri(url);
			HttpUriRequest request = requestBuilder.build();
			if (MapUtils.isNotEmpty(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse response = client.execute(request);
			if (StringUtils.isBlank(resCharset)) {
				resCharset = "utf-8";
			}
			if (response == null) {
				logger.info("目标网站连接异常");
				throw new RuntimeException("目标网站连接异常");
			} else {
				try {
					return EntityUtils.toString(response.getEntity(), resCharset);
				} catch (IOException e) {
					logger.info("目标网站响应异常");
					throw new RuntimeException("目标网站响应异常");
				}
			}
		} catch (IOException e) {
			logger.error("IO异常", e);
			throw new RuntimeException("IO异常");
		}
	}

	public static HttpResponse excutePost(HttpClient client, String url, Map<String, String> headers,
			Map<String, String> params, String reqCharset) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new IllegalArgumentException("url and client can not be null.");
		}
		if (StringUtils.isBlank(reqCharset)) {
			reqCharset = "utf-8";
		}
		try {
			RequestBuilder requestBuilder = RequestBuilder.post().setUri(url);
			if (MapUtils.isNotEmpty(params)) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, Charset.forName(reqCharset));
				requestBuilder.setEntity(formEntity);
			}
			HttpUriRequest request = requestBuilder.build();
			if (MapUtils.isNotEmpty(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse response = client.execute(request);
			return response;
		} catch (ParseException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	public static byte[] excutePostWrappedByteArray(HttpClient client, String url, Map<String, String> headers,
			Map<String, String> params, String reqCharset) {
		HttpResponse response = excutePost(client, url, headers, params, reqCharset);
		if (response == null) {
			logger.info("目标网站连接异常");
			throw new RuntimeException("目标网站连接异常");
		} else {
			try {
				return EntityUtils.toByteArray(response.getEntity());
			} catch (IOException e) {
				logger.info("目标网站响应异常");
				throw new RuntimeException("目标网站响应异常");
			}
		}
	}

	public static String excutePostWrappedString(HttpClient client, String url, Map<String, String> headers,
			Map<String, String> params, String reqCharset, String resCharset) {
		HttpResponse response = excutePost(client, url, headers, params, reqCharset);
		if (StringUtils.isBlank(resCharset)) {
			resCharset = "utf-8";
		}
		if (response == null) {
			logger.info("目标网站连接异常");
			throw new RuntimeException("目标网站连接异常");
		} else {
			try {
				return EntityUtils.toString(response.getEntity(), resCharset);
			} catch (IOException e) {
				logger.info("目标网站响应异常");
				throw new RuntimeException("目标网站响应异常");
			}
		}
	}

}
