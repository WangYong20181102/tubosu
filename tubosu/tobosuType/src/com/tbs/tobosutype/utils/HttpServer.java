package com.tbs.tobosutype.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

	public void requestUserLogin(String url, String city, String lng, String lat, String recommend,
			AsyncHttpResponseHandler httpResponseHandler) {
		RequestParams params = new RequestParams();
		params.put("city", city);
		params.put("lng", lng);
		params.put("lat", lat);
		params.put("recommend", recommend);
		requestPOST(url, params, httpResponseHandler);
	}

	public void requestPOST(String action, RequestParams params, AsyncHttpResponseHandler httpResponseHandler) {
		HttpClient.post(action, params, httpResponseHandler);
	}

	public AsyncHttpClient getAsyncHttpClient() {
		return HttpClient.getAsyncHttpClient();
	}

}
