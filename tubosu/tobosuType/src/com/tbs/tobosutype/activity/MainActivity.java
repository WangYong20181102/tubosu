package com.tbs.tobosutype.activity;

import android.Manifest;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;

import com.tbs.tobosutype.bean._AppConfig;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.HomeListener;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;

import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * 网络好时
     */
    private static final int GOOD_NETWORK_STATE = 0x000121;

    /**
     * 网络不佳
     */
    private static final int BAD_NETWORK_STATE = 0x000122;

    private Context mContext;

    private static TabHost tabHost;
    private int tabPosition = -1;

    private ImageView img_home, img_image, img_decorate, img_my;
    private TextView tv_home_textview, tv_image_textview, tv_decorate_textview, tv_my_textview;

    /**
     * 首页 按钮
     */
    private static RelativeLayout main_tab_home;

    /**
     * 逛图库 按钮
     */
    private static RelativeLayout main_tab_image;

    /**
     * 找装修 按钮
     */
    private static RelativeLayout main_tab_decorate;

    public static boolean isForeground = false;
    /**
     * 我 按钮
     */
    private static RelativeLayout main_tab_my;

    /**
     * 0 未登录标记
     * 1 业主标记
     * 3 装修公司标记
     */
    private String mark = "0";

    /**
     * 底部[我]的红色订单数提示
     */
    private TextView not_see_orders_count;

    private String token;

    /**
     * 在[我]按钮 显示订单数目的接口地址
     */
    private String requsetUrl = Constant.TOBOSU_URL + "tapp/user/my";

    private HashMap<String, Object> requestParams;

    /**
     * 存储 用户信息
     */
    private SharedPreferences userInfo;

    /**
     * 若无网络时的顶部提示
     */
    private RelativeLayout rl_checkNet;

    private NetStateReceiver receiver;
    private int mTime = 0;

    private Handler netStateHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOOD_NETWORK_STATE:
                    rl_checkNet.setVisibility(View.GONE);
                    break;

                case BAD_NETWORK_STATE:
                    rl_checkNet.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "MainActivity执行的生命周期========onCreate()");
        mContext = MainActivity.this;

        userInfo = getSharedPreferences("userInfo", 0);
        mGson = new Gson();
        needPermissions();//权限的遍历
        initReceiver();
        initView();//初始化信息
        initEvent();
        HttpUserIsChangePassWord();//用户修改密码
        clearUserInfoWithAppUpdata();//App更新清除数据
        initJpush();
        initPushEvent();//推送
        getAppConfigOnNet();//获取相关的配置
    }

    //获取App配置信息
    private void getAppConfigOnNet() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("type", "1");
        OKHttpUtil.post(Constant.GET_CONFIG, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "获取配置信息失败==========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "获取App配置信息=================" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String data = jsonObject.optString("data");
                    _AppConfig mAppConfig = mGson.fromJson(data, _AppConfig.class);
                    //存储App配置信息
                    SpUtil.setCustom_service_tel(mContext, mAppConfig.getCustom_service_tel());//电话
                    SpUtil.setCustom_service_qq(mContext, mAppConfig.getCustom_service_qq());//QQ
                    SpUtil.setApplets_name(mContext, mAppConfig.getApplets_name());//土拨鼠查单小程序
                    SpUtil.setPublic_number(mContext, mAppConfig.getOfficial_accounts());//土拨鼠微信公众号
                    //存储发单链接信息
                    HashMap<String, String> urlMap = new HashMap<>();
                    for (int i = 0; i < mAppConfig.getOrder_links().size(); i++) {
                        urlMap.put(mAppConfig.getOrder_links().get(i).getCode(), mAppConfig.getOrder_links().get(i).getUrl());
                    }
                    //存储
                    if (urlMap.containsKey("tbsaj01") &&
                            !TextUtils.isEmpty(urlMap.get("tbsaj01"))) {
                        SpUtil.setTbsAj01(mContext, urlMap.get("tbsaj01"));
                    }
                    if (urlMap.containsKey("tbsaj02") &&
                            !TextUtils.isEmpty(urlMap.get("tbsaj02"))) {
                        SpUtil.setTbsAj02(mContext, urlMap.get("tbsaj02"));
                    }
                    if (urlMap.containsKey("tbsaj03") &&
                            !TextUtils.isEmpty(urlMap.get("tbsaj03"))) {
                        SpUtil.setTbsAj03(mContext, urlMap.get("tbsaj03"));
                    }
                    if (urlMap.containsKey("tbsaj04") &&
                            !TextUtils.isEmpty(urlMap.get("tbsaj04"))) {
                        SpUtil.setTbsAj04(mContext, urlMap.get("tbsaj04"));
                    }
                    if (urlMap.containsKey("tbsaj05") &&
                            !TextUtils.isEmpty(urlMap.get("tbsaj05"))) {
                        SpUtil.setTbsAj05(mContext, urlMap.get("tbsaj05"));
                    }
                    if (urlMap.containsKey("tbsaj06")
                            && !TextUtils.isEmpty(urlMap.get("tbsaj06"))) {
                        SpUtil.setTbsAj06(mContext, urlMap.get("tbsaj06"));
                    }
                    if (urlMap.containsKey("tbsaj07")
                            && !TextUtils.isEmpty(urlMap.get("tbsaj07"))) {
                        SpUtil.setTbsAj07(mContext, urlMap.get("tbsaj07"));
                    }
                    if (urlMap.containsKey("tbsaj08")
                            && !TextUtils.isEmpty(urlMap.get("tbsaj08"))) {
                        SpUtil.setTbsAj08(mContext, urlMap.get("tbsaj08"));
                    }
                    if (urlMap.containsKey("tbsaj09") && !TextUtils.isEmpty(urlMap.get("tbsaj09"))) {
                        SpUtil.setTbsAj09(mContext, urlMap.get("tbsaj09"));
                    }
                    if (urlMap.containsKey("tbsaj10") && !TextUtils.isEmpty(urlMap.get("tbsaj10"))) {
                        SpUtil.setTbsAj10(mContext, urlMap.get("tbsaj10"));
                    }
                    if (urlMap.containsKey("tbsaj11") && !TextUtils.isEmpty(urlMap.get("tbsaj11"))) {
                        SpUtil.setTbsAj11(mContext, urlMap.get("tbsaj11"));
                    }
                    if (urlMap.containsKey("tbsaj12") && !TextUtils.isEmpty(urlMap.get("tbsaj12"))) {
                        SpUtil.setTbsAj12(mContext, urlMap.get("tbsaj12"));
                    }
                    if (urlMap.containsKey("tbsaj13") && !TextUtils.isEmpty(urlMap.get("tbsaj13"))) {
                        SpUtil.setTbsAj13(mContext, urlMap.get("tbsaj13"));
                    }
                    if (urlMap.containsKey("tbsaj14") && !TextUtils.isEmpty(urlMap.get("tbsaj14"))) {
                        SpUtil.setTbsAj14(mContext, urlMap.get("tbsaj14"));
                    }
                    if (urlMap.containsKey("tbsaj15") && !TextUtils.isEmpty(urlMap.get("tbsaj15"))) {
                        SpUtil.setTbsAj15(mContext, urlMap.get("tbsaj15"));
                    }
                    if (urlMap.containsKey("tbsaj16") && !TextUtils.isEmpty(urlMap.get("tbsaj16"))) {
                        SpUtil.setTbsAj16(mContext, urlMap.get("tbsaj16"));
                    }
                    if (urlMap.containsKey("tbsaj17") && !TextUtils.isEmpty(urlMap.get("tbsaj17"))) {
                        SpUtil.setTbsAj17(mContext, urlMap.get("tbsaj17"));
                    }
                    if (urlMap.containsKey("tbsaj18") && !TextUtils.isEmpty(urlMap.get("tbsaj18"))) {
                        SpUtil.setTbsAj18(mContext, urlMap.get("tbsaj18"));
                    }
                    if (urlMap.containsKey("tbsaj19") && !TextUtils.isEmpty(urlMap.get("tbsaj19"))) {
                        SpUtil.setTbsAj19(mContext, urlMap.get("tbsaj19"));
                    }
                    if (urlMap.containsKey("tbsaj20") && !TextUtils.isEmpty(urlMap.get("tbsaj20"))) {
                        SpUtil.setTbsAj20(mContext, urlMap.get("tbsaj20"));
                    }
                    if (urlMap.containsKey("tbsaj21") && !TextUtils.isEmpty(urlMap.get("tbsaj21"))) {
                        SpUtil.setTbsAj21(mContext, urlMap.get("tbsaj21"));
                    }
                    if (urlMap.containsKey("tbsaj22") && !TextUtils.isEmpty(urlMap.get("tbsaj22"))) {
                        SpUtil.setTbsAj22(mContext, urlMap.get("tbsaj22"));
                    }
                    if (urlMap.containsKey("tbsaj23") && !TextUtils.isEmpty(urlMap.get("tbsaj23"))) {
                        SpUtil.setTbsAj23(mContext, urlMap.get("tbsaj23"));
                    }
                    if (urlMap.containsKey("tbsaj24") && !TextUtils.isEmpty(urlMap.get("tbsaj24"))) {
                        SpUtil.setTbsAj24(mContext, urlMap.get("tbsaj24"));
                    }
                    if (urlMap.containsKey("tbsaj25") && !TextUtils.isEmpty(urlMap.get("tbsaj25"))) {
                        SpUtil.setTbsAj25(mContext, urlMap.get("tbsaj25"));
                    }
                    if (urlMap.containsKey("tbsaj26") && !TextUtils.isEmpty(urlMap.get("tbsaj26"))) {
                        SpUtil.setTbsAj26(mContext, urlMap.get("tbsaj26"));
                    }
                    if (urlMap.containsKey("tbsaj27") && !TextUtils.isEmpty(urlMap.get("tbsaj27"))) {
                        SpUtil.setTbsAj27(mContext, urlMap.get("tbsaj27"));
                    }
                    if (urlMap.containsKey("tbsaj28") && !TextUtils.isEmpty(urlMap.get("tbsaj28"))) {
                        SpUtil.setTbsAj28(mContext, urlMap.get("tbsaj28"));
                    }
                    if (urlMap.containsKey("tbsaj29") && !TextUtils.isEmpty(urlMap.get("tbsaj29"))) {
                        SpUtil.setTbsAj29(mContext, urlMap.get("tbsaj29"));
                    }
                    if (urlMap.containsKey("tbsaj30") && !TextUtils.isEmpty(urlMap.get("tbsaj30"))) {
                        SpUtil.setTbsAj30(mContext, urlMap.get("tbsaj30"));
                    }
                    if (urlMap.containsKey("tbsaj31") && !TextUtils.isEmpty(urlMap.get("tbsaj31"))) {
                        SpUtil.setTbsAj31(mContext, urlMap.get("tbsaj31"));
                    }
                    if (urlMap.containsKey("tbsaj32") && !TextUtils.isEmpty(urlMap.get("tbsaj32"))) {
                        SpUtil.setTbsAj32(mContext, urlMap.get("tbsaj32"));
                    }
                    if (urlMap.containsKey("tbsaj33") && !TextUtils.isEmpty(urlMap.get("tbsaj33"))) {
                        SpUtil.setTbsAj33(mContext, urlMap.get("tbsaj33"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //初始化极光推送
    private void initJpush() {
        Log.e(TAG, "注册极光推送=================");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //将推送的唯一标识存入本地
        SpUtil.setPushRegisterId(MyApplication.getContext(), JPushInterface.getRegistrationID(MyApplication.getContext()));
        Log.e(TAG, "获取的极光推送注册id=================" + JPushInterface.getRegistrationID(MyApplication.getContext()));
    }

    //定点推送相关
    private void initPushEvent() {
        if (!TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
            //用户已经登录
            HashMap<String, Object> param = new HashMap<>();
            param.put("user_id", AppInfoUtil.getId(mContext));
            param.put("user_type", AppInfoUtil.getTypeid(mContext));
            param.put("system_type", "1");
            param.put("app_type", "1");
            param.put("device_id", SpUtil.getPushRegisterId(mContext));
            OKHttpUtil.post(Constant.FLUSH_SMS_PUSH, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "链接失败===============" + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = new String(response.body().string());
                    Log.e(TAG, "推送相关数据链接成功===========" + json);
                }
            });
        }
    }

    // TODO: 2018/2/27 点击事件流所需要的执行的操作
    @Override
    protected void onResume() {
        super.onResume();

        setFragmentPosition(SpUtil.getMainTabPosition(mContext));
        isForeground = true;
        initData();
        if (AppInfoUtil.ISJUSTLOGIN) {
            operTab();
        }
        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        isForeground = false;
        super.onPause();
        MobclickAgent.onPause(this);
    }

    //版本更新清除之前的用户信息
    private void clearUserInfoWithAppUpdata() {
        if (AppInfoUtil.getAppVersionName(mContext).equals("3.7")) {
            if (TextUtils.isEmpty(SpUtil.getCleanUserInfoFlag(mContext))) {
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                SpUtil.setCleanUserInfoFlag(mContext, "isClear");
            }
        }
    }

    //判断用户是否修改了密码
    private void HttpUserIsChangePassWord() {
        if (!TextUtils.isEmpty(AppInfoUtil.getUserMd5PassWord(mContext)) && !TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
            HashMap<String, Object> param = new HashMap<>();
            param.put("token", Util.getDateToken());
            param.put("uid", AppInfoUtil.getUserid(mContext));
            param.put("password", AppInfoUtil.getUserMd5PassWord(mContext));
            OKHttpUtil.post(Constant.CHECK_USER_PASSWORD_IS_CHANGE, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = new String(response.body().string());
//                    Log.e(TAG, "检测用户是否修改密码返回的结果================" + json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String ststus = jsonObject.optString("status");

                        if (ststus.equals("200")) {
                            //数据获取成功
                            JSONObject data = jsonObject.getJSONObject("data");
                            String result = data.optString("result");
                            if (result.equals("1")) {
                                //用户已经修改了账号的密码 清空用户的信息
                                getSharedPreferences("userInfo", 0).edit().clear().commit();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    /***
     * 初始化页面和底部按钮
     */
    private void initView() {

        not_see_orders_count = (TextView) findViewById(R.id.not_see_orders_count);
        rl_checkNet = (RelativeLayout) findViewById(R.id.rl_checkNet);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;

        Intent intent = new Intent().setClass(this, NewHomeActivity.class);

        spec = tabHost.newTabSpec("ONE").setIndicator("首页").setContent(intent);
        tabHost.addTab(spec);
        if (Build.VERSION.SDK_INT >= 21) {
            intent = new Intent().setClass(this, NewImageActivity.class);
        } else {
            intent = new Intent().setClass(this, ImageNewActivity.class);
        }

        spec = tabHost.newTabSpec("TWO").setIndicator("效果图").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, NewGongSiAcitivity.class);
        spec = tabHost.newTabSpec("THREE").setIndicator("装修公司").setContent(intent);
        tabHost.addTab(spec);

        /* ------------------------------------
         * 根据情况不同，显示不同   FOUR.未登录，显示登陆界面
         *                         FOUR2.登陆的是装修公司界面
         *                         FOUR3.登陆的是业主界面
         **/
//        intent = new Intent().setClass(this, MyActivity.class);
        intent = new Intent().setClass(this, NoneLoginOfMineActivity.class);//3.7新增
        spec = tabHost.newTabSpec("FOUR").setIndicator("我").setContent(intent);
        tabHost.addTab(spec);

//        intent = new Intent().setClass(this, MyCompanyActivity.class);
        intent = new Intent().setClass(this, CompanyOfMineActivity.class);//3.7新增
        spec = tabHost.newTabSpec("FOUR2").setIndicator("我").setContent(intent);
        tabHost.addTab(spec);

//        intent = new Intent().setClass(this, MyOwnerActivity.class);
        intent = new Intent().setClass(this, CustomOfMineActivity.class);//3.7新增
        spec = tabHost.newTabSpec("FOUR3").setIndicator("我").setContent(intent);
        tabHost.addTab(spec);
        /*---------------------------------------*/

        main_tab_home = (RelativeLayout) this.findViewById(R.id.main_tab_first);
        main_tab_home.setOnClickListener(this);
        main_tab_image = (RelativeLayout) this.findViewById(R.id.main_tab_image);
        main_tab_image.setOnClickListener(this);
        // TODO: 2017/12/16  专门用来做测试
        main_tab_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Intent gotoLearnActivity = new Intent(mContext, AllOrderActivity.class);
////                gotoLearnActivity.putExtra("mLoadingUrl", "http://m.dev.tobosu.com/szs/zx/?app_type=1");
//                mContext.startActivity(gotoLearnActivity);
                return true;
            }
        });
        main_tab_decorate = (RelativeLayout) this.findViewById(R.id.main_tab_decorate);
        main_tab_decorate.setOnClickListener(this);
        main_tab_my = (RelativeLayout) this.findViewById(R.id.main_tab_my);
        main_tab_my.setOnClickListener(this);

        img_home = (ImageView) findViewById(R.id.img_home);
        img_image = (ImageView) findViewById(R.id.img_image);
        img_decorate = (ImageView) findViewById(R.id.img_decorate);
        img_my = (ImageView) findViewById(R.id.img_my);

        tv_home_textview = (TextView) findViewById(R.id.tv_home_textview);
        tv_image_textview = (TextView) findViewById(R.id.tv_image_textview);
        tv_decorate_textview = (TextView) findViewById(R.id.tv_decorate_textview);
        tv_my_textview = (TextView) findViewById(R.id.tv_my_textview);

        setFragmentPosition(0);
        SpUtil.setMainTabPosition(mContext, 0);
    }


    private void initReceiver() {
        receiver = new NetStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.NET_STATE_ACTION);
        filter.addAction("goto_activity_zhuangxiu");
        filter.addAction("goto_activity_xiaoguotu");
        filter.addAction(Constant.LOGOUT_ACTION);
        registerReceiver(receiver, filter);
    }

    /**
     * 初始化数据  token
     * mark
     * [我]页面的订单数目
     */
    private void initData() {
        Log.e(TAG, "MainActivity=============执行========initData=====");
        token = AppInfoUtil.getToekn(getApplicationContext());
        mark = userInfo.getString("mark", "0");
        initNet();

        if ("5".equals(getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).getString("go_poporder_string", "0"))) {
            // 首次安装执行下面代码 否则略过
            Intent intent = getIntent();
            if (intent != null && intent.getExtras() != null) {
                Bundle b = intent.getExtras().getBundle("first_install_bundle");
                if (b != null) {
                    String isFirst = b.getString("first_install_string");
                    if ("10".equals(isFirst) || "11".equals(isFirst)) {
                        // sharedpreference若是5 表示首次安装 3则不是
                        getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).edit().putString("go_poporder_string", "3").commit();
                    }
                }
            }
        } else {
            // 从poporder过来
        }


    }

    /***
     * 底部按钮的点击事件
     */
    private void initEvent() {
        // 跳转网络设置
        rl_checkNet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                } else {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                }
            }
        });
    }


    private void setFragmentPosition(int fragmentPostion) {
        mark = userInfo.getString("mark", "0");
        switch (fragmentPostion) {
            case 0:
                tabHost.setCurrentTab(fragmentPostion);
                tabHost.setCurrentTabByTag("ONE");

                img_home.setBackgroundResource(R.drawable.home_orange);
                img_image.setBackgroundResource(R.drawable.tu_black);
                img_decorate.setBackgroundResource(R.drawable.company_black);
                img_my.setBackgroundResource(R.drawable.me_black);

                tv_home_textview.setTextColor(Color.parseColor("#ff9c00"));
                tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_my_textview.setTextColor(Color.parseColor("#A8AAAC"));
                SpUtil.setMainTabPosition(mContext, 0);
                break;
            case 1:
                tabHost.setCurrentTab(fragmentPostion);
                tabHost.setCurrentTabByTag("TWO");
                img_home.setBackgroundResource(R.drawable.home_black);
                img_image.setBackgroundResource(R.drawable.tu_orange);
                img_decorate.setBackgroundResource(R.drawable.company_black);
                img_my.setBackgroundResource(R.drawable.me_black);

                tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_image_textview.setTextColor(Color.parseColor("#ff9c00"));
                tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_my_textview.setTextColor(Color.parseColor("#A8AAAC"));
                SpUtil.setMainTabPosition(mContext, 1);
                break;
            case 2:
                tabHost.setCurrentTab(fragmentPostion);
                tabHost.setCurrentTabByTag("THREE");
                img_home.setBackgroundResource(R.drawable.home_black);
                img_image.setBackgroundResource(R.drawable.tu_black);
                img_decorate.setBackgroundResource(R.drawable.company_orange);
                img_my.setBackgroundResource(R.drawable.me_black);

                tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_decorate_textview.setTextColor(Color.parseColor("#ff9c00"));
                tv_my_textview.setTextColor(Color.parseColor("#A8AAAC"));
                SpUtil.setMainTabPosition(mContext, 2);
                break;
            case 3:
                tabHost.setCurrentTab(fragmentPostion);
                Util.setLog(TAG, "从loginactivity返回的position是 " + tabPosition);

                /*
                 * 0 未登录标记     FOUR.未登录
                 * 1 业主标记       FOUR2.登陆的是装修公司界面
                 * 3 装修公司标记   FOUR3.登陆的是业主界面
                 */
                if ("1".equals(mark)) {
                    // 业主
                    tabHost.setCurrentTabByTag("FOUR3");
                    tabPosition = 3;
                    img_home.setBackgroundResource(R.drawable.home_black);
                    img_image.setBackgroundResource(R.drawable.tu_black);
                    img_decorate.setBackgroundResource(R.drawable.company_black);
                    img_my.setBackgroundResource(R.drawable.me_orange);

                    tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_my_textview.setTextColor(Color.parseColor("#ff9c00"));
                } else if ("3".equals(mark)) {
                    // 装修公司
                    tabHost.setCurrentTabByTag("FOUR2");
                    tabPosition = 3;
                    img_home.setBackgroundResource(R.drawable.home_black);
                    img_image.setBackgroundResource(R.drawable.tu_black);
                    img_decorate.setBackgroundResource(R.drawable.company_black);
                    img_my.setBackgroundResource(R.drawable.me_orange);

                    tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_my_textview.setTextColor(Color.parseColor("#ff9c00"));
                } else if ("0".equals(mark)) {
                    Util.setLog(TAG, "==========mark 0 =========");
                    tabHost.setCurrentTab(fragmentPostion);
                    tabHost.setCurrentTabByTag("FOUR");

                    img_home.setBackgroundResource(R.drawable.home_black);
                    img_image.setBackgroundResource(R.drawable.tu_black);
                    img_decorate.setBackgroundResource(R.drawable.company_black);
                    img_my.setBackgroundResource(R.drawable.me_orange);

                    tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_my_textview.setTextColor(Color.parseColor("#ff9c00"));
                }
                SpUtil.setMainTabPosition(mContext, 3);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0x000018:
                Bundle b = data.getBundleExtra("backBundle");
                setFragmentPosition(b.getInt("back"));
                break;
        }
    }


    /**
     * 检测网络状态<br/>
     *
     * @return true 连接网络
     * false 没有网络
     */
    public boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 根据不同mark标记 显示不同的[我]页面
     */
    private void operTab() {
        /*
         * 0 未登录标记     FOUR.未登录
         * 1 业主标记       FOUR2.登陆的是装修公司界面
         * 3 装修公司标记   FOUR3.登陆的是业主界面
         */
        if (AppInfoUtil.ISJUSTLOGIN) {
            if ("1".equals(mark)) {
                tabHost.setCurrentTabByTag("FOUR3");
            } else if ("3".equals(mark)) {
                tabHost.setCurrentTabByTag("FOUR2");
            } else {
                // 未登录
                tabHost.setCurrentTabByTag("FOUR");
            }
            AppInfoUtil.ISJUSTLOGIN = false;
        }
        initEvent();
    }

    /***
     * 检测网络状况 ；若登陆 则获取订单数目
     */
    private void initNet() {
        requestParams = AppInfoUtil.getPublicHashMapParams(mContext);
        if (mark.equals("0")) {
            not_see_orders_count.setVisibility(View.GONE);
            Log.d(TAG, "--MainActivity没有登陆--");
        } else {
            Log.d(TAG, "--MainActivity已经登陆--");
            requestParams.put("token", token);

            OKHttpUtil.post(requsetUrl, requestParams, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "请求错误，请稍后再试~");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, result);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getInt("error_code") == 0) {
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    // 系统消息只显示红点，订单数据要显示数目 两者共存的话，显示订单数目就是了
                                    int num = 0; //订单数目
                                    int sys_message_flag = 0;
                                    if (mark.equals("3")) {
                                        num = Integer.parseInt(jsonObject.getString("not_see_orders_count"));
                                        sys_message_flag = jsonObject.getInt("sysmesscount");
                                        Log.d(TAG, "--订单数目是[" + num + "]--");
                                        if (num > 0 && sys_message_flag >= 0) { // 订单有数目 有系统消息
                                            not_see_orders_count.setText(num + "");
//                                            not_see_orders_count.setVisibility(View.VISIBLE);
                                            not_see_orders_count.setVisibility(View.GONE);
                                        } else if (num <= 0 && sys_message_flag > 0) {
                                            not_see_orders_count.setText("");
//                                            not_see_orders_count.setVisibility(View.VISIBLE);
                                            not_see_orders_count.setVisibility(View.GONE);
                                        } else {
                                            not_see_orders_count.setVisibility(View.GONE);
                                        }
                                    } else if (mark.equals("1")) {
                                        sys_message_flag = jsonObject.getInt("sysmesscount");
                                        if (sys_message_flag > 0) {
//                                            not_see_orders_count.setVisibility(View.VISIBLE);
                                            not_see_orders_count.setVisibility(View.GONE);
                                        } else {
                                            not_see_orders_count.setVisibility(View.GONE);
                                        }
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    //判读用户是否有修改密码


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab_first:
                //点击首页按钮
                MobclickAgent.onEvent(mContext, "click_app_index");
                tabHost.setCurrentTabByTag("ONE");
                setFragmentPosition(0);
                SpUtil.setMainTabPosition(mContext, 0);
                tabPosition = 0;
                break;
            case R.id.main_tab_image:
                //点击了效果图
                MobclickAgent.onEvent(mContext, "click_app_pic_market");
                tabHost.setCurrentTabByTag("TWO");
                setFragmentPosition(1);
                SpUtil.setMainTabPosition(mContext, 1);
                tabPosition = 1;
                break;
            case R.id.main_tab_decorate:
                //点击了装修公司
                MobclickAgent.onEvent(mContext, "click_app_find_decoration");
                tabHost.setCurrentTabByTag("THREE");
                setFragmentPosition(2);
                SpUtil.setMainTabPosition(mContext, 2);
                tabPosition = 2;
                break;
            case R.id.main_tab_my:
                //点击了我的模块
                MobclickAgent.onEvent(mContext, "click_app_preson_center");
                if ("1".equals(mark)) {
                    tabHost.setCurrentTabByTag("FOUR3");
//                    setFragmentPosition(3);
                } else if ("3".equals(mark)) {
                    tabHost.setCurrentTabByTag("FOUR2");
//                    setFragmentPosition(3);
                    tabPosition = 3;
                } else if ("0".equals(mark)) {
                    tabHost.setCurrentTabByTag("FOUR");
//                    setFragmentPosition(3);
                    tabPosition = 3;
                }
                setFragmentPosition(3);
                SpUtil.setMainTabPosition(mContext, 3);
                break;
            default:
                break;
        }

        initData();
        operTab();
    }


    class NetStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.NET_STATE_ACTION)) {
                // 有网络时
                Bundle b = intent.getBundleExtra("Net_Flag_String_Bundle");
                Message m = new Message();
                String netString = b.getString("Net_Flag_message");
                if (netString.equals("Good_Net")) {
                    m.what = GOOD_NETWORK_STATE;
                } else if (netString.equals("Bad_Net")) {
                    m.what = BAD_NETWORK_STATE;
                }
                netStateHandler.sendMessage(m);
            }

            if (intent.getAction().equals("goto_activity_zhuangxiu")) {
                int position = intent.getIntExtra("position", -1);
                setFragmentPosition(position);
            }

            if (intent.getAction().equals("goto_activity_xiaoguotu")) {
                int position = intent.getIntExtra("position", -1);
                setFragmentPosition(position);
            }

//            // 以下跟网络无关 只是我不想再写一个广播类了
//            else if (intent.getAction().equals(Constant.LOGOUT_ACTION)) {
//                setFragmentPosition(0);
//            } else if (intent.getAction().equals(Constant.LOGIN_ACTION)) {
//                setFragmentPosition(3);
//            }
        }
    }

    private void needPermissions() {
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
