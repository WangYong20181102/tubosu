package com.tobosu.mydecorate.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.bean._AppConfig;
import com.tobosu.mydecorate.database.ChannelManage;
import com.tobosu.mydecorate.entity.DecorateTitleEntity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.AppInfoUtil;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.SpUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dec on 2016/9/12.
 * <p>
 * 1.基础数据同步
 * 2.新版本检测
 * 修改好的
 */
public class WelcomeActivity extends BaseActivity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private Context context;
    private OKHttpUtil okHttpUtil;
    private Gson mGson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        context = WelcomeActivity.this;
        okHttpUtil = new OKHttpUtil();
        mGson = new Gson();
        initBaidu();
        needPermissions();
    }

    private void needPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permission = getPermissionList(context);//需要获取动态权限的集合 总6个
            if (permission.size() > 0) {
                //未获取全部的权限 去获取相应的权限
                requestPermissions(permission.toArray(new String[permission.size()]), 101);
            } else {
                //已经获取了全部的权限
                allInit();
            }
        } else {
            //低于6.0版本不需要动态获取权限
            allInit();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                Log.e(TAG, "权限获取之后====permissions===" + Arrays.toString(permissions) + "====grantResults====" + Arrays.toString(permissions));
                allInit();
                break;
        }
    }
    //检测权限问题之后初始化所有

    private void allInit() {
        initUmemgSettings();
        initRegisterXinGe();
        // TODO: 2018/3/6 App点击流统计初始化
        // TODO: 2018/3/5 初始化上报数据
        initStatisticsEvent();
        // TODO: 2018/2/27 开始倒计时传数据
        Util.sendEventByTimeKill();
        //获取配置信息
        getAppConfig();
        //初始化数据
        initData();
    }

    //获取连接
    private void getAppConfig() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("type", "1");
        param.put("subchannel", "android");
        param.put("chcode", AppInfoUtil.getChannType(context));
        okHttpUtil.post(Constant.GET_APP_CONFIG, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "获取App配置信息连接成功=======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String data = jsonObject.optString("data");
                    _AppConfig mAppConfig = mGson.fromJson(data, _AppConfig.class);
                    //存储发单链接
                    HashMap<String, String> urlMap = new HashMap<>();
                    for (int i = 0; i < mAppConfig.getOrder_links().size(); i++) {
                        urlMap.put(mAppConfig.getOrder_links().get(i).getCode(), mAppConfig.getOrder_links().get(i).getUrl());
                    }
                    //存储数据
                    if (urlMap.containsKey("zhjaj01") && !TextUtils.isEmpty(urlMap.get("zhjaj01"))) {
                        SpUtil.setzhjaj01(mContext, urlMap.get("zhjaj01"));
                    }
                    if (urlMap.containsKey("zhjaj02") && !TextUtils.isEmpty(urlMap.get("zhjaj02"))) {
                        SpUtil.setzhjaj02(mContext, urlMap.get("zhjaj02"));
                    }
                    if (urlMap.containsKey("zhjaj03") && !TextUtils.isEmpty(urlMap.get("zhjaj03"))) {
                        SpUtil.setzhjaj03(mContext, urlMap.get("zhjaj03"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
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

    private void initData() {

        if (Util.isNetAvailable(context)) {
            initCityData(); // 获取城市英文simpleName
            getDecorateTitle();
//            requestForIndext();
        } else {
            //TODO 无网络提醒去设置网络
            CustomDialog.Builder builder = new CustomDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage("网络已经断开啦 o(╯□╰)o");

            builder.setNegativeButton("设置好了", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    paramDialogInterface.cancel();
                    initData();
                }
            });
            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    } else {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
            });

            builder.create().show();
            return;
        }


    }


    private void initUmemgSettings() {
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        MobclickAgent.startWithConfigure(MobclickAgent.UMAnalyticsConfig(Constant.UMENG_APP_KEY, Constant.CHANNEL_TYPE));
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, Constant.UMENG_APP_KEY, Util.getChannType(MyApplication.getContexts()));
        MobclickAgent.startWithConfigure(config);
    }


    // 先保留 版本升级方法
//    private void initHomeTypeData() {
//        MobclickAgent.onProfileSignIn(Util.getUserId(this));
//
//        CheckUpdateUtils checkUtils = new CheckUpdateUtils(context);
//        checkUtils.checkNewVersion();
//        // 在方法中开启线程下载和更新apk
//        if (MyApplication.HAS_NEW_VERSION) {
//            checkUtils.startUpdata();
//        }
//
//    }

    private void httpGetHomeFragMessage() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", Util.getDateToken());
        okHttpUtil.post(Constant.HOME_FRAGMENT_URL, hashMap, new OKHttpUtil.BaseCallBack() {

            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        Log.e(TAG, "Wecome请求成功====" + json);
                        String data = jsonObject.getString("data");
                        //将data储存在sp中 方便在后续取得数据
                        getSharedPreferences("HomeFragMessage", 0).edit().putString("dataJson", data).commit();
                        // 页面跳转
                        MyThread myThread = new MyThread();
                        myThread.start();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                Util.setErrorLog(TAG, "cao------请求失败->>" + request.toString());
            }

            @Override
            public void onError(Response response, int code) {
                Util.setErrorLog(TAG, "cao-----请求错误->>" + response.message());
            }
        });
    }

    /**
     * 请求网络返回栏目列表
     */
    private ArrayList<DecorateTitleEntity.ChannelItem> userNetChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();

    /**
     * 本地用户栏目列表 【显示】
     */
    private ArrayList<DecorateTitleEntity.ChannelItem> userChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();

    /**
     * 本地用户栏目列表 【不显示】
     */
    private ArrayList<DecorateTitleEntity.ChannelItem> userUnShowChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();

    /**
     * 本地总
     */
    private ArrayList<DecorateTitleEntity.ChannelItem> localChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();

    /**
     * 需要本地增加的列表
     */
    private ArrayList<DecorateTitleEntity.ChannelItem> addChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();
    private ChannelManage channelManager = ChannelManage.getManage(MyApplication.getApp().getSQLHelper());
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 作比较
                    int netSize = userNetChannelList.size();
                    int showSize = userChannelList.size();
                    int hideSize = userUnShowChannelList.size();
                    int localSize = showSize + hideSize;

                    localChannelList.clear();
                    localChannelList.addAll(userChannelList);
                    localChannelList.addAll(userUnShowChannelList);

                    if (netSize >= localSize) {
                        // 增加 或不增加
                        for (int i = 0; i < netSize; i++) {
                            for (int j = 0; j < localSize; j++) {
                                if (userNetChannelList.get(i).getName().equals(localChannelList.get(j).getName())) {
                                    // 在本地显示的title中有相同
                                    break;
                                } else {
                                    addChannelList.add(userNetChannelList.get(i));
                                }
                            }
                        }

                        // 添加到 本地 userChannelList数据库
                        channelManager.setDefaultData(addChannelList);
                    } else {
                        // 服务端若删除了某个title
                        for (int i = 0; i < localSize; i++) {
                            for (int j = 0; j < netSize; j++) {
                                if (localChannelList.get(i).getName().equals(userNetChannelList.get(j).getName())) {
                                    // 在本地显示的title中有相同 证明还存在
                                    break;
                                } else {
                                    channelManager.deleteAllChannel();
                                    getDecorateTitle();
                                }
                            }
                        }
                    }

                    httpGetHomeFragMessage();//获取首页信息
                    break;

                case 203:
                    httpGetHomeFragMessage();//获取首页信息
                    break;
            }
        }
    };

    /**
     * 测试用
     */
    private void requestForIndext() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", Util.getDateToken());
        Util.setErrorLog(TAG, "cao----有没有token->>" + Util.getDateToken());
        okHttpUtil.post(Constant.HOME_FRAGMENT_URL, hashMap, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Util.setErrorLog(TAG, "--cao-->>" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    Log.e(TAG, "Wecome请求成功====" + status);
                    if (status.equals("200")) {
                        Util.setErrorLog(TAG, "cao---2---");
                        Log.e(TAG, "Wecome请求成功====" + json);
                        String data = jsonObject.getString("data");
                        //将data储存在sp中 方便在后续取得数据
                        getSharedPreferences("HomeFragMessage", 0).edit().putString("dataJson", data).commit();
                        // 页面跳转
                        MyThread myThread = new MyThread();
                        myThread.start();
                    } else {
                        Util.setErrorLog(TAG, "cao---3---");
                    }
                } catch (JSONException e) {
                    Util.setErrorLog(TAG, "cao---4---");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }


    private void goNextActivity() {
        // 直接进入发单界面
        // 是否第一次进入发单页面
        if ("".equals(CacheManager.getFirstPopOrder(context))) { //  true 把true换成这个
            // 第一次进入发单页面
            startActivity(new Intent(context, PopOrderActivity.class));
        } else {
            startActivity(new Intent(context, MainActivity.class));
        }

        finish();
        System.gc();
    }


    private class MyThread extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                goNextActivity();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRegisterXinGe() {

        Context ctx = getApplicationContext();
        XGPushManager.registerPush(ctx, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.e(TAG, "注册成功, token -> " + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.e(TAG, "注册失败, code - " + i + " 失败信息 -> " + s);
            }
        });

        // 在XGPushManager.registerPush(context)或其它版本的注册接口之后调用以下代码
        // 使用ApplicationContext
//        Context context = getApplicationContext();
        Intent service = new Intent(ctx, XGPushService.class);
        ctx.startService(service);
    }

    private String city_url = Constant.ZHJ + "tapp/util/getCityList";

    private void initCityData() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        okHttpUtil.get(city_url, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(com.squareup.okhttp.Response response, String json) {
                System.out.println("city数据请求成功");
                getSharedPreferences("City_Data_Json_SP", Context.MODE_PRIVATE).edit().putString("city_data_json", json).commit();
            }

            @Override
            public void onFail(com.squareup.okhttp.Request request, IOException e) {

            }

            @Override
            public void onError(com.squareup.okhttp.Response response, int code) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ArrayList<DecorateTitleEntity.ChannelItem> titleList = new ArrayList<DecorateTitleEntity.ChannelItem>();

    private void getDecorateTitle() {
        if (ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).getUserChannel().size() == 0) {
            if (Util.isNetAvailable(context)) {
                OKHttpUtil okHttpUtil = new OKHttpUtil();
                HashMap<String, Object> titleParam = new HashMap<String, Object>();
                titleParam.put("token", Util.getDateToken()); // 此token和用户登录后获得的token无关

                okHttpUtil.post(Constant.COMMON_ARTICLE_LIST_TOP_TITLE_URL, titleParam, new OKHttpUtil.BaseCallBack() {
                    @Override
                    public void onSuccess(com.squareup.okhttp.Response response, String json) {

                        Gson gson = new Gson();
                        DecorateTitleEntity titleEntity = gson.fromJson(json, DecorateTitleEntity.class);
                        if (titleEntity.getStatus() == 200) {
                            int size = titleEntity.getData().size();
                            DecorateTitleEntity.ChannelItem item;
                            if (size > 0) {
                                titleList.add(new DecorateTitleEntity.ChannelItem(96, "推荐", 96, 1));
                                titleList.add(new DecorateTitleEntity.ChannelItem(97, "本地", 97, 1));
                                for (int i = 0; i < size; i++) {
                                    item = new DecorateTitleEntity.ChannelItem(titleEntity.getData().get(i).getId(),
                                            titleEntity.getData().get(i).getName(), i + 1, 0);
                                    titleList.add(item);
                                }

                                ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).setDefaultData(titleList);
                                goNextStep();
                            }
                        }
                    }

                    @Override
                    public void onFail(com.squareup.okhttp.Request request, IOException e) {
                        Util.setToast(context, "获取装修宝典标题失败");
                    }

                    @Override
                    public void onError(com.squareup.okhttp.Response response, int code) {
                        Util.setToast(context, "获取装修宝典标题失败");
                    }
                });
            } else {
                Util.setToast(context, "网络不佳");
            }
        } else {
            goNextStep();
        }

    }

    private void goNextStep() {
        checkTitleChange();
    }


    /**
     * 检查有没有增加和减少标题
     */
    private void checkTitleChange() {
        HashMap<String, Object> titleParam = new HashMap<String, Object>();
        titleParam.put("token", Util.getDateToken()); // 此token和用户登录后获得的token无关
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        okHttpUtil.post(Constant.COMMON_ARTICLE_LIST_TOP_TITLE_URL, titleParam, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Util.setLog(TAG, json);
                Gson gson = new Gson();
                DecorateTitleEntity titleEntity = gson.fromJson(json, DecorateTitleEntity.class);
                if (titleEntity.getStatus() == 200) {
                    int size = titleEntity.getData().size();
                    DecorateTitleEntity.ChannelItem item;
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            item = new DecorateTitleEntity.ChannelItem(titleEntity.getData().get(i).getId(),
                                    titleEntity.getData().get(i).getName(), i + 1, 0);
                            userNetChannelList.add(item);
                        }
                        handler.sendEmptyMessage(1);
                    }
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                // 网络不好 重试！
                handler.sendEmptyMessage(203);
            }

            @Override
            public void onError(Response response, int code) {
                // 网络不好 重试！
                handler.sendEmptyMessage(203);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(context);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initBaidu() {
        SDKInitializer.initialize(getApplicationContext());
        initLocationSetting();
    }

    private void initLocationSetting() {
        try {
            mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
            mLocationClient.start();

            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
            int span = 1000;
            option.setIsNeedAddress(true);
            option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            option.setOpenGps(true);//可选，默认false,设置是否使用gps
            option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
            option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
            mLocationClient.setLocOption(option);

            if (mLocationClient != null && mLocationClient.isStarted()) {
                System.out.println("--WelcomeActivity-->>" + mLocationClient.requestLocation());
            } else {
                System.out.println("WelcomeActivity === locClient is null or not started");
            }


            mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String locationCity = "";
    private LocationClient mLocationClient = null;

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            AppInfoUtil.setLat(context, location.getLatitude() + "");
            AppInfoUtil.setLng(context, location.getLongitude() + "");
            locationCity = location.getCity();
            if (locationCity != null) {
                locationCity = locationCity.replaceAll("[^\u4E00-\u9FA5]", "");
//                System.out.println("============WelcomeActivity -- city>>>" + locationCity +"<<<");
                CacheManager.setCity(getApplicationContext(), locationCity);
            }


            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
//            System.out.print("------->>" + sb);
        }
    }
}
