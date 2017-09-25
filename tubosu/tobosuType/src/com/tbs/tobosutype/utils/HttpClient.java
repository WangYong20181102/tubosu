package com.tbs.tobosutype.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class HttpClient {
//	private static final AsyncHttpClient httpClient = new AsyncHttpClient(true,80,443);
	private static final AsyncHttpClient httpClient = new AsyncHttpClient();
	public static AsyncHttpClient getAsyncHttpClient(){
		return httpClient;
	}
	public static void setIMEI(String imei){
		httpClient.addHeader("IMEI", imei);
	}
	public static void get(String url, RequestParams params, ResponseHandlerInterface responseHandler){
		httpClient.get(url, params, responseHandler);
	}
	public static void post(String url, RequestParams params, ResponseHandlerInterface responseHandler){
		httpClient.post(url, params, responseHandler);
	}
}
