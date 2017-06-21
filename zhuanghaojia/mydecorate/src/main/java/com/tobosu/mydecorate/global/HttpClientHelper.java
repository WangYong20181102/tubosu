package com.tobosu.mydecorate.global;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class HttpClientHelper {

	public static HttpClient checkNetwork(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				return httpClient;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream loadFileFromURL(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet requestGet = new HttpGet(url);
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(requestGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				return entity.getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] loadByteFromURL(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet requestGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(requestGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				return EntityUtils.toByteArray(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("====>" + e.toString());
		}
		return null;
	}

	

	public static HashMap<String,Object> loadTextFromURL(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		BasicHttpParams headerParams = new BasicHttpParams();
		headerParams.setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803");
		HttpGet requestGet = new HttpGet(url);
		requestGet.setParams(headerParams);
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		try {
			HttpResponse httpResponse = httpClient.execute(requestGet);
			Header[] headers = (Header[]) httpResponse.getAllHeaders();
			for(int i=0;i<headers.length;i++){
	          if(headers[i].getName().equalsIgnoreCase("Set-Cookie")){
	        	  map.put("header", headers[i].getValue());
	            }
	        }
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				
				map.put("body", EntityUtils.toByteArray(httpEntity));
				
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] doGetSubmit(String url, String params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet requestGet = new HttpGet(url + "?" + params);
		try {
			HttpResponse httpResponse = httpClient.execute(requestGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				return EntityUtils.toByteArray(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] doPostSubmit(String url, List<NameValuePair> params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost requestPost = new HttpPost(url);
		try {
			requestPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse httpResponse = httpClient.execute(requestPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				return EntityUtils.toByteArray(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] doPostSubmit(String url, Map<String, Object> params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost requestPost = new HttpPost(url);

		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		try {
			if (params != null) {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					BasicNameValuePair nameValuePair = new BasicNameValuePair(
							key, value);
					parameters.add(nameValuePair);
				}
			}
			requestPost
					.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
			HttpResponse httpResponse = httpClient.execute(requestPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				return EntityUtils.toByteArray(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doPostSubmit(String url, Map<String, String> params,String header) {
		HttpClient httpClient = new DefaultHttpClient();
		BasicHttpParams headerParams = new BasicHttpParams();
		headerParams.setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803");
		HttpPost requestPost = new HttpPost(url);
		requestPost.addHeader("Cookie",header);
		requestPost.setParams(headerParams);
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		try {
			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					BasicNameValuePair nameValuePair = new BasicNameValuePair(
							key, value);
					parameters.add(nameValuePair);
				}
			}
			requestPost
					.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
			HttpResponse httpResponse = httpClient.execute(requestPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				return EntityUtils.toString(httpEntity,"utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	
	
}
