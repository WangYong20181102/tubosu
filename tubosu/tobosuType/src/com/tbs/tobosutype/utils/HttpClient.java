package com.tbs.tobosutype.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.tbs.tobosutype.global.MyApplication;
import org.apache.http.conn.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class HttpClient {

	private static final AsyncHttpClient httpClient = new AsyncHttpClient();
//	private static final AsyncHttpClient httpClient =  new AsyncHttpClient(true, 80, 443);//请求https的方式

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
		// 处理 https的问题
//        httpClient.setSSLSocketFactory(getSocketFactory()); // 加载证书
		httpClient.post(url, params, responseHandler);
	}


    private  static  MySSLSocketFactory getSocketFactory() {
        // TODO Auto-generated method stub
        MySSLSocketFactory sslFactory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream instream = MyApplication.getContext().getAssets().open("tobosucom.crt");//后台拿到的.p12证书
            keyStore.load(instream, "后台拿到的.p12证书密码".toCharArray());
            sslFactory = new MySSLSocketFactory(keyStore);
        } catch (KeyStoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (CertificateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (UnrecoverableKeyException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sslFactory;
    }



    private static KeyStore getkeyStore() {
        // TODO Auto-generated method stub
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStore.getDefaultType()); //  BKS  "RSA/DSA/BKS/AES"   //NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
            InputStream instream = MyApplication.getContext().getAssets().open("keystore.keystore");
            keyStore.load(instream, "tubosu".toCharArray());
        } catch (KeyStoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (CertificateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return keyStore;
    }
}
