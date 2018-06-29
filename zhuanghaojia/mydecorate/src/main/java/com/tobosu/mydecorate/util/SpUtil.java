package com.tobosu.mydecorate.util;

import android.content.Context;

import com.tobosu.mydecorate.global.Constant;

/**
 * Created by Mr.Lin on 2018/6/25 13:52.
 */
public class SpUtil {
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

    //todo 1.首页-免费设计      zhjaj01
    //获取
    public static String getzhjaj01(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zhjaj01", Constant.QUOTE);
    }

    //存储
    public static void setzhjaj01(Context context, String zhjaj01) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zhjaj01", zhjaj01).commit();
    }

    //todo 2.首页-免费报价       zhjaj02
    public static String getzhjaj02(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zhjaj02", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzhjaj02(Context context, String zhjaj02) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj02", zhjaj02).commit();
    }

    //todo 3.我要装修-免费报价      zhjaj03
    public static String getzhjaj03(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zhjaj03", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzhjaj03(Context context, String zhjaj03) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zhjaj03", zhjaj03).commit();
    }
}
