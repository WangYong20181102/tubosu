package com.tbs.tbsbusiness.util;

import android.content.Context;

/**
 * Created by Mr.Lin on 2018/6/2 10:29.
 */
public class SpUtil {
    //存储推送的唯一标识
    public static String getPushRegisterId(Context context) {
        return context.getSharedPreferences("PushRegisterId", 0).getString("mPushRegisterId", "");
    }

    //获取推送的唯一标识
    public static void setPushRegisterId(Context context, String mPushRegisterId) {
        context.getSharedPreferences("PushRegisterId", 0).edit().putString("mPushRegisterId", mPushRegisterId).commit();
    }
}
