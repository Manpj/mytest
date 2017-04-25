package com.tairan.cloud.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tairan.cloud.common.ErrorCode;
import com.tairan.cloud.common.FundException;

/**
 * http工具类
 * 
 * @author hzzlj
 *
 */
public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	private static final String CHARSET = "UTF-8";

	public static <T> Object excuteGet(HttpContext context, String url, ResponseHandler<T> handler) {
		return excuteGet(context, url, null, handler);
	}

	public static <T> Object excutePost(String url, byte[] bytes, ResponseHandler<T> handler) {
		return excutePost(url, bytes, null, handler);
	}

	public static <T> Object excutePost(HttpContext context, String url, Map<String, String> params, String reqCharset,
			ResponseHandler<T> handler) {
		return excutePost(context, url, params, null, reqCharset, handler);
	}

	public static <T> Object excuteGet(HttpContext context, String url, Map<String, String> headers,
			ResponseHandler<T> handler) {
		if (StringUtils.isBlank(url)) {
			throw new IllegalArgumentException("url and context can not be null.");
		}
		try (CloseableHttpClient client = HttpClientFactory.getInstance().create()) {
			RequestBuilder requestBuilder = RequestBuilder.get().setUri(url);
			if (headers != null) {
				for (Entry<String, String> header : headers.entrySet()) {
					requestBuilder.addHeader(header.getKey(), header.getValue());
				}
			}
			HttpUriRequest request = requestBuilder.build();
			HttpResponse response = client.execute(request, context);
			return handler.handleResponse(response);
		} catch (ParseException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	public static <T> Object excutePost(String url, byte[] bytes, Map<String, String> headers,
			ResponseHandler<T> handler) {
		if (StringUtils.isBlank(url) || bytes == null) {
			throw new IllegalArgumentException("url and client can not be null.");
		}
		try (CloseableHttpClient client = HttpClientFactory.getInstance().create()) {
			RequestBuilder requestBuilder = RequestBuilder.post().setUri(url);
			if (headers != null) {
				for (Entry<String, String> header : headers.entrySet()) {
					requestBuilder.addHeader(header.getKey(), header.getValue());
				}
			}
			ByteArrayEntity formEntity = new ByteArrayEntity(bytes);
			requestBuilder.setEntity(formEntity);
			HttpUriRequest request = requestBuilder.build();
			HttpResponse response = client.execute(request);
			return handler.handleResponse(response);
		} catch (ParseException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	public static <T> Object excutePost(HttpContext context, String url, Map<String, String> params,
			Map<String, String> headers, String reqCharset, ResponseHandler<T> handler) {
		if (StringUtils.isBlank(url)) {
			throw new IllegalArgumentException("url and client can not be null.");
		}
		try (CloseableHttpClient client = HttpClientFactory.getInstance().create()) {
			RequestBuilder requestBuilder = RequestBuilder.post().setUri(url);
			if (headers != null) {
				for (Entry<String, String> header : headers.entrySet()) {
					requestBuilder.addHeader(header.getKey(), header.getValue());
				}
			}
			if (MapUtils.isNotEmpty(params)) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, Charset.forName(reqCharset));
				requestBuilder.setEntity(formEntity);
			}
			HttpUriRequest request = requestBuilder.build();
			HttpResponse response = client.execute(request, context);
			return handler.handleResponse(response);
		} catch (ParseException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	/**
	 * 执行get请求并将response转化为字符串
	 * 
	 * @param client
	 * @param url
	 * @param headers
	 * @param resCharset
	 * @return
	 */
	public static String excuteGetWrappedString(HttpClient client, String url, Map<String, String> headers,
			String resCharset) {
		HttpResponse response = excuteGet(client, url, headers);
		if (StringUtils.isBlank(resCharset)) {
			resCharset = CHARSET;
		}
		if (response == null) {
			throw new FundException(ErrorCode.GOAL_CONNECTION_ERROR.getCode(),
					ErrorCode.GOAL_CONNECTION_ERROR.getMessage());
		} else {
			try {
				return EntityUtils.toString(response.getEntity(), resCharset);
			} catch (IOException e) {
				logger.error("", e);
				throw new FundException(ErrorCode.GOAL_RESPONSE_ERROR.getCode(),
						ErrorCode.GOAL_RESPONSE_ERROR.getMessage());
			}
		}
	}

	/**
	 * 执行get请求并将response转化为字节数组
	 * 
	 * @param client
	 * @param url
	 * @param headers
	 * @return
	 */
	public static byte[] excuteGetWrappedByteArray(HttpClient client, String url, Map<String, String> headers) {
		HttpResponse response = excuteGet(client, url, headers);
		if (response == null) {
			throw new FundException(ErrorCode.GOAL_CONNECTION_ERROR.getCode(),
					ErrorCode.GOAL_CONNECTION_ERROR.getMessage());
		} else {
			try {
				return EntityUtils.toByteArray(response.getEntity());
			} catch (IOException e) {
				logger.error("", e);
				throw new FundException(ErrorCode.GOAL_RESPONSE_ERROR.getCode(),
						ErrorCode.GOAL_RESPONSE_ERROR.getMessage());
			}
		}
	}

	/**
	 * 执行get请求并并返回response对象
	 * 
	 * @param client
	 * @param url
	 * @param headers
	 * @return
	 */
	public static HttpResponse excuteGet(HttpClient client, String url, Map<String, String> headers) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new IllegalArgumentException("url and client can not be null.");
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
			logger.error("", e);
		}
		return null;

	}

	/**
	 * 执行post请求并将response转化为字符串
	 * 
	 * @param client
	 * @param url
	 * @param headers
	 * @param params
	 * @param reqCharset
	 * @param resCharset
	 * @return
	 */
	public static String excutePostWrappedString(HttpClient client, String url, Map<String, String> headers,
			Map<String, String> params, String reqCharset, String resCharset) {
		HttpResponse response = excutePost(client, url, headers, params, reqCharset);
		if (StringUtils.isBlank(resCharset)) {
			resCharset = CHARSET;
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

	/**
	 * 执行post请求并将response转化为字节数组
	 * 
	 * @param client
	 * @param url
	 * @param headers
	 * @return
	 */
	public static byte[] excutePostWrappedByteArray(HttpClient client, String url, Map<String, String> headers,
			Map<String, String> params, String reqCharset) {
		HttpResponse response = excutePost(client, url, headers, params, reqCharset);
		if (response == null) {
			throw new FundException(ErrorCode.GOAL_CONNECTION_ERROR.getCode(),
					ErrorCode.GOAL_CONNECTION_ERROR.getMessage());
		} else {
			try {
				return EntityUtils.toByteArray(response.getEntity());
			} catch (IOException e) {
				logger.error("", e);
				throw new FundException(ErrorCode.GOAL_RESPONSE_ERROR.getCode(),
						ErrorCode.GOAL_RESPONSE_ERROR.getMessage());
			}
		}
	}

	/**
	 * 执行post请求并并返回response对象
	 * 
	 * @param client
	 * @param url
	 * @param headers
	 * @return
	 */
	public static HttpResponse excutePost(HttpClient client, String url, Map<String, String> headers,
			Map<String, String> params) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new IllegalArgumentException("url and client can not be null.");
		}
		return excutePost(client, url, headers, params, CHARSET);

	}

	/**
	 * 执行post请求并并返回response对象
	 * 
	 * @param client
	 * @param url
	 * @param headers
	 * @return
	 */
	public static HttpResponse excutePost(HttpClient client, String url, Map<String, String> headers,
			Map<String, String> params, String reqCharset) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new IllegalArgumentException("url and client can not be null.");
		}
		if (StringUtils.isBlank(reqCharset)) {
			reqCharset = CHARSET;
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

	public static String excutePost(HttpClient client, String url, Map<String, String> headers, String jsonParams,
			String reqCharset) {
		if (StringUtils.isBlank(url) || client == null) {
			throw new IllegalArgumentException("url and client can not be null.");
		}
		if (StringUtils.isBlank(reqCharset)) {
			reqCharset = "utf-8";
		}
		try {
			RequestBuilder requestBuilder = RequestBuilder.post().setUri(url);
			if (StringUtils.isNotBlank(jsonParams)) {
				StringEntity entity = new StringEntity(jsonParams);
				entity.setContentEncoding(reqCharset);
				entity.setContentType("text/plain");
				requestBuilder.setEntity(entity);
			}
			HttpUriRequest request = requestBuilder.build();
			if (MapUtils.isNotEmpty(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse response = client.execute(request);
			try {
				String html = EntityUtils.toString(response.getEntity(), "utf-8");
				return html;
			} catch (IOException e) {
				logger.error("", e);
				throw new FundException(ErrorCode.GOAL_RESPONSE_ERROR.getCode(),
						ErrorCode.GOAL_RESPONSE_ERROR.getMessage());
			}
		} catch (ParseException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

}
