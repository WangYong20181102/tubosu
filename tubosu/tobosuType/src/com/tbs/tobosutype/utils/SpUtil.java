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
}
