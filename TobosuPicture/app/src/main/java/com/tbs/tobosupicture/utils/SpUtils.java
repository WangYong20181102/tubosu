package com.tbs.tobosupicture.utils;

import android.content.Context;

/**
 * Created by Mr.Lin on 2017/5/11 09:15.
 * 缓存用的工具
 */

public class SpUtils {
    //存储用户是否第一次登录
    public static void setUserIsFristLogin(Context context, String alreadyLogin) {
        context.getSharedPreferences("UserIsLogin", 0).edit().putString("alreadyLogin", alreadyLogin).commit();
    }

    //获取用户是否第一次登录
    public static String getUserIsFristLogin(Context context) {
        return context.getSharedPreferences("UserIsLogin", 0).getString("alreadyLogin", "");
    }

    //存储定位信息
    public static void setLocationCity(Context context, String mCity) {
        context.getSharedPreferences("LocationInfo", 0).edit().putString("locationCity", mCity).commit();
    }

    //获取定位的城市
    public static String getLocationCity(Context context) {
        return context.getSharedPreferences("LocationInfo", 0).getString("locationCity", "");
    }

    //存储定位的纬度信息
    public static void setLocationLat(Context context, String lat) {
        context.getSharedPreferences("LocationInfo", 0).edit().putString("Lat", lat).commit();
    }

    //获取纬度信息
    public static String getLocationLat(Context context) {
        return context.getSharedPreferences("LocationInfo", 0).getString("Lat", "");
    }

    //存储经度信息
    public static void setLocationLon(Context context, String lon) {
        context.getSharedPreferences("LocationInfo", 0).edit().putString("Lon", lon).commit();
    }

    //获取经度信息
    public static String getLocationLon(Context context) {
        return context.getSharedPreferences("LocationInfo", 0).getString("Lon", "");
    }
}
