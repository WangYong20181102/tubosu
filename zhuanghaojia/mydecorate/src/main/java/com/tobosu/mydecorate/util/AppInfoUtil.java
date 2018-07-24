package com.tobosu.mydecorate.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by Mr.Lin on 2018/6/25 11:22.
 */
public class AppInfoUtil {
    //用户的Userid
    public static String getUserid(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("userid", "");
    }

    public static void setUserid(Context context, String userid) {
        context.getSharedPreferences("userInfo", 0).edit().putString("userid", userid).commit();
    }

    //获取App的版本信息
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //获取新的渠道代码值 链接中的tbschcode
    public static String getNewChannType(Context context) {
        if (AppInfoUtil.getChannType(context).equals("ali")) {
            return "al_yysc_zhj";
        } else if (AppInfoUtil.getChannType(context).equals("anzhi")) {
            return "az_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("appbaidu")) {
            return "bd_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("apphuawei")) {
            return "hw_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("applenovo")) {
            return "lx_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("appmeizu")) {
            return "mz_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("appoppo")) {
            return "oppo_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("appqihu")) {
            return "360_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("appttt")) {
            return "cz_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("appvivo")) {
            return "vivo_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("appxiaomi")) {
            return "xm_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("appyyb")) {
            return "tx_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("yybff")) {
            return "tx_yysc_yybff";
        }else if (AppInfoUtil.getChannType(context).equals("jinli")) {
            return "jl_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("leshi")) {
            return "tbs";
        }else if (AppInfoUtil.getChannType(context).equals("sougou")) {
            return "sg_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("sumsung")) {
            return "sx_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("youyi")) {
            return "yysc_yysc_zhj";
        }else if (AppInfoUtil.getChannType(context).equals("zhuanghaojia")) {
            return "";
        }else if (AppInfoUtil.getChannType(context).equals("tbspc")) {
            return "tbspc_pczy";
        }else {
            return "";
        }
    }

    //获取渠道信息（在AndroidManifest文件中的渠道信息）
    public static String getChannType(Context context) {
        String mChannType = "";
        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (!TextUtils.isEmpty(applicationInfo.metaData.getString("UMENG_CHANNEL"))) {
                mChannType = applicationInfo.metaData.getString("UMENG_CHANNEL");
                return mChannType;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mChannType;
    }

    public static void setLat(Context context, String lat) {
        context.getSharedPreferences("city", 0).edit().putString("lat", lat).commit();
    }

    public static String getLat(Context context) {
        return context.getSharedPreferences("city", 0).getString("lat", "22");
    }

    public static void setLng(Context context, String lng) {
        context.getSharedPreferences("city", 0).edit().putString("lng", lng).commit();
    }

    public static String getLng(Context context) {
        return context.getSharedPreferences("city", 0).getString("lng", "113");
    }
}
