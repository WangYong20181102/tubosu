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

    //用户是否第一次进入图片查看
    public static void setFristIntoImageLook(Context context, String mFristIntoImageLook) {
        context.getSharedPreferences("FristIntoImageLook", 0).edit().putString("mFristIntoImageLook", mFristIntoImageLook).commit();
    }

    //获取用户的是否第一次进入图片查看器
    public static String getFristIntoImageLook(Context context) {
        return context.getSharedPreferences("FristIntoImageLook", 0).getString("mFristIntoImageLook", "");
    }
}
