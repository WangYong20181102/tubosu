package com.tbs.tobosupicture.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mr.Lin on 2017/6/28 18:29.
 * 小工具类 一些小的工具在这里实现
 */

public class Utils {

    //TODO App相关处理工具****************************

    /**
     * 获取当天的加密token
     * 加密规则：
     * 1.先将密码盐：zhj 进行md5加密；
     * 2.获取当天日期 如：2017-05-23；
     * 3.再对加密后的密码盐和当天日期进行md5加密；
     * 4.然后再对整体进行base64加密
     */
    public static String getDateToken() {
        String md5ZHJ = Md5Utils.md5("xgt");//加密后的xgt
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String mTime = format.format(date);
        String s = Md5Utils.md5(md5ZHJ + mTime);
        String dataToken = Base64Util.getBase64(s);
        return dataToken;
    }

    /**
     * 获取App版本号工具
     *
     * @param context
     * @return
     */
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
     * 获取手机的MAC地址
     *
     * @param context
     * @return
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

    /**
     * 获取App当前的所在的渠道信息
     *
     * @param context
     * @return
     */
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

    //TODO 网络连接相关的处理工具***********************************

    /**
     * 判断网络是否可用
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
     * 获取当前手机的WiFi是否可用
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
}
