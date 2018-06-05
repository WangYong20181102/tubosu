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


    // TODO: 2018/6/4 存储用户的相关信息↓↓↓↓
    //存储用户的id
    public static String getUserId(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mUserId", "");
    }

    //获取用户的id
    public static void setUserId(Context context, String mUserId) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mUserId", mUserId).commit();
    }

    //存储用户的company_id 装修公司的id
    public static String getCompany_id(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mCompany_id", "");
    }

    //获取用户的company_id
    public static void setCompany_id(Context context, String mCompany_id) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mCompany_id", mCompany_id).commit();
    }

    //存储用户的mCellphone 用户的手机号码
    public static String getCellphone(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mCellphone", "");
    }

    //获取用户的mCellphone
    public static void setCellphone(Context context, String mCellphone) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mCellphone", mCellphone).commit();
    }

    //存储用户的mCellphone 用户的手机号码
    public static String getCellphone_check(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mCellphone_check", "");
    }

    //获取用户的mCellphone
    public static void setCellphone_check(Context context, String mCellphone_check) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mCellphone_check", mCellphone_check).commit();
    }

    //存储用户的Nickname 用户的昵称
    public static String getNickname(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mNickname", "");
    }

    //获取用户的Nickname 用户的昵称
    public static void setNickname(Context context, String mNickname) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mNickname", mNickname).commit();
    }

    //存储用户的头像
    public static String getIcon(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mIcon", "");
    }

    //获取用户的头像
    public static void setIcon(Context context, String mIcon) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mIcon", mIcon).commit();
    }

    //存储用户的订单数量
    public static String getOrder_count(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mOrder_count", "");
    }

    //获取用户的订单数量
    public static void setOrder_count(Context context, String mOrder_count) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mOrder_count", mOrder_count).commit();
    }

    //存储用户的等级
    public static String getGrade(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mGrade", "");
    }

    //获取用户的等级
    public static void setGrade(Context context, String mGrade) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mGrade", mGrade).commit();
    }

    //存储用户所在的城市
    public static String getCity_name(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mCity_name", "");
    }

    //获取用户所在的城市
    public static void setCity_name(Context context, String mCity_name) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mCity_name", mCity_name).commit();
    }

    //存储用户所在的省市
    public static String getProvince_name(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mProvince_name", "");
    }

    //获取用户所在的省市
    public static void setProvince_name(Context context, String mProvince_name) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mProvince_name", mProvince_name).commit();
    }

    //存储用户新订单的数量
    public static String getNew_order_count(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mNew_order_count", "");
    }

    //获取用户新订单的数量
    public static void setNew_order_count(Context context, String mNew_order_count) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mNew_order_count", mNew_order_count).commit();
    }

    //存储用户未量房放入数量
    public static String getNot_lf_order_count(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mNot_lf_order_count", "");
    }

    //获取用户未量房的数量
    public static void setNot_lf_order_count(Context context, String mNot_lf_order_count) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mNot_lf_order_count", mNot_lf_order_count).commit();
    }

    //存储用户量房放入数量
    public static String getLf_order_count(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mLf_order_count", "");
    }

    //获取用户量房的数量
    public static void setLf_order_count(Context context, String mLf_order_count) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mLf_order_count", mLf_order_count).commit();
    }
    //存储用户量房放入数量
    public static String getIs_new_sms(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mIs_new_sms", "");
    }

    //获取用户量房的数量
    public static void setIs_new_sms(Context context, String mIs_new_sms) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mIs_new_sms", mIs_new_sms).commit();
    }
    //用户的md5密码
    public static String getUserMd5PassWord(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("user_md5_password", "");
    }

    public static void setUserMd5PassWord(Context context, String user_md5_password) {
        context.getSharedPreferences("userInfo", 0).edit().putString("user_md5_password", user_md5_password).commit();
    }
    // TODO: 2018/6/4 存储用户的相关信息↑↑↑↑
}
