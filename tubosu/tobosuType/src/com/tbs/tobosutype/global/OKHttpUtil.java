package com.tbs.tobosutype.global;
import android.content.Context;
import android.util.Log;

import com.tbs.tobosutype.utils.CacheManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dec on 2016/10/19.
 */

public class OKHttpUtil {
    private static final String TAG = OKHttpUtil.class.getSimpleName();
    private static OkHttpClient client;
    public OKHttpUtil() {
    }

    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (OKHttpUtil.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }


    /**
     * get请求
     * @param url
     * @param callBack
     */
    public static void get(String url, Callback callBack) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callBack);
    }


    /**
     * post请求
     * @param url
     * @param params
     * @param callback
     */
    public static void post(String url, HashMap<String, String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, String.valueOf(params.get(key)));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);

    }

//    /**
//     * 1）获取Request对象
//     *
//     * @param url
//     * @param params
//     * @param httpMethodType 请求方式不同，Request对象中的内容不一样
//     * @return Request 必须要返回Request对象， 因为发送请求的时候要用到此参数
//     */
//    private Request buildRequest(String url, HashMap<String, String> params, HttpMethodType httpMethodType) {
//
//        //获取辅助类对象
//        Request.Builder builder = new Request.Builder();
//        builder.url(url);
//
//        //如果是get请求
//        if (httpMethodType == HttpMethodType.GET) {
//            builder.get();
//        } else {
//            RequestBody body = buildFormData(params);
//            builder.post(body);
//        }
//
//        //返回请求对象
//        return builder.build();
//    }



    /**
     * 下载文件
     *@param  context
     * @param url      请求的地址 可以是图片的url或者是apk的下载地址
     * @param fileDir  保存文件的路径名
     * @param fileName 保存文件的文件名
     */
    public static void downFile(final Context context, String url, final String fileDir, final String fileName) {
        Request request = new Request.Builder().url(url).build();
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File dirFile = new File(fileDir);
                    if (!dirFile.exists()) {
                        //没有该文件夹的时候创建该文件夹
                        dirFile.mkdir();
                    }
                    //创建图片文件
                    File file = new File(fileDir, fileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //保存下载文件路径
                    CacheManager.setLoadingAdPath(context, fileDir + "/" + fileName);
                    Log.e("HttpUtils", "下载文件成功！===" + file.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("HttpUtils", "下载出错===" + e.toString());
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                }
            }
        });
    }
}
