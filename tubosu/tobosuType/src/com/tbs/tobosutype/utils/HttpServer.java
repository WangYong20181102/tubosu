package com.tbs.tobosutype.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.http.HttpClientHelper;

public class HttpServer {
	private static HttpServer instance;

	private HttpServer() {

	}

	public static HttpServer getInstance() {
		if (instance == null) {
			synchronized (HttpServer.class) {
				if (instance == null)
					instance = new HttpServer();
			}
		}
		return instance;
	}

	/**
	 * 没有使用
	 * @param url
	 * @param city
	 * @param lng
	 * @param lat
	 * @param recommend
	 * @param httpResponseHandler
	 */
	public void requestUserLogin( String url, String city, String lng, String lat, String recommend, AsyncHttpResponseHandler httpResponseHandler) {
		RequestParams params = new RequestParams();
		params.put("city", city);
		params.put("lng", lng);
		params.put("lat", lat);
		params.put("recommend", recommend);
		requestPOST1(url, params, httpResponseHandler);
	}

	public void requestPOST1(String action, RequestParams params, AsyncHttpResponseHandler httpResponseHandler) {
		HttpClient.post(action, params, httpResponseHandler);
	}

	public AsyncHttpClient getAsyncHttpClient() {
		return HttpClient.getAsyncHttpClient();
	}

}
