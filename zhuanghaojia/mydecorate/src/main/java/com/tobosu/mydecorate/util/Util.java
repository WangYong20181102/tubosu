package com.tobosu.mydecorate.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.bean._AppEvent;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by dec on 2016/9/12.
 * 工具类
 */
public class Util {
    private static Gson mGson = new Gson();
    private static OKHttpUtil okHttpUtil = new OKHttpUtil();
    private static int mTime = 0;//上传用户信息流的倒计时时间


    public static void setErrorLog(String tag, String msg) {
        if (true) {
            Log.e(tag, msg);
        }

    }

    public static void setLog(String tag, String msg) {
        if (true) {
            Log.d(tag, msg);
        }

    }

    public static void setToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        if ("".equals(token)) {
            return false;//未登录
        } else {
            return true;//已登陆
        }
    }

    public static String getUserId(Context context) {
        return context.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("user_id", "");
    }

    //获取用户的身份标识
    public static String getUserMark(Context context) {
        return context.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("mark", "");
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {

        }
        return versionName;
    }


    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        setToast(context, "网络不可用");
        return false;
    }


    /**
     * 检测网络状态<br/>
     *
     * @return true 连接网络
     * false 没有网络
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
     * 无网提示
     *
     * @param mContext
     */
    public static void toastNetOut(Context mContext) {
        Toast.makeText(mContext, "网络断开，请检查网络~", Toast.LENGTH_SHORT).show();
    }


    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * @param context
     * @return -1：没有网络  1：WIFI网络2：wap网络3：net网络
     */
    public static int GetNetype(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }


    /***
     * 判断电话号码是否合法
     * @param mContext
     * @param phoneNum
     * @return
     */
    public static boolean judgePhone(Context mContext, String phoneNum) {
        phoneNum = phoneNum.replaceAll("-", "");

        if ("".equals(phoneNum)) {
            Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
            Pattern pattern = Pattern.compile(MOBILE);
            Matcher matcher = pattern.matcher(phoneNum);
            boolean flag = matcher.matches();
            if (!flag) {
                Toast.makeText(mContext, "请输入合法电话号码", Toast.LENGTH_SHORT).show();
            }
            return matcher.matches();
        }
    }


    /**
     * @param loadingView 加载视图
     * @param isLoading   正在加载中
     * @param isNet       是否有网络
     */
    public static void showDataAmin(View loadingView, boolean isLoading, boolean isNet) {
        if (isLoading && isNet) {
            loadingView.setVisibility(View.VISIBLE);
        } else if (isLoading == false && isNet) {
            loadingView.setVisibility(View.GONE);
        } else {

        }
    }

    /**
     * @param context
     * @param view
     * @param isNet   true 有网络  false 无网络
     */
    public static void showNetOutView(final Context context, View view, boolean isNet) {
        if (isNet) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.findViewById(R.id.tv_go_getnet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    } else {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
            });
        }
    }

    //获取渠道相关的信息
    public static String getChannType(Context context) {
        String mChannType = "";
        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (!TextUtils.isEmpty(applicationInfo.metaData.getString("UMENG_CHANNEL"))) {
                mChannType = applicationInfo.metaData.getString("UMENG_CHANNEL");
                return mChannType;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mChannType;
    }

    /**
     * 隐藏输入
     *
     * @param activity
     * @param edit
     */
    public static void hideKeyboard(FragmentActivity activity, EditText edit) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            edit.setInputType(InputType.TYPE_NULL);
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edit, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        edit.requestFocus();
    }

    /**
     * 获取当天的加密token
     * 加密规则：
     * 1.先将密码盐：zhj 进行md5加密；
     * 2.获取当天日期 如：2017-05-23；
     * 3.再对加密后的密码盐和当天日期进行md5加密；
     * 4.然后再对整体进行base64加密
     */
    public static String getDateToken() {
        String md5ZHJ = MD5Util.md5("zhj");//加密后的zhj
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String mTime = format.format(date);
        String s = MD5Util.md5(md5ZHJ + mTime);
        String dataToken = Base64Util.getBase64(s);
        return dataToken;
    }

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

    //获取Unix时间戳
    public static long getUnixTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        String t = df.format(d);
        long epoch = 0;
        try {
            epoch = df.parse(t).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epoch;
    }

    //获取手机的IMEI
    public static String getIMEI() {
        TelephonyManager TelephonyMgr = (TelephonyManager) MyApplication.getContexts().getSystemService(TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }

    //获取Android的 api   数据流中的ani
    public static String getAni() {
        return Settings.System.getString(MyApplication.getContexts().getContentResolver(), Settings.System.ANDROID_ID);
    }

    //获取设备的id Android:md5(ani+imei+mac)
    public static String getDeviceID() {
        return MD5Util.md5(Util.getAni() + Util.getIMEI() + MacUtils.getMobileMAC(MyApplication.getContexts()));
    }

    //生成大于6位的随机字符串
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //获取会话Id md5(di+current_time+random_str)
    public static String getSessionID() {
        return MD5Util.md5(Util.getDeviceID() + Util.getUnixTime() + Util.getRandomString(8));
    }

    //获取手机的分辨率
    public static String getPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) MyApplication.getContexts().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        String s = dm.widthPixels + "*" + dm.heightPixels;
        return s;
    }

    //获取设备的型号
    public static String getDeviceModel() {
        return Build.BRAND + "/" + android.os.Build.MODEL;
    }

    //获取手机的系统版本
    public static String getSystemVersion() {
        return "Android " + android.os.Build.VERSION.RELEASE;
    }

    //获取运营商 名称
    public static String getOperator() {

        String ProvidersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getContexts().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String IMSI = telephonyManager.getSubscriberId();
        Log.i("qweqwes", "运营商代码" + IMSI);
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                ProvidersName = "CMCC";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                ProvidersName = "CUCC";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "CTCC";
            }
            return ProvidersName;
        } else {
            return "WIFI";
        }
    }

    //获取IP
    public static String getIp(final Context context) {
        String ip = null;
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile 3G Data Network
        android.net.NetworkInfo.State mobile = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState();
        // wifi
        android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState();

        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobile == android.net.NetworkInfo.State.CONNECTED
                || mobile == android.net.NetworkInfo.State.CONNECTING) {
            ip = getLocalIpAddress();
        }
        if (wifi == android.net.NetworkInfo.State.CONNECTED
                || wifi == android.net.NetworkInfo.State.CONNECTING) {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = (ipAddress & 0xFF) + "." +
                    ((ipAddress >> 8) & 0xFF) + "." +
                    ((ipAddress >> 16) & 0xFF) + "." +
                    (ipAddress >> 24 & 0xFF);
        }
        return ip;

    }

    private static String getLocalIpAddress() {
        try {
            //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {//获取IPv4的IP地址
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * @param is_init      是否进行初始化
     * @param from_where   访问来源
     * @param now_activity 当前页面
     */
    public static void useStatisticsEventVistEvent(boolean is_init,
                                                   String from_where,
                                                   String now_activity) {
        if (is_init) {
            //初始化
            //设定访问时间
            SpUtil.setStatisticsEventVistTime(MyApplication.getContexts(), Util.getUnixTime());
            SpUtil.setStatisticsEventFromWhere(MyApplication.getContexts(), from_where);
        } else {
            //统计上传
            SpUtil.setStatisticsEventLeaveTime(MyApplication.getContexts(), Util.getUnixTime());
            //访问事件
            _AppEvent.EvBean evBean0 = new _AppEvent.EvBean(
                    SpUtil.getStatisticsEventFromWhere(MyApplication.getContexts()), now_activity, "",
                    SpUtil.getStatisticsEventVistTime(MyApplication.getContexts()),
                    SpUtil.getStatisticsEventLeaveTime(MyApplication.getContexts()), "0");
            Util.addAppEventCount(evBean0);
        }
    }

    //点击事件
    public static void useStatisticsEventClickEvent(String event_code, String now_activity) {
        if (!TextUtils.isEmpty(event_code)) {
            //统计点击事件
            _AppEvent.EvBean evBean1 = new _AppEvent.EvBean(
                    SpUtil.getStatisticsEventFromWhere(MyApplication.getContexts()),
                    now_activity, event_code,
                    Util.getUnixTime(),
                    Util.getUnixTime(), "1");
            Util.addAppEventCount(evBean1);
        }
    }

    // TODO: 2018/2/27 App点击流事件统计(添加事件到全局的集合中)
    public static void addAppEventCount(_AppEvent.EvBean evBean) {
        //添加事件
        MyApplication.evBeanArrayList.add(evBean);
//        Log.e(TAG, "添加事件成功=========" + evBean.getLt());
        if (MyApplication.evBeanArrayList.size() >= 50) {
            //上传数据
            mTime = 0;
            HttpPostUserUseInfo();
        }
    }
    //上传数据
    public static void HttpPostUserUseInfo() {
        //根据事件的集合生成要上传的对象
        _AppEvent appEvent = new _AppEvent(MyApplication.evBeanArrayList);
        if (!appEvent.getEv().isEmpty()) {
            if (appEvent.getEv().size() >= 2
                    && appEvent.getEv().get(1).getRef().equals("activity.WelcomeActivity")
                    && appEvent.getEv().get(1).getUrl().equals("activity.WelcomeActivity")
                    && appEvent.getEv().get(0).getRef().equals("")) {
                //修改数据
                appEvent.getEv().get(1).setRef("activity.LauncherActivity");
                appEvent.getEv().remove(0);
            } else if (appEvent.getEv().get(0).getRef().equals("")) {
                appEvent.getEv().get(0).setRef("activity.LauncherActivity");
            }
        }
//        Log.e(TAG, "上传事件集合长度==========" + appEvent.getEv().size());
        //将对象转为json
        String appEventJson = mGson.toJson(appEvent);
        //生成对象后将集合清空
        MyApplication.evBeanArrayList.clear();
        //生成参数上传
        HashMap<String, Object> param = new HashMap<>();
        param.put("data", appEventJson);
        Log.e("Util工具类", "点击流上传的JSON数据==============" + appEventJson);

        okHttpUtil.post(Constant.TBS_DATA_STREAM, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e("Util工具类", "点击流上传成功==========" + json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e("Util工具类", "点击流上传失败==========" + e.getMessage());
            }

            @Override
            public void onError(Response response, int code) {
                Log.e("Util工具类", "点击流上传出错==========" + code);
            }
        });
    }

    //第一次上传数据
    public static void HttpFristPostUserUseInfo() {
        ArrayList<_AppEvent.EvBean> evBeans = new ArrayList<>();
        _AppEvent.EvBean evBean = new _AppEvent.EvBean();
        evBean.setEt("2");
        evBean.setVt(Util.getUnixTime());
        evBeans.add(evBean);
        //根据事件的集合生成要上传的对象
        _AppEvent appEvent = new _AppEvent(evBeans);
        //将对象转为json
        String appEventJson = mGson.toJson(appEvent);
//        Log.e(TAG, "第一次上传生成的数据====================" + appEventJson);
        //生成参数上传
        HashMap<String, Object> param = new HashMap<>();
        param.put("data", appEventJson);
        Log.e("Util工具类", "App启动第一次点击流上传的JSON数据==============" + appEventJson);
        okHttpUtil.post(Constant.TBS_DATA_STREAM, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e("Util工具类", "App启动第一次点击流上传的JSON数据======上传成功========" + json);
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    // TODO: 2018/2/27 倒计时进行数据的上传
    public static void sendEventByTimeKill() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        mTime++;
//                        Log.e(TAG, "计时时间============" + mTime + "===当前全局事件集合长度===" + MyApplication.evBeanArrayList.size());
                        if (mTime >= 30) {
                            //时间到了30秒进行数据的上传
                            mTime = 0;
                            if (!MyApplication.evBeanArrayList.isEmpty()) {
                                //上传数据
                                HttpPostUserUseInfo();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
