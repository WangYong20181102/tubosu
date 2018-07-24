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

    //存储用户的mCellphone 用户的手机号码是否验证
    public static String getCellphone_check(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mCellphone_check", "");
    }

    //获取用户的mCellphone
    public static void setCellphone_check(Context context, String mCellphone_check) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mCellphone_check", mCellphone_check).commit();
    }


    //存储用户的微信是否验证
    public static String getWechat_check(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mWechat_check", "");
    }

    //获取用户的微信是否验证
    public static void setWechat_check(Context context, String mWechat_check) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mWechat_check", mWechat_check).commit();
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
    //存储用户的性别
    public static String getGender(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mGender", "");
    }

    //获取用户的性别
    public static void setGender(Context context, String mGender) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mGender", mGender).commit();
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

    //存储用户所在的小区
    public static String getCommunity(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mCommunity", "");
    }

    //获取用户所在的小区
    public static void setCommunity(Context context, String mCommunity) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mCommunity", mCommunity).commit();
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
    //用户的登录方式
    public static String getUserLoginType(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("userLoginType", "");
    }

    public static void setUserLoginType(Context context, String userLoginType) {
        context.getSharedPreferences("userInfo", 0).edit().putString("userLoginType", userLoginType).commit();
    }
    //记录用户登录的account信息
    public static String getUserLoginAccount(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("userLoginAccount", "");
    }

    public static void setUserLoginAccount(Context context, String userLoginAccount) {
        context.getSharedPreferences("userInfo", 0).edit().putString("userLoginAccount", userLoginAccount).commit();
    }
    // TODO: 2018/6/4 存储用户的相关信息↑↑↑↑

    //获取官网电话
    public static String getCustom_service_tel(Context context) {
        return context.getSharedPreferences("CustomServiceTel", 0).getString("custom_service_tel", "400-696-2221");
    }

    //存储官网电话
    public static void setCustom_service_tel(Context context, String custom_service_tel) {
        context.getSharedPreferences("CustomServiceTel", 0).edit().putString("custom_service_tel", custom_service_tel).commit();
    }


    //获取官网QQ
    public static String getCustom_service_qq(Context context) {
        return context.getSharedPreferences("CustomServiceQQ", 0).getString("custom_service_qq", "4006062221");
    }

    //存储官网QQ
    public static void setCustom_service_qq(Context context, String custom_service_qq) {
        context.getSharedPreferences("CustomServiceQQ", 0).edit().putString("custom_service_qq", custom_service_qq).commit();
    }

    //获取小程序
    public static String getApplets_name(Context context) {
        return context.getSharedPreferences("AppletsName", 0).getString("applets_name", "土拨鼠查订单");
    }

    //存储小程序
    public static void setApplets_name(Context context, String applets_name) {
        context.getSharedPreferences("AppletsName", 0).edit().putString("applets_name", applets_name).commit();
    }
    //获取小程序id
    public static String getApplets_id(Context context) {
        return context.getSharedPreferences("AppletsId", 0).getString("applets_id", "gh_1a5495aba500");
    }

    //存储小程序id
    public static void setApplets_id(Context context, String applets_id) {
        context.getSharedPreferences("AppletsId", 0).edit().putString("applets_id", applets_id).commit();
    }

    //获取公众号
    public static String getPublic_number(Context context) {
        return context.getSharedPreferences("Public_number", 0).getString("public_number", "itobosu");
    }

    //存储公众号
    public static void setPublic_number(Context context, String public_number) {
        context.getSharedPreferences("Public_number", 0).edit().putString("public_number", public_number).commit();
    }

    //是否弹更新弹窗
    public static String getIsShowUpdataDialog(Context context) {
        return context.getSharedPreferences("dialogInfo", 0).getString("updataDialog", "");
    }

    public static void setIsShowUpdataDialog(Context context, String updataDialog) {
        context.getSharedPreferences("dialogInfo", 0).edit().putString("updataDialog", updataDialog).commit();
    }
    //存储网络端获取的手机正则校验
    public static void setCellphonePartern(Context context, String mCellphonePartern) {
        context.getSharedPreferences("mCellphonePartern", 0).edit().putString("mCellphonePartern", mCellphonePartern).commit();
    }

    //获取网络端获取的手机正则校验
    public static String getCellphonePartern(Context context) {
        return context.getSharedPreferences("mCellphonePartern", 0).getString("mCellphonePartern", "");
    }

    //获取城市JSON
    public static String getLocalCityJson(Context context) {
        return context.getSharedPreferences("LocalCityJson", 0).getString("mLocalCityJson", "");
    }

    //存储城市JSON
    public static void setLocalCityJson(Context context, String localCityJson) {
        context.getSharedPreferences("LocalCityJson", 0).edit().putString("mLocalCityJson", localCityJson).commit();
    }

    //获取省份option
    public static int getShengOption(Context context) {
        return context.getSharedPreferences("ShengOption", 0).getInt("mShengOption", 0);
    }

    //存储省份option
    public static void setShengOption(Context context, int mShengOption) {
        context.getSharedPreferences("ShengOption", 0).edit().putInt("mShengOption", mShengOption).commit();
    }

    //获取城市option
    public static int getShiOption(Context context) {
        return context.getSharedPreferences("ShiOption", 0).getInt("mShiOption", 0);
    }

    //存储城市option
    public static void setShiOption(Context context, int mShiOption) {
        context.getSharedPreferences("ShiOption", 0).edit().putInt("mShiOption", mShiOption).commit();
    }

    //获取区域option
    public static int getQuOption(Context context) {
        return context.getSharedPreferences("QuOption", 0).getInt("mQuOption", 0);
    }

    //存储城市option
    public static void setQuOption(Context context, int mQuOption) {
        context.getSharedPreferences("QuOption", 0).edit().putInt("mQuOption", mQuOption).commit();
    }
    //存储用户按的Tab
    public static void setMainTabPosition(Context context, int mMainTabPosition) {
        context.getSharedPreferences("mMainTabPosition", 0).edit().putInt("mMainTabPosition", mMainTabPosition).commit();
    }

    //获取用户按的Tab
    public static int getMainTabPosition(Context context) {
        return context.getSharedPreferences("mMainTabPosition", 0).getInt("mMainTabPosition", 0);
    }
    //存储用户在App升级是做的删除信息操作
    public static void setCleanUserInfoFlag(Context context, String flag) {
        context.getSharedPreferences("mCleanUserInfoFlag", 0).edit().putString("mCleanUserInfoFlag", flag).commit();
    }

    //获取用户在App升级是做的删除信息操作
    public static String getCleanUserInfoFlag(Context context) {
        return context.getSharedPreferences("mCleanUserInfoFlag", 0).getString("mCleanUserInfoFlag", "");
    }
}
