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

    /**
     * 存储用户的信息  在退出登录时调用修改这些信息
     */
    //用户的昵称
    public static void saveUserNick(Context context, String nick) {
        context.getSharedPreferences("UserInfo", 0).edit().putString("nick", nick).commit();
    }

    public static String getUserNick(Context context) {
        return context.getSharedPreferences("UserInfo", 0).getString("nick", "");
    }

    //用户的头像
    public static void saveUserIcon(Context context, String icon) {
        context.getSharedPreferences("UserInfo", 0).edit().putString("icon", icon).commit();
    }

    public static String getUserIcon(Context context) {
        return context.getSharedPreferences("UserInfo", 0).getString("icon", "");
    }

    //用户的个性签名
    public static void saveUserPersonalSignature(Context context, String personal_signature) {
        context.getSharedPreferences("UserInfo", 0).edit().putString("personal_signature", personal_signature).commit();
    }

    public static String getUserPersonalSignature(Context context) {
        return context.getSharedPreferences("personal_signature", 0).getString("personal_signature", "");
    }

    //用户的uid
    public static void saveUserUid(Context context, String uid) {
        context.getSharedPreferences("UserInfo", 0).edit().putString("uid", uid).commit();
    }

    public static String getUserUid(Context context) {
        return context.getSharedPreferences("UserInfo", 0).getString("uid", "");
    }

    //用户的类型
    public static void saveUserType(Context context, String user_type) {
        context.getSharedPreferences("UserInfo", 0).edit().putString("user_type", user_type).commit();
    }

    public static String getUserType(Context context) {
        return context.getSharedPreferences("UserInfo", 0).getString("user_type", "");
    }

    private static final String XGTKK_PRE = "xgtkk_pre";
    private static final String house_style_json = "house_style_json";
    private static final String factory_style_json = "factory_style_json";

    public static void setHouseStyleJson(Context context, String houseJson) {
        context.getSharedPreferences(XGTKK_PRE, 0).edit().putString(house_style_json, houseJson).commit();
    }

    public static String getHouseStyleJson(Context context) {
        return context.getSharedPreferences(XGTKK_PRE, 0).getString(house_style_json, "");
    }

    public static void setFactoryStyleJson(Context context, String factoryJson) {
        context.getSharedPreferences(XGTKK_PRE, 0).edit().putString(factory_style_json, factoryJson).commit();
    }

    public static String getFactoryStyleJson(Context context) {
        return context.getSharedPreferences(XGTKK_PRE, 0).getString(factory_style_json, "");
    }


    private static final String XGTKK_STYLE_PRE = "xgtkk_style_pre";

    private static final String house_style_space_num = "house_style_space_num";

    public static void setHouseSpaceNum(Context context, int spaceNum) {
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_space_num, spaceNum).commit();
    }

    public static int getHouseSpaceNum(Context context) {
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_space_num, -1);
    }

    private static final String house_style_style_num = "house_style_style_num";

    public static void setHouseStyleNum(Context context, int styleNum) {
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_style_num, styleNum).commit();
    }

    public static int getHouseStyleNum(Context context) {
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_style_num, -1);
    }


    private static final String house_style_part_num = "house_style_part_num";

    public static void setHousePartNum(Context context, int partNum) {
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_part_num, partNum).commit();
    }

    public static int getHousePartNum(Context context) {
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_part_num, -1);
    }


    private static final String house_style_huxing_num = "house_style_huxing_num";

    public static void setHouseHuxingNum(Context context, int huxingNum) {
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_huxing_num, huxingNum).commit();
    }

    public static int getHouseHuxingNum(Context context) {
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_huxing_num, -1);
    }


    private static final String house_style_color_num = "house_style_color_num";

    public static void setHouseColorNum(Context context, int colorNum) {
        context.getSharedPreferences(XGTKK_STYLE_PRE, 0).edit().putInt(house_style_color_num, colorNum).commit();
    }

    public static int getHouseColorNum(Context context) {
        return context.getSharedPreferences(XGTKK_STYLE_PRE, 0).getInt(house_style_color_num, -1);
    }


    private static final String XGTKK_SEARCH_CASE_STYLE_PRE = "xgtkk_search_case_style_pre";
    private static final String area = "search_history_case_area";
    private static final String layout = "search_history_case_layout";
    private static final String price = "search_history_case_price";
    private static final String style = "search_history_case_style";

    public static void setSearchCaseColorAreaNum(Context context, int areaNum) {
        context.getSharedPreferences(XGTKK_SEARCH_CASE_STYLE_PRE, 0).edit().putInt(area, areaNum).commit();
    }

    public static int getSearchCaseColorAreaNum(Context context) {
        return context.getSharedPreferences(XGTKK_SEARCH_CASE_STYLE_PRE, 0).getInt(area, -1);
    }


    public static void setSearchCaseColorLayoutNum(Context context, int layoutNum) {
        context.getSharedPreferences(XGTKK_SEARCH_CASE_STYLE_PRE, 0).edit().putInt(layout, layoutNum).commit();
    }

    public static int getSearchCaseColorLayoutNum(Context context) {
        return context.getSharedPreferences(XGTKK_SEARCH_CASE_STYLE_PRE, 0).getInt(layout, -1);
    }

    public static void setSearchCaseColorPriceNum(Context context, int priceNum) {
        context.getSharedPreferences(XGTKK_SEARCH_CASE_STYLE_PRE, 0).edit().putInt(price, priceNum).commit();
    }

    public static int getSearchCaseColorPriceNum(Context context) {
        return context.getSharedPreferences(XGTKK_SEARCH_CASE_STYLE_PRE, 0).getInt(price, -1);
    }

    public static void setSearchCaseColorStyleNum(Context context, int styleNum) {
        context.getSharedPreferences(XGTKK_SEARCH_CASE_STYLE_PRE, 0).edit().putInt(style, styleNum).commit();
    }

    public static int getSearchCaseColorStyleNum(Context context) {
        return context.getSharedPreferences(XGTKK_SEARCH_CASE_STYLE_PRE, 0).getInt(style, -1);
    }


    public static void setBaiduLocationCity(Context context, String locationCity){
        context.getSharedPreferences("baidu_baidu_city", 0).edit().putString("location_city_baidu", locationCity).commit();
    }

    public static String getBaiduLocationCity(Context context){
        return context.getSharedPreferences("baidu_baidu_city", 0).getString("location_city_baidu", "");
    }

    public static void setCacheLocalCityJson(Context context, String json){
        context.getSharedPreferences("CacheLocalCityJson", 0).edit().putString("LocalCityJson", json).commit();
    }

    public static String getCacheLocalCityJson(Context context){
        return context.getSharedPreferences("CacheLocalCityJson", 0).getString("LocalCityJson", "");
    }


    // 以下是为了给家装和工装请求网络时用到的城市
    public static String getTemplateFragmentCity(Context context) {
        return context.getSharedPreferences("TemplateFragmentCity", 0).getString("template_fragment_city", "");
    }
    public static void setTemplateFragmentCity(Context context, String fragment_city) {
        context.getSharedPreferences("TemplateFragmentCity", 0).edit().putString("template_fragment_city", fragment_city).commit();
    }

}
