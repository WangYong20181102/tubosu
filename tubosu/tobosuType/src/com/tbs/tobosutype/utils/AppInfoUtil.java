package com.tbs.tobosutype.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.SystemStatusManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AppInfoUtil {
    private static final String TAG = AppInfoUtil.class.getSimpleName();

    /**
     * 刚刚登陆标记
     */
    public static boolean ISJUSTLOGIN = false;

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
    /**
     * 获取手机mac地址<br/>
     * 错误返回12个0
     */
    public static String getMacAddress(Context context) {
        // 获取mac地址：
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr
                    .getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress()))
                    macAddress = info.getMacAddress().replace(":", "");
                else
                    return macAddress;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
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

    public static String getCityName(Context context) {
        return context.getSharedPreferences("city", 0).getString("cityName", "深圳");
    }

    public static void setCityName(Context context, String city){
        context.getSharedPreferences("city", 0).edit().putString("cityName", city).commit();
    }

    public static void setLat(Context context, String lat){
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

    public static String getToekn(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("token", "");
    }

    public static void setToken(Context context, String token){
        context.getSharedPreferences("userInfo", 0).edit().putString("token", token).commit();
    }

    public static String getTypeid(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("typeid", "");
    }

    public static String getCellPhone(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("cellphone", "");
    }

    public static String getMark(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mark", "0");
    }

    public static String getUserid(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("userid", "");
    }

    public static String getImageActivityCatch(Context context) {
        return context.getSharedPreferences("IsImageCache", 0).getString("result", "");
    }

    //获取风格缓存
    public static String getStyleFgCache(Context context) {
        return context.getSharedPreferences("StyleFgCache", 0).getString("resultJson", "");
    }

    //获取户型缓存
    public static String getStyleHxCache(Context context) {
        return context.getSharedPreferences("StyleHxCache", 0).getString("resultJson", "");
    }

    //获取面积缓存
    public static String getStyleMjCache(Context context) {
        return context.getSharedPreferences("StyleMjCache", 0).getString("resultJson", "");
    }

    public static String getDeviceName() {
        return "android";
    }

    public static RequestParams getPublicParams(Context context) {
        RequestParams params = new RequestParams();
        params.put("device", getDeviceName());
        params.put("version", getAppVersionName(context));
        params.put("city", getCityName(context));
        params.put("lat", getLat(context));
        params.put("lng", getLng(context));
        return params;
    }

    public static HashMap<String,Object> getPublicHashMapParams(Context context) {
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("device", getDeviceName());
        params.put("version", getAppVersionName(context));
        params.put("city", getCityName(context));
        params.put("lat", getLat(context));
        params.put("lng", getLng(context));

//        params.put("lat", !"".equals(getLat(context))? getLat(context) : Constant.LAT);
//        params.put("lng", !"".equals(getLng(context))? getLng(context) : Constant.LNG);

        return params;
    }


    /***
     * 将本app中所有的Activity标题栏设置为土拨鼠颜色风格
     *
     * @param activity
     */
    public static void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.color_icon);
            activity.getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }

    /***
     * 设置沉浸式
     *
     * @param activity
     */
    public static void setActivityTheme1(Activity activity, int color) {
        if (AndtoidRomUtil.isMIUI() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 小米手机
            boolean sIsMiuiV6 = false;
            try {
                Class<?> sysClass = Class.forName("android.os.SystemProperties");
                Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
                sIsMiuiV6 = "V6".equals((String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (sIsMiuiV6) {
                Class<? extends Window> clazz = activity.getWindow().getClass();
                try {
                    int darkModeFlag = 0;
                    Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                    Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                    darkModeFlag = field.getInt(layoutParams);
                    Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                    extraFlagField.invoke(activity.getWindow(), true ? darkModeFlag : 0, darkModeFlag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);
            activity.getWindow().getDecorView().setFitsSystemWindows(true);
        }

    }


    public static void setActivityTheme(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            //tintManager.setTintColor(R.color.red);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.cal_calculater_color);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
//			tintManager.setNavigationBarTintResource(R.color.mask_tags_1);
        }
    }


    public static void setSelectedImageDetailActivityTheme(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            //tintManager.setTintColor(R.color.red);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.selecte_image_detail_bg_color);
        }
    }

    public static void hideKeyboard(FragmentActivity activity, EditText edit) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            edit.setInputType(InputType.TYPE_NULL);
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edit, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        edit.requestFocus();
    }

    public static void setTitleBarColor(Activity activity, int mColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(mColor);
            activity.getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }


}
