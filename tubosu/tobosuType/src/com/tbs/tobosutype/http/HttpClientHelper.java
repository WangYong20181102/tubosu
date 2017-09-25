package com.tbs.tobosutype.http;
import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.tbs.tobosutype.utils.SSLSocketFactoryEx;
import com.tbs.tobosutype.utils.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import java.security.KeyStore;
import org.apache.http.conn.ssl.SSLSocketFactory;


public class HttpClientHelper {
	private static String TAG = "HttpClientHelper";
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
					BasicNameValuePair nameValuePair = new BasicNameValuePair(key, value);
					parameters.add(nameValuePair);
				}
			}
			requestPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
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


	public static HashMap<String,Object> loadTextFromURL(String url) {
//		HttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = HttpClientHelper.getHttpClient();
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


	public static String doPostSubmit(String url, Map<String, String> params,String header) {
//		HttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = HttpClientHelper.getHttpClient();
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
					BasicNameValuePair nameValuePair = new BasicNameValuePair(key, value);
					parameters.add(nameValuePair);
				}
			}
			requestPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
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



	//************************************************************************************************

	private static HttpClient httpClient;
	private HttpClientHelper() {
	}
	public static synchronized HttpClient getHttpClient() {
		if (null == httpClient) {
			// 初始化工作
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  //允许所有主机的验证
				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
				HttpProtocolParams.setUseExpectContinue(params, true);
				// 设置连接管理器的超时
				ConnManagerParams.setTimeout(params, 10000);
				// 设置连接超时
				HttpConnectionParams.setConnectionTimeout(params, 10000);
				// 设置socket超时
				HttpConnectionParams.setSoTimeout(params, 10000);
				// 设置http https支持
				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				schReg.register(new Scheme("https", sf, 443));
				ClientConnectionManager conManager = new ThreadSafeClientConnManager(params, schReg);
				httpClient = new DefaultHttpClient(conManager, params);
			} catch (Exception e) {
				e.printStackTrace();
				return new DefaultHttpClient();
			}
		}
		return httpClient;
	}


	/**
	 * 读取证书
	 * @param context
	 * @param mUrl
	 * @return
	 */
	public static String requestHTTPSPage(Context context, String mUrl) {
		InputStream ins = null;
		String result = "";
		try {
			ins = context.getAssets().open("tobosucom.crt"); //下载的证书放到项目中的assets目录中
			CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(ins);
			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);
			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
			Scheme sch = new Scheme("https", socketFactory, 443);
			HttpClient mHttpClient = new DefaultHttpClient();
			mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);
			BufferedReader reader = null;
			try {
				Util.setErrorLog(TAG, "executeGet is in,murl:" + mUrl);
				HttpGet request = new HttpGet();
				request.setURI(new URI(mUrl));
				HttpResponse response = mHttpClient.execute(request);
				if (response.getStatusLine().getStatusCode() != 200) {
					request.abort();
					return result;
				}
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				result = buffer.toString();
				Util.setErrorLog(TAG, "mUrl=" + mUrl + "\nresult = " + result);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
