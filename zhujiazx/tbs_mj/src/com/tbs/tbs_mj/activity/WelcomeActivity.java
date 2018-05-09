package com.tbs.tbs_mj.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.CacheManager;
import com.tbs.tbs_mj.utils.MD5Util;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WelcomeActivity extends com.tbs.tbs_mj.base.BaseActivity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    @BindView(R.id.welcome_image)
    ImageView welcomeImage;
    private String SURVIVAL_URL = Constant.TOBOSU_URL + "tapp/DataCount/survival_count";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_bg);
        ButterKnife.bind(this);
        mContext = WelcomeActivity.this;
        initBaiduMap();
        initView();
        //获取权限 执行下一步
        needPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "WelcomeActivity执行的生命周期========onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "WelcomeActivity执行的生命周期========onPause()");
    }

    //欢迎页的初始化
    private void welcomeInit() {
        CacheManager.setStartFlag(WelcomeActivity.this, 0);
        getSetting();
        //todo 新的welcome处理逻辑 creat by lin  20180103
        countDownloadNum();//新增用户的数据统计
        // TODO: 2018/3/6 App点击流统计初始化
        // TODO: 2018/3/5 初始化上报数据
        initStatisticsEvent();
        // TODO: 2018/2/27 开始倒计时传数据
        Util.sendEventByTimeKill();

        CacheManager.setChentaoFlag(mContext, 0);
        CacheManager.setCompanyFlag(mContext, 0);
        getCityJson();//获取城市的数据
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: 2017/12/28  在这个节点处理推送消息  下面这个线程是进入Main的方法 在此之前要处理好推送相关的逻辑
                runOnUiThread(new IntentTask());
            }
        }, 3000);
    }

    /**
     * 初始化App事件统计
     * 1.清空之前存储的数据
     * 2.上报一条数据
     */

    private void initStatisticsEvent() {
        Log.e(TAG, "_lin启动App时上传数据===========");
        getSharedPreferences("StatisticsEvent", 0).edit().clear().commit();
        Util.HttpFristPostUserUseInfo();
    }

    //百度定位
    private void initBaiduMap() {
        try {
            LocationClient mLocationClient = new LocationClient(mContext);     //声明LocationClient类
            mLocationClient.start();

            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
            int span = 1000;
            option.setIsNeedAddress(true);
            option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setOpenGps(false);//可选，默认false,设置是否使用gps
            option.setLocationNotify(false);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
            option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
            mLocationClient.setLocOption(option);
            mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            Util.setErrorLog(TAG, "定位码" + location.getLocType());
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            AppInfoUtil.setLat(mContext, location.getLatitude() + "");
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            AppInfoUtil.setLng(mContext, location.getLongitude() + "");
            sb.append("\nradius : ");
            //设置真实的定位信息
            SpUtil.setLatitude(mContext, location.getLatitude() + "");//设置纬度
            SpUtil.setLongitude(mContext, location.getLongitude() + "");//设置经度
            SpUtil.setCity(mContext, location.getCity() + "");//设置城市
            SpUtil.setRadius(mContext, location.getRadius() + "");
            SpUtil.setHomeAndCompanyUsingCity(mContext, "");
//            ToastUtil.showShort(mContext, "触发百度定位=====" + location.getCity() + "获取的本地真实的存储定位信息====" + SpUtil.getCity(mContext));
//            Log.e(TAG, "百度定位监听==========" + location.getCity());
        }

    }

    private void initView() {
        Glide.with(mContext).load(R.drawable.welcome_image)
                .asBitmap().centerCrop().placeholder(R.drawable.welcome_image)
                .error(R.drawable.welcome_image).into(welcomeImage);
//        welcomeImage.setImageResource(R.drawable.welcome_image);
        //区分市场
//        if("appxiaomi".equals(AppInfoUtil.getChannType(MyApplication.getContext()))){
//            Glide.with(mContext).load(R.drawable.wel_xiaomi).placeholder(R.drawable.wel_xiaomi).error(R.drawable.wel_xiaomi).into(welcomeImage);
//        }else if("ali".equals(AppInfoUtil.getChannType(MyApplication.getContext()))){
//            Glide.with(mContext).load(R.drawable.wel_new_ali_img).placeholder(R.drawable.wel_new_ali_img).error(R.drawable.wel_new_ali_img).into(welcomeImage);
//        }else{
//            Glide.with(mContext).load(R.drawable.welcome_image).placeholder(R.drawable.welcome_image).error(R.drawable.welcome_image).into(welcomeImage);
//        }
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


    private void getSetting() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        MAC_CODE = getMacAddress(mContext);
        _TOKEN = MD5Util.md5(MD5Util.md5(MAC_CODE + 1) + date);
    }

    private void countDownloadNum() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("mac_code", MAC_CODE);
        map.put("type", "1");
        map.put("_token", _TOKEN);
        OKHttpUtil.post(SURVIVAL_URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }


    private void getCityJson() {
        if ("".equals(CacheManager.getSaveCityFlag(mContext))) {
            if (Util.isNetAvailable(mContext)) {
                HashMap<String, Object> cityMap = new HashMap<String, Object>();
                cityMap.put("token", Util.getDateToken());
                OKHttpUtil.post(Constant.CITY_JSON, cityMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String cityjson = response.body().string();
                        try {
                            JSONObject object = new JSONObject(cityjson);
                            if (object.getInt("status") == 200) {
                                CacheManager.setSaveCityFlag(mContext, cityjson);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * 获取 loading页的图片地址
     */
    private class IntentTask implements Runnable {

        @Override
        public void run() {
            if (Util.isNetAvailable(mContext)) {
                Util.setErrorLog(TAG, "----11----有网络--------");
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                WindowManager wm = getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                hashMap.put("width", width + "");
                hashMap.put("height", height + "");
                OKHttpUtil.post(Constant.GET_LOADING_AD_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 没有拿到图片地址
                        Util.setErrorLog(TAG, "----22----有网络--请求失败------");
                        goLoadingActivity("", -1,"");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String json = response.body().string();
                        Util.setErrorLog(TAG, json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getInt("error_code") == 0) {
                                // 拿到了图片地址
                                JSONObject data = jsonObject.getJSONObject("data");
                                String url = data.optString("img_url");
                                String time = data.optString("stay_time");
                                String jumpUrl = data.optString("jump_url");
                                Util.setErrorLog(TAG, "----33---有网络 有地址------");
                                goLoadingActivity(url, Integer.parseInt(time),jumpUrl);
                            } else {
                                // 没有拿到图片地址
                                Util.setErrorLog(TAG, "---44----有网络 无地址------");
                                goLoadingActivity("", -1,"");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                // 没有拿到图片地址
                Util.setErrorLog(TAG, "-----5---无网络--------");
                goLoadingActivity("", -1,"");
            }
        }
    }

    /**
     * 跳转
     *
     * @param time 广告停留时间
     */
    private void goLoadingActivity(String imgUrl, int time,String jumpUrl) {
        Util.setErrorLog(TAG, "-----66---" + time + "s--------");
        Intent intent;
        if (time == -1) {
            // 没有图片下载
            if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
                //初次进入我们的App进入发单页面
                CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
                intent = new Intent(mContext, PopOrderActivity.class);
            } else {
                intent = new Intent(mContext, MainActivity.class);
            }
        } else {
            // 有图片下载
            if (!"".equals(imgUrl/*CacheManager.getLoadingAdPath(mContext)*/)) {
                // 已经下载好了
                intent = new Intent(mContext, LoadingActivity.class);
                // 传递url
                intent.putExtra("loading_img_url", imgUrl);
                intent.putExtra("staytime", time);
                intent.putExtra("jump_url", jumpUrl);
            } else {
                // 图片正在下载中，还没有下载好
                if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
                    CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
                    intent = new Intent(mContext, PopOrderActivity.class);
                } else {
                    intent = new Intent(mContext, MainActivity.class);
                }
            }
        }
        if (intent != null) {
            startActivity(intent);
            finish();
//            System.gc();
        }

    }

    //基于6.0以上系统动态获取权限问题
    private void needPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permission = getPermissionList(mContext);//需要获取动态权限的集合 总6个
            Log.e(TAG, "获取权限的集合长度===========" + permission.size());
            if (permission.size() > 0) {
                //未获取全部的权限 去获取相应的权限
                requestPermissions(permission.toArray(new String[permission.size()]), 101);
            } else {
                //已经获取了全部的权限
                welcomeInit();
            }
        } else {
            //低于6.0版本不需要动态获取权限
            welcomeInit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                Log.e(TAG, "权限获取之后====permissions===" + Arrays.toString(permissions) + "====grantResults====" + Arrays.toString(permissions));
                welcomeInit();
                break;
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
