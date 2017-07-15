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




    private static final String XGTKK_PRE = "xgtkk_pre";
    private static final String house_style_json = "house_style_json";
    private static final String factory_style_json = "factory_style_json";

    public static void setHouseStyleJson(Context context, String houseJson){
        context.getSharedPreferences(XGTKK_PRE, 0).edit().putString(house_style_json, houseJson).commit();
    }

    public static String getHouseStyleJson(Context context){
        return context.getSharedPreferences(XGTKK_PRE, 0).getString(house_style_json, "");
    }

    public static void setFactoryStyleJson(Context context, String factoryJson){
        context.getSharedPreferences(XGTKK_PRE, 0).edit().putString(factory_style_json, factoryJson).commit();
    }

    public static String getFactoryStyleJson(Context context){
        return context.getSharedPreferences(XGTKK_PRE, 0).getString(factory_style_json, "");
    }


    private static final String XGTKK_STYLE_PRE = "xgtkk_style_pre";

    private static final String house_style_space_num = "house_style_space_num";
    public static void setHouseSpaceNum(Context context, int spaceNum){
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_space_num, spaceNum).commit();
    }
    public static int getHouseSpaceNum(Context context){
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_space_num, -1);
    }

    private static final String house_style_style_num = "house_style_style_num";
    public static void setHouseStyleNum(Context context, int styleNum){
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_style_num, styleNum).commit();
    }
    public static int getHouseStyleNum(Context context){
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_style_num, -1);
    }


    private static final String house_style_part_num = "house_style_part_num";
    public static void setHousePartNum(Context context, int partNum){
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_part_num, partNum).commit();
    }
    public static int getHousePartNum(Context context){
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_part_num, -1);
    }


    private static final String house_style_huxing_num = "house_style_huxing_num";
    public static void setHouseHuxingNum(Context context, int huxingNum){
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_huxing_num, huxingNum).commit();
    }
    public static int getHouseHuxingNum(Context context){
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_huxing_num, -1);
    }


    private static final String house_style_color_num = "house_style_color_num";
    public static void setHouseColorNum(Context context, int colorNum){
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_color_num, colorNum).commit();
    }
    public static int getHouseColorNum(Context context){
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_color_num, -1);
    }


}
