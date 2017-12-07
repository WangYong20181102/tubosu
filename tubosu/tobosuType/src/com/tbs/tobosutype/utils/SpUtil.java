package com.tbs.tobosutype.utils;

import android.content.Context;

/**
 * Created by Mr.Lin on 2017/11/17 11:17.
 * 存储的工具类
 */

public class SpUtil {
    //单图列表进入详情时的大数据过度 存储
    public static void setSingImageListJson(Context context, String mSingImageListJson) {
        context.getSharedPreferences("SingImageListJson", 0).edit().putString("mSingImageListJson", mSingImageListJson).commit();
    }

    //单图列表进入详情时的大数据过度 获取
    public static String getSingImageListJson(Context context) {
        return context.getSharedPreferences("SingImageListJson", 0).getString("mSingImageListJson", "");
    }

    //套图列表进入详情时的大数据过度 存储
    public static void setDoubleImageListJson(Context context, String mDoubleImageListJson) {
        context.getSharedPreferences("DoubleImageListJson", 0).edit().putString("mDoubleImageListJson", mDoubleImageListJson).commit();
    }

    //套图列表进入详情时的大数据过度 获取
    public static String getDoubleImageListJson(Context context) {
        return context.getSharedPreferences("DoubleImageListJson", 0).getString("mDoubleImageListJson", "");
    }

    //用户是否第一次进入图片查看
    public static void setFristIntoImageLook(Context context, String mFristIntoImageLook) {
        context.getSharedPreferences("FristIntoImageLook", 0).edit().putString("mFristIntoImageLook", mFristIntoImageLook).commit();
    }

    //获取用户的是否第一次进入图片查看器
    public static String getFristIntoImageLook(Context context) {
        return context.getSharedPreferences("FristIntoImageLook", 0).getString("mFristIntoImageLook", "");
    }


    //用户在查看图册时每天一次的发单
    public static void setImageDetailDataToken(Context context, String mImageDetailDataToken) {
        context.getSharedPreferences("ImageDetailDataToken", 0).edit().putString("mImageDetailDataToken", mImageDetailDataToken).commit();
    }

    //用户在查看图册时每天一次的发单
    public static String getImageDetailDataToken(Context context) {
        return context.getSharedPreferences("ImageDetailDataToken", 0).getString("mImageDetailDataToken", "");
    }

    //存储纬度信息
    public static void setLatitude(Context context, String mLatitude) {
        context.getSharedPreferences("mlLatitude", 0).edit().putString("mLatitude", mLatitude).commit();
    }
    //获取纬度信息
    public static String getLatitude(Context context) {
        return context.getSharedPreferences("mlLatitude", 0).getString("mLatitude", "");
    }

    //存储经度信息
    public static void setLongitude(Context context, String mLongitude) {
        context.getSharedPreferences("mlLongitude", 0).edit().putString("mLongitude", mLongitude).commit();
    }
    //获取经度信息
    public static String getLongitude(Context context) {
        return context.getSharedPreferences("mlLongitude", 0).getString("mLongitude", "");
    }

    //存储城市信息
    public static void setCity(Context context, String City) {
        context.getSharedPreferences("mlCity", 0).edit().putString("mCity", City).commit();
    }
    //获取城市信息
    public static String getCity(Context context) {
        return context.getSharedPreferences("mlCity", 0).getString("mCity", "");
    }

    //存储城市信息
    public static void setRadius(Context context, String Radius) {
        context.getSharedPreferences("mlRadius", 0).edit().putString("mRadius", Radius).commit();
    }
    //获取城市信息
    public static String getRadius(Context context) {
        return context.getSharedPreferences("mlRadius", 0).getString("mRadius", "");
    }
}
