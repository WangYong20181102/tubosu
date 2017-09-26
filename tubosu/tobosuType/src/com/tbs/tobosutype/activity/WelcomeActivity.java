package com.tbs.tobosutype.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.MD5Util;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import java.util.List;

/**
 * 欢迎 页
 *
 * @author dec
 */
public class WelcomeActivity extends Activity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
//    private CheckUpdateUtils updateUtils;

    private String check_password = Constant.TOBOSU_URL + "tapp/passport/isCheckPwdUp";
    private String check_ab_test  = Constant.TOBOSU_URL + "tapp/spcailpic/get_ab";
    private String SURVIVAL_URL   = Constant.TOBOSU_URL + "tapp/DataCount/survival_count";

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
//        do_webpage();
        getSetting();

        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                countDownloadNum();
                if ("".equals(getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("encode_pass", ""))) {
                    // 没有登录
					Util.setErrorLog(TAG,">>>>>>>没有登录<<<<<");
                } else {
                    // 登录
                    check_password();
					Util.setErrorLog(TAG,">>>>>>>登录<<<<<");
                    SystemClock.sleep(2000);
                }

                // AB测试
//                if ("0".equals(getSharedPreferences("AB_TEST", Context.MODE_PRIVATE).getString("status", "0"))) {
//                    get_ABTest();
//                } else {
//                    Util.setErrorLog(TAG,"-- 你已经选择过ab测试 --");
//                }


                runOnUiThread(new IntentTask());
            }
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
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mac_code", MAC_CODE);
        map.put("type","1");
        map.put("_token", _TOKEN);
        OKHttpUtil.post(SURVIVAL_URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.setLog(TAG, "onFailure >>>"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String json = response.body().string();
                Util.setLog(TAG, "onResponse >>>"+json);
            }
        });
    }


    private void check_password() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("uid", getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("userid", ""));
        hashMap.put("pass", getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("encode_pass", ""));
        OKHttpUtil.post(check_password, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String json = response.body().string();
                Util.setErrorLog(TAG,">>>>>>>检查修改密码请求结果[" + json + "]<<<<<");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(json);
                            if (obj.getInt("error_code") == 0) {
                                long gap = startapp_time - getSharedPreferences("userInfo", Context.MODE_PRIVATE).getLong("login_time", 0);
                                int days = (int) (gap / 1000 / 3600 / 24);
                                Util.setErrorLog(TAG,">>>>>>>" + days + "<<<<<");
                                if (days >= 30) {
                                    getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().clear().commit();
                                    Util.setErrorLog(TAG,">>>>>>>有30天啦<<<<<");
                                }
                            } else if (obj.getInt("error_code") == 250) {
                                getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().clear().commit();
                                Toast.makeText(mContext, "密码已修改,请重新登录", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取 loading页的图片地址
     */
    private class IntentTask implements Runnable {

        @Override
        public void run() {
            if(Util.isNetAvailable(mContext)){
                Util.setErrorLog(TAG, "----11----有网络--------");
                HashMap<String, String> hashMap = new HashMap<String, String>();
                WindowManager wm = getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                hashMap.put("width",width+"");
                hashMap.put("height",height+"");
                OKHttpUtil.post(Constant.GET_LOADING_AD_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 没有拿到图片地址
                        Util.setErrorLog(TAG, "----22----有网络--请求失败------");
                        goLoadingActivity("", -1);
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        final String json = response.body().string();
                        Util.setErrorLog(TAG, json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getInt("error_code") == 0){
                                // 拿到了图片地址
                                JSONObject data = jsonObject.getJSONObject("data");
                                String url = data.getString("img_url");
//                                String adId = data.getString("id");
//                                if(CacheManager.getLoadingAdId(mContext).equals(adId)){
//                                    // id相等 则不需要重新下载广告图片
//                                }else {
//                                    // 重新下载广告图片 重新设置广告id
//                                    httpDownLoadImg(url);
//                                    CacheManager.setLoadingAdId(mContext, adId);
//                                }
                                String time = data.getString("stay_time");
                                Util.setErrorLog(TAG, "----33---有网络 有地址------");
                                goLoadingActivity(url,Integer.parseInt(time));
                            }else {
                                // 没有拿到图片地址
                                Util.setErrorLog(TAG, "---44----有网络 无地址------");
                                goLoadingActivity("", -1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else {
                // 没有拿到图片地址
                Util.setErrorLog(TAG, "-----5---无网络--------");
                goLoadingActivity("", -1);
            }
        }
    }

    /**
     *  下载广告页面
     * @param downloadUrl
     */
    private void httpDownLoadImg(String downloadUrl) {
        //创建文件夹
        File dirFile = new File(Constant.IMG_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = "loadingAd.jpg";
        OKHttpUtil.downFile(mContext, downloadUrl, dirFile.getPath(), fileName);
    }

    /**
     * 跳转
     * @param time 广告停留时间
     */
    private void goLoadingActivity(String imgUrl, int time){
        Util.setErrorLog(TAG, "-----66---"+time+"s--------");
        Intent intent = null;
        if(time == -1){
            // 没有图片下载
            if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
                CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
                intent = new Intent(mContext, PopOrderActivity.class);
            }else {
                intent = new Intent(mContext, MainActivity.class);
            }
        }else {
            // 有图片下载
            if(!"".equals(imgUrl/*CacheManager.getLoadingAdPath(mContext)*/)){
                // 已经下载好了
                intent = new Intent(mContext, LoadingActivity.class);
                // 传递url
                intent.putExtra("loading_img_url", imgUrl);
                intent.putExtra("staytime", time);
            }else {
                // 图片正在下载中，还没有下载好
                if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
                    CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
                    intent = new Intent(mContext, PopOrderActivity.class);
                }else {
                    intent = new Intent(mContext, MainActivity.class);
                }
            }
        }
        startActivity(intent);
        finish();
        System.gc();
    }


    private String status = "";

    private void get_ABTest() {
        OKHttpUtil.post(check_ab_test, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.setErrorLog(TAG,">>>>>>>ab测试 请求结果6465415613153<<<<<");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String json = response.body().string();
                Util.setErrorLog(TAG,">>>>>>>ab测试 请求结果[" + json + "]<<<<<");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(json);
                            if (obj.getInt("error_code") == 0) {
                                JSONArray array = obj.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    status = array.getJSONObject(i).getString("status");
                                    // 保存 ab测的状态
                                    getSharedPreferences("AB_TEST", Context.MODE_PRIVATE).edit().putString("status", status).commit();
                                    Util.setErrorLog(TAG,"-- 已经存ab测的状态 --");
                                    break;
                                }

                            } else if (obj.getInt("error_code") == 201) {
                                Util.setErrorLog(TAG,"-- 获取ab测 状态失败 --");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


//    private void do_webpage() {
//        Intent i_getvalue = getIntent();
//        String action = i_getvalue.getAction();
//
//        if (Intent.ACTION_VIEW.equals(action)) {
//            Uri uri = i_getvalue.getData();
//            if (uri != null) {
//                Util.setErrorLog(TAG,"====涂蓉html>>" + uri + "<<<");
////				String name = uri.getQueryParameter("name");
////				String age= uri.getQueryParameter("age");
//            }
//        }
//    }

    private  void needPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permission = getPermissionList(mContext);
            if (permission.size() > 0) {
                requestPermissions(permission.toArray(new String[permission.size()]), 101);
            }
        }
    }

    public List<String> getPermissionList(Context activity) {
        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_WIFI_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_NETWORK_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.INTERNET);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.CHANGE_WIFI_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission;
    }
}
