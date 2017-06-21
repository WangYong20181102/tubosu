package com.tobosu.mydecorate.global;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by dec on 2016/10/19.
 */

public class OKHttpUtil {

    private static final String TAG = OKHttpUtil.class.getSimpleName();

    private OkHttpClient client;
    private Handler mHandler;

    public OKHttpUtil() {
        client = new OkHttpClient();
        //设置连接超时时间,在网络正常的时候有效
        client.setConnectTimeout(5000, TimeUnit.SECONDS);
        //设置读取数据的超时时间
        client.setReadTimeout(5000, TimeUnit.SECONDS);
        //设置写入数据的超时时间
        client.setWriteTimeout(5000, TimeUnit.SECONDS);

        //Looper.getMainLooper()  获取主线程的消息队列
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * get请求
     * @param url
     * @param baseCallBack
     */
    public void get(String url, BaseCallBack baseCallBack) {
        Request request = buildRequest(url, null, HttpMethodType.GET);
        sendRequest(request, baseCallBack);
    }


    /**
     * post请求
     * @param url
     * @param params
     * @param baseCallBack
     */
    public void post(String url, HashMap<String, Object> params, BaseCallBack baseCallBack) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        sendRequest(request, baseCallBack);

    }

    /**
     * 1）获取Request对象
     *
     * @param url
     * @param params
     * @param httpMethodType 请求方式不同，Request对象中的内容不一样
     * @return Request 必须要返回Request对象， 因为发送请求的时候要用到此参数
     */
    private Request buildRequest(String url, HashMap<String, Object> params, HttpMethodType httpMethodType) {
        //获取辅助类对象
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        //如果是get请求
        if (httpMethodType == HttpMethodType.GET) {
            builder.get();
        } else {
            RequestBody body = buildFormData(params);
            builder.post(body);
        }

        //返回请求对象
        return builder.build();
    }

    /**
     * 2)发送网络请求
     *
     * @param request
     * @param baseCallBack
     */
    private void sendRequest(Request request, final BaseCallBack baseCallBack) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBackFail(baseCallBack,request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    //此时请求结果在子线程里面，如何把结果回调到主线程里？
                    callBackSuccess(baseCallBack,response, json);
                } else {
                    callBackError(baseCallBack,response, response.code());
                }
            }
        });
    }


    /**
     * 主要用于构建请求参数
     *
     * @param param
     * @return ResponseBody
     */
    private RequestBody buildFormData(HashMap<String, Object> param) {

        FormEncodingBuilder builder = new FormEncodingBuilder();
        //遍历HashMap集合
        if (param != null && !param.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = param.entrySet();
            for (Map.Entry<String, Object> entity : entries) {
                String key = entity.getKey();
                Object value = entity.getValue();
                builder.add(key, String.valueOf(value));
            }
        }
        return builder.build();
    }

    //请求类型定义
    private enum HttpMethodType {
        GET,
        POST
    }

    //定义回调接口
    public interface BaseCallBack {
        void onSuccess(Response response, String json);

        void onFail(Request request, IOException e);

        void onError(Response response, int code);
    }



    //主要用于子线程和主线程进行通讯
    private void callBackSuccess(final BaseCallBack baseCallBack, final Response response, final String json){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                baseCallBack.onSuccess(response,json);
            }
        });
    }


    private void callBackError(final BaseCallBack baseCallBack, final Response response, final int code){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                baseCallBack.onError(response,code);
            }
        });
    }

    private void callBackFail(final BaseCallBack baseCallBack, final Request request, final IOException e){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //相当于此run方法是在主线程执行的，可以进行更新UI的操作
                baseCallBack.onFail(request,e);
            }
        });
    }
}
