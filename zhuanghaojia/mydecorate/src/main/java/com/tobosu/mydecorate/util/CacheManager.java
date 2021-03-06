package com.tobosu.mydecorate.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * 缓存工具类
 * Created by dec on 2017/2/14.
 */

public class CacheManager {

    private static final String POPORDER_BECON_PREFERENCE = "poporder_location_becon";
    private static final String BECON_LOCATION = "becon_location";
    private static final String POPORDER_WAY = "poporder_way";

    private static final String POP = "pop";

    private static final String STYLE = "style";
    private static final String SQUARE = "square";
    private static final String BUDGET = "budget";


    private static final String STYLEPOSITION = "styleposition";
    private static final String SQUAREPOSITION = "squareposition";
    private static final String BUDGETPOSITION = "budgetposition";


    private static final String DECORATIONSTAGE = "decorationstage";
    private static final String DECORATIONSTAGEPOSITION = "decorationstageposition";

    private static final String DECORATIONSTYLE = "decorationstyle";
    private static final String DECORATIONSTYLEPOSITION = "decorationstyleposition";

    private static final String DONTNEEDORDER = "dontneedorder";

    private static final String CITY_PREFERENCE = "city_preference";
    private static final String CITY = "city";
    private static final String CITYJSON = "cityjson";

    private static final String FIRSTENTRYAPPPREFERENCE = "firstentryapppreference";
    private static final String FIRSTENTRY = "firstentry";

    private static final String SELECT_CITY_GO = "select_city_go";




    /*=============================================================================
    * 方法 setPopWay() 说明
    *   设值 before  -->> 准备装修
    *   设值 ongoing  -->> 正在装修过程中
    **/

    /***
     * 发单时 页面走向
     * @param context
     * @param way  prepare | ongoing
     */
    public static void setPopWay(Context context, String way) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(POPORDER_WAY, way).commit();
    }

    /***
     * 获取发单记录页面走向
     * @param context
     * @return
     */
    public static String getPopWay(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(POPORDER_WAY, "");
    }






    /*=============================================================================
    * 方法 setBecon() 说明
    * 选择before
    *   设值 1  -->> 开始
    *   设值 2  -->> 风格 页面标记
    *   设值 3  -->> 面积 预算 页面标记
    *   设值 4  -->> 领取设计 页面标记
    * 选择ongoing
    *   设值 1 -->> 阶段 页面标记
    *   设值 2 -->> 风格 页面标记
    *   设值 3 -->> 领取设计 页面标记
    **/

    /***
     * 发单时记录发单的位置
     * @param context
     * @param beconLocation
     */
    public static void setBecon(Context context, String beconLocation) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(BECON_LOCATION, beconLocation).commit();
    }

    /***
     * 获取发单记录位置
     * @param context
     * @return
     */
    public static String getBecon(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(BECON_LOCATION, "");
    }



    /*=============================================================================
    * 方法 setFistEntryApp() 说明
    *
    **/

    /***
     * 发单时记录发单的位置
     * @param context
     * @param first
     */
    public static void setFistPopOrder(Context context, String first) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(POP, first).commit();
    }

    /***
     * 获取发单记录位置
     * @param context
     * @return
     */
    public static String getFirstPopOrder(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(POP, "");
    }

    /***
     * 获取style
     * @param context
     * @return
     */
    public static String getStyle(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(STYLE, "");
    }


    /***
     * 记录style 位置
     * @param context
     * @param ps
     */
    public static void setStylePosition(Context context, String style, int ps) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(STYLEPOSITION, ps).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(STYLE, style).commit();
    }

    private static final String tulaoban = "tulaoban";

//    public static String getPrepareStyle(Context context){
//        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(tulaoban,"");
//    }

    /***
     * 获取style 位置
     * @param context
     * @return 等于 10 即 没选择
     */
    public static int getStylePosition(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(STYLEPOSITION, 10);
    }


    /***
     * 获取square
     * @param context
     * @return
     */
    public static String getSquare(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(SQUARE, "");
    }


    /***
     * 记录square 位置
     * @param context
     * @param ps
     */
    public static void setSquarePosition(Context context, String square, int ps) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(SQUAREPOSITION, ps).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(SQUARE, square).commit();
    }

    /***
     * 获取square 位置
     * @param context
     * @return
     */
    public static int getSquarePosition(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(SQUAREPOSITION, 10);
    }


    /***
     * 获取budget
     * @param context
     * @return
     */
    public static String getBudget(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(BUDGET, "");
    }


    /***
     * 记录budget 位置
     * @param context
     * @param ps
     */
    public static void setBudgetPosition(Context context, String budget, int ps) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(BUDGETPOSITION, ps).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(BUDGET, budget).commit();
    }

    /***
     * 获取budget 位置
     * @param context
     * @return
     */
    public static int getBudgetPosition(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(BUDGETPOSITION, 10);
    }


    /***
     * 记录阶段
     * @param context
     * @param stage
     * @param stagePosition
     */
    public static void setStage(Context context, String stage, int stagePosition) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(DECORATIONSTAGE, stage).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(DECORATIONSTAGEPOSITION, stagePosition).commit();
    }

    /***
     * 获取阶段 名称
     * @param context
     * @return
     */
    public static String getStage(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(DECORATIONSTAGE, "");
    }

    /***
     * 获取阶段号码
     * @param context
     * @return
     */
    public static int getStagePosition(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(DECORATIONSTAGEPOSITION, 20);
    }


    /***
     * 记录正在装修 风格
     * @param context
     * @param style
     * @param po
     */
    public static void setOngoingStyle(Context context, String style, int po) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(DECORATIONSTYLE, style).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(DECORATIONSTYLEPOSITION, po).commit();
    }

    /***
     * 获正在装修 风格
     * @param context
     * @return
     */
    public static String getOngoingStyle(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(DECORATIONSTYLE, "");
    }


    /***
     * 获正在装修 风格
     * @param context
     * @return
     */
    public static int getOngoingStylePosition(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(DECORATIONSTYLEPOSITION, 20);
    }


    /***
     * 记录暂时不需要
     * @param context
     * @param dontneed   dontneed:还未发单成功 ok:已经发单成功
     */
    public static void setDontNeed(Context context, String dontneed) {
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(DONTNEEDORDER, dontneed).commit();
    }

    /***
     * 获取阶段 名称
     * @param context
     * @return
     */
    public static String getDontNeed(Context context) {
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(DONTNEEDORDER, "");
    }

    /***
     * 记录城市
     * @param context
     * @param city
     */
    public static void setCity(Context context, String city) {
        if(!TextUtils.isEmpty(city)){
            if(city.contains("市") || city.contains("县")){
                city = city.substring(0, city.length()-1);
            }
        }
        context.getSharedPreferences(CITY_PREFERENCE, Context.MODE_PRIVATE).edit().putString(CITY, city).commit();
    }

    /***
     * 获取城市
     * @param context
     * @return
     */
    public static String getCity(Context context) {
        return context.getSharedPreferences(CITY_PREFERENCE, Context.MODE_PRIVATE).getString(CITY, "");
    }


    /***
     * 记录城市Json
     * @param context
     * @param cityJson
     */
    public static void setCityJson(Context context, String cityJson) {
        context.getSharedPreferences(CITY_PREFERENCE, Context.MODE_PRIVATE).edit().putString(CITYJSON, cityJson).commit();
    }

    /***
     * 获取城市Json
     * @param context
     * @return
     */
    public static String getCityJson(Context context) {
        return context.getSharedPreferences(CITY_PREFERENCE, Context.MODE_PRIVATE).getString(CITYJSON, "");
    }


    /***
     * 记录 第一次进入app
     * @param context
     * @param first
     */
    public static void setEnterApp(Context context, String first) {
        context.getSharedPreferences(FIRSTENTRYAPPPREFERENCE, Context.MODE_PRIVATE).edit().putString(FIRSTENTRY, first).commit();
    }

    /***
     * 获取是否第一次进入app
     * @param context
     * @return
     */
    public static String getEnterApp(Context context) {
        return context.getSharedPreferences(FIRSTENTRYAPPPREFERENCE, Context.MODE_PRIVATE).getString(FIRSTENTRY, "");
    }


    /***
     * 记录 选择city页面
     * @param context
     * @param go
     */
    public static void setSelectCityFlag(Context context, String go) {
        context.getSharedPreferences(FIRSTENTRYAPPPREFERENCE, Context.MODE_PRIVATE).edit().putString(SELECT_CITY_GO, go).commit();
    }

    /***
     * 获取 选择city页面
     * @param context
     * @return
     */
    public static String getSelectCityFlag(Context context) {
        return context.getSharedPreferences(FIRSTENTRYAPPPREFERENCE, Context.MODE_PRIVATE).getString(SELECT_CITY_GO, "");
    }

    //获取首页的缓存
    public static String getHomeFragCache(Context context) {
        return context.getSharedPreferences("HomeFragMessage", 0).getString("dataJson", "");
    }

    //获取当前登录用户的uid
    public static String getUserUid(Context context) {
        return context.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("user_id", "");
    }

    public static void setUserUid(Context context, String uid){
        context.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).edit().putString("user_id", uid).commit();
    }


    /**
     * ----------------------------------------------------------------
     * 装修宝典请求title的标记
     *
     */
    private static final String TITLESTRINGLIST_PRE = "titleStringList_pre";
    private static final String REQUEST_TIME = "request_time";

    /**
     *
     * @param context
     * @param time 次数
     */
    public static void setTitleListTime(Context context, int time){
        context.getSharedPreferences(TITLESTRINGLIST_PRE, Context.MODE_PRIVATE).edit().putInt(REQUEST_TIME, time).commit();
    }

    public static int getTitleListTime(Context context){
        return context.getSharedPreferences(TITLESTRINGLIST_PRE, Context.MODE_PRIVATE).getInt(REQUEST_TIME, 0);
    }
}
