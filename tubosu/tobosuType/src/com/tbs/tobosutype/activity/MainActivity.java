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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.Util;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private RelativeLayout main_tab_home;

    /**
     * 逛图库 按钮
     */
    private static RelativeLayout main_tab_image;

    /**
     * 找装修 按钮
     */
    private static RelativeLayout main_tab_decorate;

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

    private RequestParams requestParams;

    /**
     * 存储 用户信息
     */
    private SharedPreferences userInfo;

    /**
     * 若无网络时的顶部提示
     */
    private RelativeLayout rl_checkNet;

    private NetStateReceiver receiver;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		AppInfoUtil.setActivityTheme(MainActivity.this);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        setContentView(R.layout.activity_main);
//        AppInfoUtil.setTranslucentStatus(this);
        mContext = MainActivity.this;

        userInfo = getSharedPreferences("userInfo", 0);

        initView();
        initReceiver();
        initEvent();
        needPermissions();
    }


    /***
     * 初始化页面和底部按钮
     */
    private void initView() {

        not_see_orders_count = (TextView) findViewById(R.id.not_see_orders_count);
        rl_checkNet = (RelativeLayout) findViewById(R.id.rl_checkNet);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;

        Intent intent = new Intent().setClass(this, HomeActivity.class);
        spec = tabHost.newTabSpec("ONE").setIndicator("首页").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ImageNewActivity.class);
        spec = tabHost.newTabSpec("TWO").setIndicator("逛图库").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, FindDecorateActivity.class);
        spec = tabHost.newTabSpec("THREE").setIndicator("找装修").setContent(intent);
        tabHost.addTab(spec);

		/* ------------------------------------
         * 根据情况不同，显示不同   FOUR.未登录，显示登陆界面；
		 *                        FOUR2.登陆的是装修公司界面
		 *                        FOUR3.登陆的是业主界面
		 **/
        intent = new Intent().setClass(this, MyActivity.class);
        spec = tabHost.newTabSpec("FOUR").setIndicator("我").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MyCompanyActivity.class);
        spec = tabHost.newTabSpec("FOUR2").setIndicator("我").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MyOwnerActivity.class);
        spec = tabHost.newTabSpec("FOUR3").setIndicator("我").setContent(intent);
        tabHost.addTab(spec);
        /*---------------------------------------*/


        // 默认显示第一个页面--首页
//        tabHost.setCurrentTab(0);


        // 底部控件
//        radioGroup = (RadioGroup) this.findViewById(R.id.main_tab_group);
        main_tab_home = (RelativeLayout) this.findViewById(R.id.main_tab_first);
        main_tab_home.setOnClickListener(this);
        main_tab_image = (RelativeLayout) this.findViewById(R.id.main_tab_image);
        main_tab_image.setOnClickListener(this);
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
    }


    private void initReceiver() {
        receiver = new NetStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.NET_STATE_ACTION);
//        filter.addAction(Constant.LOGIN_ACTION);
//        filter.addAction(Constant.LOGOUT_ACTION);
        registerReceiver(receiver, filter);
    }

    /**
     * 初始化数据  token
     * mark
     * [我]页面的订单数目
     */
    private void initData() {

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

                img_home.setBackgroundResource(R.drawable.menu_icon_0_pressed);
                img_image.setBackgroundResource(R.drawable.menu_icon_1_normal);
                img_decorate.setBackgroundResource(R.drawable.menu_icon_2_normal);
                img_my.setBackgroundResource(R.drawable.menu_icon_3_normal);

                tv_home_textview.setTextColor(Color.parseColor("#ff9c00"));
                tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_my_textview.setTextColor(Color.parseColor("#A8AAAC"));
                break;
            case 1:
                tabHost.setCurrentTab(fragmentPostion);
                tabHost.setCurrentTabByTag("TWO");
                img_home.setBackgroundResource(R.drawable.menu_icon_0_normal);
                img_image.setBackgroundResource(R.drawable.menu_icon_1_pressed);
                img_decorate.setBackgroundResource(R.drawable.menu_icon_2_normal);
                img_my.setBackgroundResource(R.drawable.menu_icon_3_normal);

                tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_image_textview.setTextColor(Color.parseColor("#ff9c00"));
                tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_my_textview.setTextColor(Color.parseColor("#A8AAAC"));
                break;
            case 2:
                tabHost.setCurrentTab(fragmentPostion);
                tabHost.setCurrentTabByTag("THREE");
                img_home.setBackgroundResource(R.drawable.menu_icon_0_normal);
                img_image.setBackgroundResource(R.drawable.menu_icon_1_normal);
                img_decorate.setBackgroundResource(R.drawable.menu_icon_2_pressed);
                img_my.setBackgroundResource(R.drawable.menu_icon_3_normal);

                tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                tv_decorate_textview.setTextColor(Color.parseColor("#ff9c00"));
                tv_my_textview.setTextColor(Color.parseColor("#A8AAAC"));
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
                    img_home.setBackgroundResource(R.drawable.menu_icon_0_normal);
                    img_image.setBackgroundResource(R.drawable.menu_icon_1_normal);
                    img_decorate.setBackgroundResource(R.drawable.menu_icon_2_normal);
                    img_my.setBackgroundResource(R.drawable.menu_icon_3_pressed);

                    tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_my_textview.setTextColor(Color.parseColor("#ff9c00"));
                } else if ("3".equals(mark)) {
                    // 装修公司
                    tabHost.setCurrentTabByTag("FOUR2");

                    tabPosition = 3;
                    img_home.setBackgroundResource(R.drawable.menu_icon_0_normal);
                    img_image.setBackgroundResource(R.drawable.menu_icon_1_normal);
                    img_decorate.setBackgroundResource(R.drawable.menu_icon_2_normal);
                    img_my.setBackgroundResource(R.drawable.menu_icon_3_pressed);

                    tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_my_textview.setTextColor(Color.parseColor("#ff9c00"));
                } else if ("0".equals(mark)) {
                    Util.setLog(TAG, "==========mark 0 =========");
                    tabHost.setCurrentTab(fragmentPostion);
                    tabHost.setCurrentTabByTag("FOUR");

                    img_home.setBackgroundResource(R.drawable.menu_icon_0_normal);
                    img_image.setBackgroundResource(R.drawable.menu_icon_1_normal);
                    img_decorate.setBackgroundResource(R.drawable.menu_icon_2_normal);
                    img_my.setBackgroundResource(R.drawable.menu_icon_3_pressed);

                    tv_home_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_image_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_decorate_textview.setTextColor(Color.parseColor("#A8AAAC"));
                    tv_my_textview.setTextColor(Color.parseColor("#ff9c00"));
                }
                break;
            default:
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


//	/***
//	 * 底部根据不同tab 选择不同页面
//	 * @param tab 0-->首页；
//	 *        tab 1-->逛图库；
//	 *        tab 2-->找装修公司；
//	 *        tab 3-->我
//	 */
//	public void setTab(int tab) {
//		tabHost.setCurrentTab(tab);
//	}

//	/***
//	 * 网络状况不佳的时候显示
//	 */
//	private void judgeNetWorkState(){
//		Message msg = new Message();
//		if(checkNetwork()){
//			msg.what = GOOD_NETWORK_STATE;
//		}else{
//			msg.what = BAD_NETWORK_STATE;
//		}
//		netStateHandler.sendMessage(msg);
//	}


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        if (AppInfoUtil.ISJUSTLOGIN) {
            operTab();
        }
        MobclickAgent.onResume(this);
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

    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
        MobclickAgent.onPause(this);
    }


    /***
     * 检测网络状况 ；若登陆 则获取订单数目
     */
    private void initNet() {
        requestParams = AppInfoUtil.getPublicParams(mContext);
        if (mark.equals("0")) {
            not_see_orders_count.setVisibility(View.GONE);
            Log.d(TAG, "--MainActivity没有登陆--");
        } else {
            Log.d(TAG, "--MainActivity已经登陆--");
            requestParams.put("token", token);

            HttpServer.getInstance().requestPOST(requsetUrl, requestParams, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                    String result = new String(body);
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
                                    not_see_orders_count.setVisibility(View.VISIBLE);
                                } else if (num <= 0 && sys_message_flag > 0) {
                                    not_see_orders_count.setText("");
                                    not_see_orders_count.setVisibility(View.VISIBLE);
                                } else {
                                    not_see_orders_count.setVisibility(View.GONE);
                                }
                            } else if (mark.equals("1")) {
                                sys_message_flag = jsonObject.getInt("sysmesscount");
                                if (sys_message_flag > 0) {
                                    not_see_orders_count.setVisibility(View.VISIBLE);
                                } else {
                                    not_see_orders_count.setVisibility(View.GONE);
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    Util.setToast(mContext, "请求错误，请稍后再试~");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab_first:
                MobclickAgent.onEvent(mContext, "click_app_index");
                tabHost.setCurrentTabByTag("ONE");
                setFragmentPosition(0);
                tabPosition = 0;
                break;
            case R.id.main_tab_image:
                MobclickAgent.onEvent(mContext, "click_app_pic_market");
                tabHost.setCurrentTabByTag("TWO");
                setFragmentPosition(1);
                tabPosition = 1;
                break;
            case R.id.main_tab_decorate:
                MobclickAgent.onEvent(mContext, "click_app_find_decoration");
                tabHost.setCurrentTabByTag("THREE");
                setFragmentPosition(2);
                tabPosition = 2;
                break;
            case R.id.main_tab_my:
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

//            // 以下跟网络无关 只是我不想再写一个广播类了
//            else if (intent.getAction().equals(Constant.LOGOUT_ACTION)) {
//                setFragmentPosition(0);
//            } else if (intent.getAction().equals(Constant.LOGIN_ACTION)) {
//                setFragmentPosition(3);
//            }
        }
    }

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
