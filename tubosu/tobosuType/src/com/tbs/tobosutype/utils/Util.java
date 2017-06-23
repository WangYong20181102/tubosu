package com.tbs.tobosutype.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dec on 2016/9/12.
 * 工具类
 */
public class Util {

    public static void setToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
        String token = sp.getString("token","");
        if ("".equals(token)) {
            return false;//未登录
        }else {
            return true;//已登陆
        }
    }

    public static String getUserId(Context context){
        return context.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("user_id","");
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
        Toast.makeText(context, "网络断开了~", Toast.LENGTH_SHORT).show();
        return false;
    }



    /**
     * 检测网络状态<br/>
     * @return true 连接网络
     *         false 没有网络
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
     * @param mContext
     */
    public static void toastNetOut(Context mContext) {
        Toast.makeText(mContext, "网络断开，请检查网络~", Toast.LENGTH_SHORT).show();
    }


    /**
     * 判断是否有网络连接
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
     *
     * @param context
     * @return -1：没有网络  1：WIFI网络2：wap网络3：net网络
     */
    public static int GetNetype(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType== ConnectivityManager.TYPE_MOBILE) {
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            }else{
                netType = 2;
            }
        }else if(nType== ConnectivityManager.TYPE_WIFI){
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
        phoneNum = phoneNum.replaceAll("-","");

        if("".equals(phoneNum)){
            Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
            Pattern pattern = Pattern.compile(MOBILE);
            Matcher matcher = pattern.matcher(phoneNum);
            boolean flag = matcher.matches();
            if(!flag){
                Toast.makeText(mContext, "请输入合法电话号码", Toast.LENGTH_SHORT).show();
            }
            return matcher.matches();
        }
    }


    /**
     *
     * @param loadingView 加载视图
     * @param isLoading 正在加载中
     * @param isNet 是否有网络
     */
    public static void showDataAmin(View loadingView, boolean isLoading, boolean isNet){
        if(isLoading&&isNet){
            loadingView.setVisibility(View.VISIBLE);
        }else if(isLoading==false&&isNet) {
            loadingView.setVisibility(View.GONE);
        }else{

        }
    }


    public static void setLog(String tag, String log){
        System.out.println("---当前页面是" + tag + "-->>>" + "--打印信息>>>" + log + "<<");
    }

    public static void setErrorLog(String tag, String log){
        Log.e("---当前页面是" + tag + "-->>>", "--打印信息>>>" + log + "<<");
    }


    public static void setActivityStatusColor(Activity context){
        context.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
//
//    /**
//     *
//     * @param context
//     * @param view
//     * @param isNet true 有网络  false 无网络
//     */
//    public static void showNetOutView(final Context context, View view, boolean isNet){
//        if(isNet){
//            view.setVisibility(View.GONE);
//        }else {
//            view.setVisibility(View.VISIBLE);
//            view.findViewById(R.id.tv_go_getnet).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if(android.os.Build.VERSION.SDK_INT > 10 ){
//                        //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
//                        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
//                    } else {
//                        context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
//                    }
//                }
//            });
//        }
//    }
}
