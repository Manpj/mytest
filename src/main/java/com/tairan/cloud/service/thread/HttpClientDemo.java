package com.tairan.cloud.service.thread;

import java.io.IOException;

import javax.xml.ws.spi.http.HttpContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientDemo {
	static class GetThread extends Thread {

		private final CloseableHttpClient httpClient;
		private final HttpClientContext context;
		private final HttpGet httpget;

		public GetThread(CloseableHttpClient httpClient, HttpGet httpget) {
			this.httpClient = httpClient;
			this.context = HttpClientContext.create();
			this.httpget = httpget;
		}

		@Override
		public void run() {
			try {
				CloseableHttpResponse response = httpClient.execute(httpget, context);
				try {
					HttpEntity entity = response.getEntity();
				} finally {
					response.close();
				}
			} catch (ClientProtocolException ex) {
				// Handle protocol errors
			} catch (IOException ex) {
				// Handle I/O errors
			}
		}

	}

	public static void main(String[] args) {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

		// URIs to perform GETs on
		String[] urisToGet = { "http://www.domain1.com/", "http://www.domain2.com/", "http://www.domain3.com/",
				"http://www.domain4.com/" };

		// create a thread for each URI
		GetThread[] threads = new GetThread[urisToGet.length];
		for (int i = 0; i < threads.length; i++) {
			HttpGet httpget = new HttpGet(urisToGet[i]);
			threads[i] = new GetThread(httpClient, httpget);
		}

		// start the threads
		for (int j = 0; j < threads.length; j++) {
			threads[j].start();
		}

		// join the threads
		for (int j = 0; j < threads.length; j++) {
			try {
				threads[j].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
