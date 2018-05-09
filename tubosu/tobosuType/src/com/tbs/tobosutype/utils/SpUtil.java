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

    //存储学装修分类信息
    public static void setArticleType(Context context, String ArticleType) {
        context.getSharedPreferences("mlArticleType", 0).edit().putString("mArticleType", ArticleType).commit();
    }

    //获取学装修分类信息
    public static String getArticleType(Context context) {
        return context.getSharedPreferences("mlArticleType", 0).getString("mArticleType", "");
    }

    //存储推送提醒的时间
    public static void setNoticeTime(Context context, String NoticeTime) {
        context.getSharedPreferences("mNoticeTime", 0).edit().putString("mNoticeTime", NoticeTime).commit();
    }

    //获取推送提醒的时间
    public static String getNoticeTime(Context context) {
        return context.getSharedPreferences("mNoticeTime", 0).getString("mNoticeTime", "");
    }

    //存储用户按的Tab
    public static void setMainTabPosition(Context context, int mMainTabPosition) {
        context.getSharedPreferences("mMainTabPosition", 0).edit().putInt("mMainTabPosition", mMainTabPosition).commit();
    }

    //获取用户按的Tab
    public static int getMainTabPosition(Context context) {
        return context.getSharedPreferences("mMainTabPosition", 0).getInt("mMainTabPosition", 0);
    }


    //存储网络端获取的手机正则校验
    public static void setCellphonePartern(Context context, String mCellphonePartern) {
        context.getSharedPreferences("mCellphonePartern", 0).edit().putString("mCellphonePartern", mCellphonePartern).commit();
    }

    //获取网络端获取的手机正则校验
    public static String getCellphonePartern(Context context) {
        return context.getSharedPreferences("mCellphonePartern", 0).getString("mCellphonePartern", "");
    }


    //存储用户在App升级是做的删除信息操作
    public static void setCleanUserInfoFlag(Context context, String flag) {
        context.getSharedPreferences("mCleanUserInfoFlag", 0).edit().putString("mCleanUserInfoFlag", flag).commit();
    }

    //获取用户在App升级是做的删除信息操作
    public static String getCleanUserInfoFlag(Context context) {
        return context.getSharedPreferences("mCleanUserInfoFlag", 0).getString("mCleanUserInfoFlag", "");
    }

    //首页每天的弹窗逻辑
    //设置每天的token标识
    public static void setTodayToken(Context context, String mTodayToken) {
        context.getSharedPreferences("mTodayToken", 0).edit().putString("mTodayToken", mTodayToken).commit();
    }

    //获取每天的token标识
    public static String getTodayToken(Context context) {
        return context.getSharedPreferences("mTodayToken", 0).getString("mTodayToken", "");
    }

    //弹窗数据
    public static void cleanDialogInfo(Context context) {
        context.getSharedPreferences("dialogInfo", 0).edit().clear().commit();
    }

    //是否弹更新弹窗
    public static String getIsShowUpdataDialog(Context context) {
        return context.getSharedPreferences("dialogInfo", 0).getString("updataDialog", "");
    }

    public static void setIsShowUpdataDialog(Context context, String updataDialog) {
        context.getSharedPreferences("dialogInfo", 0).edit().putString("updataDialog", updataDialog).commit();
    }

    //是否弹运营活动弹窗
    public static String getIsShowActivityDialog(Context context) {
        return context.getSharedPreferences("dialogInfo", 0).getString("activityDialog", "");
    }

    public static void setIsShowActivityDialog(Context context, String activityDialog) {
        context.getSharedPreferences("dialogInfo", 0).edit().putString("activityDialog", activityDialog).commit();
    }

    //是否弹推送弹窗
    public static String getIsShowPushDialog(Context context) {
        return context.getSharedPreferences("dialogInfo", 0).getString("pushDialog", "");
    }

    public static void setIsShowPushDialog(Context context, String pushDialog) {
        context.getSharedPreferences("dialogInfo", 0).edit().putString("pushDialog", pushDialog).commit();
    }

    //首页 和装修公司主页用的城市信息  首页或者是装修公司页面修改了城市的信息得以同步
    public static String getHomeAndCompanyUsingCity(Context context) {
        return context.getSharedPreferences("mHomeAndCompanyUsingCity", 0).getString("mHomeAndCompanyUsingCity", "");
    }

    public static void setHomeAndCompanyUsingCity(Context context, String mHomeAndCompanyUsingCity) {
        context.getSharedPreferences("mHomeAndCompanyUsingCity", 0).edit().putString("mHomeAndCompanyUsingCity", mHomeAndCompanyUsingCity).commit();
    }

    // TODO: 2018/3/1  统计相关的数据
    //存储统计的访问时间
    public static long getStatisticsEventVistTime(Context context) {
        return context.getSharedPreferences("StatisticsEvent", 0).getLong("m_vist_time", 0);
    }

    //获取统计的访问时间
    public static void setStatisticsEventVistTime(Context context, long vist_time) {
        context.getSharedPreferences("StatisticsEvent", 0).edit().putLong("m_vist_time", vist_time).commit();
    }

    //存储统计的离开时间
    public static long getStatisticsEventLeaveTime(Context context) {
        return context.getSharedPreferences("StatisticsEvent", 0).getLong("m_leave_time", 0);
    }

    //获取统计的离开时间
    public static void setStatisticsEventLeaveTime(Context context, long leave_time) {
        context.getSharedPreferences("StatisticsEvent", 0).edit().putLong("m_leave_time", leave_time).commit();
    }

    //存储统计的from_where
    public static String getStatisticsEventFromWhere(Context context) {
        return context.getSharedPreferences("StatisticsEvent", 0).getString("m_from_where", "");
    }

    //获取统计的from_where
    public static void setStatisticsEventFromWhere(Context context, String from_where) {
        context.getSharedPreferences("StatisticsEvent", 0).edit().putString("m_from_where", from_where).commit();
    }

    //存储统计的页面的pageId
    public static String getStatisticsEventPageId(Context context) {
        return context.getSharedPreferences("StatisticsEvent", 0).getString("m_page_id", "");
    }

    //获取统计的from_where
    public static void setStatisticsEventPageId(Context context, String page_id) {
        context.getSharedPreferences("StatisticsEvent", 0).edit().putString("m_page_id", page_id).commit();
    }

    //存储推送的唯一标识
    public static String getPushRegisterId(Context context) {
        return context.getSharedPreferences("PushRegisterId", 0).getString("mPushRegisterId", "");
    }

    //获取推送的唯一标识
    public static void setPushRegisterId(Context context, String mPushRegisterId) {
        context.getSharedPreferences("PushRegisterId", 0).edit().putString("mPushRegisterId", mPushRegisterId).commit();
    }


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


    //获取公众号
    public static String getPublic_number(Context context) {
        return context.getSharedPreferences("Public_number", 0).getString("public_number", "itobosu");
    }

    //存储公众号
    public static void setPublic_number(Context context, String public_number) {
        context.getSharedPreferences("Public_number", 0).edit().putString("public_number", public_number).commit();
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

    // TODO: 2018/5/7  App发单地址********根据获取配置表的信息去取最新的值********

}
