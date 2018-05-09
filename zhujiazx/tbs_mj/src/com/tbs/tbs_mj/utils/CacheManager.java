package com.tbs.tbs_mj.utils;

import android.content.Context;

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

    private static final String CITY_PREFERENCE = "city";
    private static final String CITY = "cityName";
    private static final String CITYJSON = "cityjson";

    private static final String FIRSTENTRYAPPPREFERENCE = "firstentryapppreference";
    private static final String FIRSTENTRY = "firstentry";

    private static final String SELECT_CITY_GO = "select_city_go";

    /***
     * 发单时 页面走向
     * @param context
     * @param way  prepare | ongoing
     */
    public static void setPopWay(Context context, String way){
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(POPORDER_WAY, way).commit();
    }

    /***
     * 获取发单记录页面走向
     * @param context
     * @return
     */
    public static String getPopWay(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(POPORDER_WAY,"");
    }

    /***
     * 发单时记录发单的位置
     * @param context
     * @param beconLocation
     */
    public static void setBecon(Context context, String beconLocation){
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(BECON_LOCATION, beconLocation).commit();
    }

    /***
     * 获取发单记录位置
     * @param context
     * @return
     */
    public static String getBecon(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(BECON_LOCATION,"");
    }



    /***
     * 发单时记录发单的位置
     * @param context
     * @param first
     */
    public static void setFistPopOrder(Context context, String first){
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(POP, first).commit();
    }

    /***
     * 获取发单记录位置
     * @param context
     * @return
     */
    public static String getFirstPopOrder(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(POP,"");
    }

    /***
     * 获取style
     * @param context
     * @return
     */
    public static String getStyle(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(STYLE,"");
    }


    /***
     * 记录style 位置
     * @param context
     * @param ps
     */
    public static void setStylePosition(Context context, String style, int ps){
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(STYLEPOSITION, ps).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(STYLE, style).commit();
    }

    /***
     * 获取style 位置
     * @param context
     * @return  等于 10 即 没选择
     */
    public static int getStylePosition(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(STYLEPOSITION,10);
    }


    /***
     * 获取square
     * @param context
     * @return
     */
    public static String getSquare(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(SQUARE,"");
    }


    /***
     * 记录square 位置
     * @param context
     * @param ps
     */
    public static void setSquarePosition(Context context, String square, int ps){
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(SQUAREPOSITION, ps).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(SQUARE, square).commit();
    }

    /***
     * 获取square 位置
     * @param context
     * @return
     */
    public static int getSquarePosition(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(SQUAREPOSITION,10);
    }


    /***
     * 获取budget
     * @param context
     * @return
     */
    public static String getBudget(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(BUDGET,"");
    }


    /***
     * 记录budget 位置
     * @param context
     * @param ps
     */
    public static void setBudgetPosition(Context context, String budget, int ps){
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(BUDGETPOSITION, ps).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(BUDGET, budget).commit();
    }

    /***
     * 获取budget 位置
     * @param context
     * @return
     */
    public static int getBudgetPosition(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(BUDGETPOSITION,10);
    }


    /***
     * 记录阶段
     * @param context
     * @param stage
     * @param stagePosition
     */
    public static void setStage(Context context, String stage, int stagePosition){
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(DECORATIONSTAGE, stage).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(DECORATIONSTAGEPOSITION, stagePosition).commit();
    }

    /***
     * 获取阶段 名称
     * @param context
     * @return
     */
    public static String getStage(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(DECORATIONSTAGE,"");
    }

    /***
     * 获取阶段号码
     * @param context
     * @return
     */
    public static int getStagePosition(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(DECORATIONSTAGEPOSITION,20);
    }


    /***
     * 记录正在装修 风格
     * @param context
     * @param style
     * @param po
     */
    public static void setOngoingStyle(Context context, String style, int po){
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putString(DECORATIONSTYLE, style).commit();
        context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).edit().putInt(DECORATIONSTYLEPOSITION, po).commit();
    }

    /***
     * 获正在装修 风格
     * @param context
     * @return
     */
    public static String getOngoingStyle(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getString(DECORATIONSTYLE,"");
    }


    /***
     * 获正在装修 风格
     * @param context
     * @return
     */
    public static int getOngoingStylePosition(Context context){
        return context.getSharedPreferences(POPORDER_BECON_PREFERENCE, Context.MODE_PRIVATE).getInt(DECORATIONSTYLEPOSITION,20);
    }


    /***
     * 记录城市
     * @param context
     * @param city
     */
    public static void setCity(Context context, String city){
        context.getSharedPreferences(CITY_PREFERENCE, Context.MODE_PRIVATE).edit().putString(CITY, city).commit();
    }


    /***
     * 获取城市
     * @param context
     * @return
     */
    public static String getCity(Context context){
        return context.getSharedPreferences(CITY_PREFERENCE, Context.MODE_PRIVATE).getString(CITY,"");
    }


    /***
     * 记录城市Json
     * @param context
     * @param cityJson
     */
    public static void setCityJson(Context context, String cityJson){
        context.getSharedPreferences(CITY_PREFERENCE, Context.MODE_PRIVATE).edit().putString(CITYJSON, cityJson).commit();
    }

    /***
     * 获取城市Json
     * @param context
     * @return
     */
    public static String getCityJson(Context context){
        return context.getSharedPreferences(CITY_PREFERENCE, Context.MODE_PRIVATE).getString(CITYJSON,"");
    }


    /***
     * 记录 第一次进入app
     * @param context
     * @param first
     */
    public static void setEnterApp(Context context, String first){
        context.getSharedPreferences(FIRSTENTRYAPPPREFERENCE, Context.MODE_PRIVATE).edit().putString(FIRSTENTRY, first).commit();
    }

    /***
     * 获取是否第一次进入app
     * @param context
     * @return
     */
    public static String getEnterApp(Context context){
        return context.getSharedPreferences(FIRSTENTRYAPPPREFERENCE, Context.MODE_PRIVATE).getString(FIRSTENTRY,"");
    }


    /***
     * 记录 选择city页面
     * @param context
     * @param go
     */
    public static void setSelectCityFlag(Context context, String go){
        context.getSharedPreferences(FIRSTENTRYAPPPREFERENCE, Context.MODE_PRIVATE).edit().putString(SELECT_CITY_GO, go).commit();
    }

    /***
     * 获取 选择city页面
     * @param context
     * @return
     */
    public static String getSelectCityFlag(Context context){
        return context.getSharedPreferences(FIRSTENTRYAPPPREFERENCE, Context.MODE_PRIVATE).getString(SELECT_CITY_GO,"");
    }



    private static final String POPPAGEPREFERENCE = "poppagepreference";
    private static final String POPPAGE = "poppage";

    public static void setPageFlag(Context context, String flag){
        context.getSharedPreferences(POPPAGEPREFERENCE, Context.MODE_PRIVATE).edit().putString(POPPAGE, flag).commit();
    }

    public static String getPageFlag(Context context){
        return context.getSharedPreferences(POPPAGEPREFERENCE, Context.MODE_PRIVATE).getString(POPPAGE,"");
    }



    private static final String POPFLAG = "popflag";
    private static final String POPFLAGSTRING = "popflagstring";

    /**
     *
     * @param context
     * @param flag  若存入是16则表示执行过此方法
     */
    public static void setPopFlag(Context context, int flag){
        context.getSharedPreferences(POPFLAG, Context.MODE_PRIVATE).edit().putInt(POPPAGE, flag).commit();
    }

    public static int getPopFlag(Context context){
        return context.getSharedPreferences(POPFLAG, Context.MODE_PRIVATE).getInt(POPPAGE,19); // 7代表没有存入过。
    }



    private static final String OUTCOME_PRE = "outcome_pre";
    private static final String DECORATE_BUDGET_TEXT = "decorate_budget_text";
    private static final String DECORATE_BUDGET = "decorate_budget_accosdfsda";


    public static void setStringArrayList(Context context, String[] textArray){
//        {b.getString("outcome_name"),b.getString("outcome_money"),b.getString("outcome_time"),b.getString("outcome_contents")}
        String textList = "";
        for(int i=0,len=textArray.length;i<len;i++){
            textList = textList + "#" + textArray[i];
        }
        context.getSharedPreferences(OUTCOME_PRE, Context.MODE_PRIVATE).edit().putString(DECORATE_BUDGET_TEXT, textList).commit();
    }

    public static void clearStringArrayList(Context context){
        context.getSharedPreferences(OUTCOME_PRE, Context.MODE_PRIVATE).edit().putString(DECORATE_BUDGET_TEXT, "").commit();
    }

    public static String getStringArrayList(Context context){
        return context.getSharedPreferences(OUTCOME_PRE, Context.MODE_PRIVATE).getString(DECORATE_BUDGET_TEXT, "");
    }


    public static void setDecorateBudget(Context context, String budget){
        context.getSharedPreferences(OUTCOME_PRE, Context.MODE_PRIVATE).edit().putString(DECORATE_BUDGET, budget).commit();
    }


    public static int getDecorateBudget(Context context){
        String budget = context.getSharedPreferences(OUTCOME_PRE, Context.MODE_PRIVATE).getString(DECORATE_BUDGET, "");
        if("".equals(budget) || "0".equals(budget)){
            return 0;
        }else{
            return 1;
        }
    }



    private static final String APP_ENTRY_ORDER_PRE = "app_entry_order_pre";
    private static final String APP_ENTRY_ORDER = "app_entry_order";
    public static String getAppEntryOrderPre(Context context){
        return context.getSharedPreferences(APP_ENTRY_ORDER_PRE, Context.MODE_PRIVATE).getString(APP_ENTRY_ORDER, "");
    }
    public static void setAppEntryOrderPre(Context context, String order){
        context.getSharedPreferences(APP_ENTRY_ORDER_PRE, Context.MODE_PRIVATE).edit().putString(APP_ENTRY_ORDER, order).commit();
    }

    private static final String APP_LOADING_AD_PRE = "app_loading_ad_pre";
    private static final String APP_LOADING_AD = "app_loading_ad";
    public static String getLoadingAdId(Context context){
        return context.getSharedPreferences(APP_LOADING_AD_PRE, Context.MODE_PRIVATE).getString(APP_LOADING_AD, "");
    }
    public static void setLoadingAdId(Context context, String id){
        context.getSharedPreferences(APP_LOADING_AD_PRE, Context.MODE_PRIVATE).edit().putString(APP_LOADING_AD, id).commit();
    }

    private static final String APP_LOADING_ADPIC_PATH = "app_loading_adpic_path";
    private static final String APP_LOADING_ADPIC = "app_loading_adpic";
    public static String getLoadingAdPath(Context context){
        return context.getSharedPreferences(APP_LOADING_ADPIC_PATH, Context.MODE_PRIVATE).getString(APP_LOADING_ADPIC, "");
    }
    public static void setLoadingAdPath(Context context, String path){
        context.getSharedPreferences(APP_LOADING_ADPIC_PATH, Context.MODE_PRIVATE).edit().putString(APP_LOADING_ADPIC, path).commit();
    }



    private static final String APP_LOADING_HUODONG_PRE = "app_loading_huodong_pre";
    private static final String APP_LOADING_HUODONG = "app_loading_huodong";
    public static String getLoadingHUODONG(Context context){
        return context.getSharedPreferences(APP_LOADING_HUODONG_PRE, Context.MODE_PRIVATE).getString(APP_LOADING_HUODONG, "");
    }
    public static void setLoadingHUODONG(Context context, String date){
        context.getSharedPreferences(APP_LOADING_HUODONG_PRE, Context.MODE_PRIVATE).edit().putString(APP_LOADING_HUODONG, date).commit();
    }



    private static final String APP_NEWHOME_JSON_PRE = "app_newhome_json_pre";
    private static final String APP_NEWHOME_JSON_STRING = "app_newhome_json_string";
    public static String getNewhomeJson(Context context){
        return context.getSharedPreferences(APP_NEWHOME_JSON_PRE, Context.MODE_PRIVATE).getString(APP_NEWHOME_JSON_STRING, "");
    }
    public static void setNewhomeJson(Context context, String json){
        context.getSharedPreferences(APP_NEWHOME_JSON_PRE, Context.MODE_PRIVATE).edit().putString(APP_NEWHOME_JSON_STRING, json).commit();
    }


    private static final String APP_START_PRE = "app_start_pre";
    private static final String APP_START_STRING = "app_start_string";
    public static int getStartFlag(Context context){
        return context.getSharedPreferences(APP_START_PRE, Context.MODE_PRIVATE).getInt(APP_START_STRING, 0);
    }
    public static void setStartFlag(Context context, int fag){
        context.getSharedPreferences(APP_START_PRE, Context.MODE_PRIVATE).edit().putInt(APP_START_STRING, fag).commit();
    }


    private static final String APP_CHENG_TAO_PRE = "app_cheng_tao_pre";
    private static final String APP_CHENG_TAO_STRING = "app_cheng_tao_string";
    public static int getChentaoFlag(Context context){
        return context.getSharedPreferences(APP_CHENG_TAO_PRE, Context.MODE_PRIVATE).getInt(APP_CHENG_TAO_STRING, 0);
    }
    public static void setChentaoFlag(Context context, int chentao){
        context.getSharedPreferences(APP_CHENG_TAO_PRE, Context.MODE_PRIVATE).edit().putInt(APP_CHENG_TAO_STRING, chentao).commit();
    }


    private static final String APP_PROVINCE_CITY_PRE = "app_province_city_pre";
    private static final String APP_PROVINCE_CITY_STRING = "app_province_city_string";
    public static String getSaveCityFlag(Context context){
        return context.getSharedPreferences(APP_PROVINCE_CITY_PRE, Context.MODE_PRIVATE).getString(APP_PROVINCE_CITY_STRING, "");
    }
    public static void setSaveCityFlag(Context context, String chentao){
        context.getSharedPreferences(APP_PROVINCE_CITY_PRE, Context.MODE_PRIVATE).edit().putString(APP_PROVINCE_CITY_STRING, chentao).commit();
    }


    private static final String APP_TOOL_PRE = "app_tool_pre";
    private static final String APP_TOOL_STRING = "app_tool_string";
    public static String getToolFlag(Context context){
        return context.getSharedPreferences(APP_TOOL_PRE, Context.MODE_PRIVATE).getString(APP_TOOL_STRING, "");
    }
    public static void setToolFlag(Context context, String chentao){
        context.getSharedPreferences(APP_TOOL_PRE, Context.MODE_PRIVATE).edit().putString(APP_TOOL_STRING, chentao).commit();
    }



    private static final String APP_COMPANY_FADAN_PRE = "app_company_fadan_pre";
    private static final String APP_COMPANY_FADAN_STRING = "app_company_fadan_string";
    public static int getCompanyFlag(Context context){
        return context.getSharedPreferences(APP_COMPANY_FADAN_PRE, Context.MODE_PRIVATE).getInt(APP_COMPANY_FADAN_STRING, 0);
    }
    public static void setCompanyFlag(Context context, int company){
        context.getSharedPreferences(APP_COMPANY_FADAN_PRE, Context.MODE_PRIVATE).edit().putInt(APP_COMPANY_FADAN_STRING, company).commit();
    }

}
