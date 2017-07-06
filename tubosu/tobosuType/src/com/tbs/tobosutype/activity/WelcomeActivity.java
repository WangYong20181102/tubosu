package com.tbs.tobosutype.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Request;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.CheckUpdateUtils;
import com.tbs.tobosutype.utils.MD5Util;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 欢迎 页
 *
 * @author dec
 */
public class WelcomeActivity extends Activity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
//    private CheckUpdateUtils updateUtils;

    private String check_password = Constant.TOBOSU_URL + "tapp/passport/isCheckPwdUp";
    private String check_ab_test = Constant.TOBOSU_URL + "tapp/spcailpic/get_ab";
    private String SURVIVAL_URL = Constant.TOBOSU_URL + "tapp/DataCount/survival_count";


    private Context mContext;

    private long startapp_time = 0L;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_welcome_bg);

        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, Constant.UMENG_KEY, AppInfoUtil.getChannType(this));

        MobclickAgent.startWithConfigure(config);

        mContext = WelcomeActivity.this;
        startapp_time = new Date().getTime();
        needPermissions();
        do_webpage();
        getSetting();

        // 欢迎页面 这里可以做基础数据集成到本地，如果没有则不需要
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                countDownloadNum();
                if ("".equals(getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("encode_pass", ""))) {
                    // 没有登录
//					System.out.println(">>>>>>>没有登录<<<<<");
                } else {
                    // 登录
                    check_password();
//					System.out.println(">>>>>>>登录<<<<<");
                    SystemClock.sleep(2000);
                }

                // AB测试
                if ("0".equals(getSharedPreferences("AB_TEST", Context.MODE_PRIVATE).getString("status", "0"))) {
                    get_ABTest();
                } else {
                    System.out.println("-- 你已经选择过ab测试 --");
                }


                runOnUiThread(new IntentTask());
            }

            ;
        }.start();
    }

    private String MAC_CODE = "";
    private String _TOKEN = "";


    /**
     * 获取手机mac地址<br/>
     * 错误返回12个0
     */
    public static String getMacAddress(Context context) {
        // 获取mac地址：
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr
                    .getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress()))
                    macAddress = info.getMacAddress().replace(":", "");
                else
                    return macAddress;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }


    private void getSetting(){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        MAC_CODE = getMacAddress(mContext);
        _TOKEN = MD5Util.md5(MD5Util.md5(MAC_CODE+1)+date);
    }

    private void countDownloadNum(){
        OKHttpUtil client = new OKHttpUtil();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mac_code", MAC_CODE);
        map.put("type","1");
        map.put("_token", _TOKEN);
        client.post(SURVIVAL_URL, map, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(com.squareup.okhttp.Response response, String json) {
                Util.setLog(TAG, "songchengcai >>>"+json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                e.printStackTrace();
                Util.setLog(TAG, "songchengcai >>>onFail");
            }

            @Override
            public void onError(com.squareup.okhttp.Response response, int code) {
                Util.setLog(TAG, code+"<<<<songchengcai <<<<");
            }
        });
    }

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private void check_password() {
        mRequestQueue = Volley.newRequestQueue(mContext);
        mStringRequest = new StringRequest(Method.POST, check_password, new Listener<String>() {

            @Override
            public void onResponse(String json) {
                System.out.println(">>>>>>>检查修改密码请求结果[" + json + "]<<<<<");
                try {
                    JSONObject obj = new JSONObject(json);
                    if (obj.getInt("error_code") == 0) {
                        long gap = startapp_time - getSharedPreferences("userInfo", Context.MODE_PRIVATE).getLong("login_time", 0);
                        int days = (int) (gap / 1000 / 3600 / 24);
                        System.out.println(">>>>>>>" + days + "<<<<<");
                        if (days >= 30) {
                            getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().clear().commit();
                            System.out.println(">>>>>>>有30天啦<<<<<");
                        }
                    } else if (obj.getInt("error_code") == 250) {
                        getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().clear().commit();
                        Toast.makeText(mContext, "密码已修改,请重新登录", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError err) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("uid", getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("userid", ""));
                hashMap.put("pass", getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("encode_pass", ""));
                return hashMap;
            }
        };
        mStringRequest.setTag("volley_request_check");
        mRequestQueue.add(mStringRequest);
    }




    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("volley_request_check");
        }
        if (testRequestQueue != null) {
            testRequestQueue.cancelAll("ab_test");
        }
    }


    private class IntentTask implements Runnable {

        @Override
        public void run() {

                // 首次安装标记
                getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).edit().putString("go_poporder_string", "5").commit();

                if (getSharedPreferences("city", 0).getString("cityName", "").length() > 0) {
                    // 定位到城市
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                } else {
                    // 定位不到城市时，去定位城市
                    // 设置这里是welcome
                    CacheManager.setPageFlag(mContext, "welcome");
                    Intent cityIntent = new Intent(mContext, SelectCtiyActivity.class);
                    startActivity(cityIntent);
                    Log.d(TAG, "--进入 SelectCtiyActivity 页面--");
                }

                finish();
                System.gc();
//            }
        }
    }


    private RequestQueue testRequestQueue;
    private StringRequest testStringRequest;
    private String status = "";

    private void get_ABTest() {
        testRequestQueue = Volley.newRequestQueue(mContext);
        testStringRequest = new StringRequest(Method.POST, check_ab_test, new Listener<String>() {

            @Override
            public void onResponse(String json) {
                System.out.println(">>>>>>>ab测试 请求结果[" + json + "]<<<<<");

                try {
                    JSONObject obj = new JSONObject(json);
                    if (obj.getInt("error_code") == 0) {
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            status = array.getJSONObject(i).getString("status");
                            // 保存 ab测的状态
                            getSharedPreferences("AB_TEST", Context.MODE_PRIVATE).edit().putString("status", status).commit();
                            System.out.println("-- 已经存ab测的状态 --");
                            break;
                        }

                    } else if (obj.getInt("error_code") == 201) {
                        System.out.println("-- 获取ab测 状态失败 --");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return null;
            }
        };
        testStringRequest.setTag("ab_test");
        testRequestQueue.add(testStringRequest);
    }


    private void do_webpage() {
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                System.out.println("====涂蓉html>>" + uri + "<<<");
//				String name = uri.getQueryParameter("name");
//				String age= uri.getQueryParameter("age");
            }
        }
    }

    private  void needPermissions(){
        if(Build.VERSION.SDK_INT >= 23){
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,

                    Manifest.permission.ACCESS_NETWORK_STATE
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_WIFI_STATE

            };

            requestPermissions(permissions, 101);
        }
    }
}
