package com.tairan.cloud.service.thread;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpRequestFutureTask;

/**
 * 
 * httpClient 的异步访问处理；基于4.x版本的FutureRequestExecutionService
 * 
 */

public class EasyAsyncRequestFuture {

	public static void main(String[] args) throws Exception {

		HttpClient httpclient = HttpClientBuilder.create()

				.setMaxConnPerRoute(5).setMaxConnTotal(5).build();

		ExecutorService execService = Executors.newFixedThreadPool(5);

		FutureRequestExecutionService requestExecService = new FutureRequestExecutionService(

				httpclient, execService);

		String url = "http://blog.csdn.NET/ffm83/article/details/41944921";

		try {

			// 因为是一个asynchronous, 我们必须提供一个ResponseHandler

			ResponseHandler<Boolean> handler = new ResponseHandler<Boolean>() {

				public Boolean handleResponse(HttpResponse response)

						throws ClientProtocolException, IOException {

					// 如果能访问就认为OK

					return response.getStatusLine().getStatusCode() == 200;

				}

			};

			// 创建一个简单的请求
			
			HttpGet request1 = new HttpGet(url);

			HttpRequestFutureTask<Boolean> futureTask1 = requestExecService

					.execute(request1, HttpClientContext.create(), handler);

			Boolean wasItOk1 = futureTask1.get();

			System.out.println(StringUtils.leftPad("1创建请求 ok? " + wasItOk1,

					60, "-"));

			// 取消一个已经发出的请求

			try {

				HttpGet request2 = new HttpGet(url);

				HttpRequestFutureTask<Boolean> futureTask2 = requestExecService

						.execute(request2, HttpClientContext.create(), handler);

				futureTask2.cancel(true);

				Boolean wasItOk2 = futureTask2.get();

				System.out.println(StringUtils.leftPad("2取消请求, 不应该被执行: "

						+ wasItOk2 + wasItOk1, 60, "-"));

			} catch (CancellationException e) {

				System.out.println(StringUtils.rightPad("2取消请求, 应该被执行", 60,

						"-"));

			}

			// 设置请求超时

			try {

				HttpGet request3 = new HttpGet(url);

				HttpRequestFutureTask<Boolean> futureTask3 = requestExecService

						.execute(request3, HttpClientContext.create(), handler);

				// 如果响应非常快的应用，想要测试这个结果，需要调整超时的时间

				Boolean wasItOk3 = futureTask3.get(1, TimeUnit.MILLISECONDS);

				System.out.println(StringUtils.leftPad(

						"3超时请求 ok? " + wasItOk3, 60, "-"));

			} catch (TimeoutException e) {

				System.out.println(StringUtils.rightPad("3超时", 60, "-"));

			}

			FutureCallback<Boolean> callback = new FutureCallback<Boolean>() {

				public void completed(Boolean result) {

					System.out.println("completedwith " + result);

				}

				public void failed(Exception ex) {

					System.out.println("failedwith " + ex.getMessage());

				}

				public void cancelled() {

					System.out.println("cancelled");

				}

			};

			// 创建一组简单的有回调的请求

			try {

				HttpGet reqCallbackA = new HttpGet(url);

				HttpRequestFutureTask<Boolean> futureTaskA = requestExecService

						.execute(reqCallbackA, HttpClientContext.create(),

								handler, callback);

				Boolean isCallbackAok = futureTaskA.get(10, TimeUnit.SECONDS);

				System.out.println(StringUtils.leftPad("4A回调ok? "

						+ isCallbackAok, 60, "-"));

			} catch (TimeoutException e) {

				System.out.println("4A超时");

			}

			// 取消

			try {

				System.out.println(StringUtils.center("4取消", 60, "-"));

				HttpGet reqCallbackB = new HttpGet(url);

				HttpRequestFutureTask<Boolean> futureTaskB = requestExecService

						.execute(reqCallbackB, HttpClientContext.create(),

								handler, callback);

				futureTaskB.cancel(true);

				Boolean isCallbackBok = futureTaskB.get(1, TimeUnit.SECONDS);

				System.out.println(StringUtils.leftPad("4B回调ok? "

						+ isCallbackBok, 60, "-"));

			} catch (TimeoutException e) {

				System.out.println("4B超时");

			} catch (CancellationException e) {

				System.out.println("4B取消");

			}

			// 超时

			try {

				System.out.println(StringUtils.center("4超时", 60, "-"));

				HttpGet reqCallbackC = new HttpGet(url);

				HttpRequestFutureTask<Boolean> futureTaskC = requestExecService

						.execute(reqCallbackC, HttpClientContext.create(),

								handler, callback);

				Boolean isCallbackCok = futureTaskC.get(1,

						TimeUnit.MILLISECONDS);

				System.out.println(StringUtils.leftPad("4C回调ok? "

						+ isCallbackCok, 60, "-"));

			} catch (TimeoutException e) {

				System.out.println("4C超时");

			} catch (CancellationException e) {

				System.out.println("4C取消");

			}

			// 异常

			try {

				System.out.println(StringUtils.center("4异常", 60, "-"));

				HttpGet reqCallbackD = new HttpGet("http://www.不可能网站.ccom");

				HttpRequestFutureTask<Boolean> futureTaskD = requestExecService

						.execute(reqCallbackD, HttpClientContext.create(),

								handler, callback);

				Boolean isCallbackDok = futureTaskD.get(1,

						TimeUnit.SECONDS);

				System.out.println(StringUtils.leftPad("4D回调ok? "

						+ isCallbackDok, 60, "-"));

			} catch (TimeoutException e) {

				System.out.println("4D超时");

			} catch (CancellationException e) {

				System.out.println("4D取消");

			}

		} finally {

			requestExecService.close();

		}

	}

}