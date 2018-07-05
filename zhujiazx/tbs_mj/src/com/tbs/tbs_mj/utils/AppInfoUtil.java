package com.tbs.tbs_mj.utils;

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
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.SystemStatusManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AppInfoUtil {
    private static final String TAG = AppInfoUtil.class.getSimpleName();

    /**
     * 刚刚登陆标记
     */
    public static boolean ISJUSTLOGIN = false;


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

    //获取新的渠道代码值 链接中的tbschcode
    public static String getNewChannType(Context context) {
        if (AppInfoUtil.getChannType(context).equals("ali")) {
            return "al_yysc_zjzx";
        } else if (AppInfoUtil.getChannType(context).equals("anzhi")) {
            return "az_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("appbaidu")) {
            return "bd_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("apphuawei")) {
            return "hw_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("applenovo")) {
            return "lx_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("appmeizu")) {
            return "mz_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("appoppo")) {
            return "oppo_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("appqihu")) {
            return "360_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("appttt")) {
            return "cz_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("appvivo")) {
            return "vivo_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("appxiaomi")) {
            return "xm_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("appyyb")) {
            return "tx_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("yybff")) {
            return "tx_yysc_yybff";
        }else if (AppInfoUtil.getChannType(context).equals("jinli")) {
            return "jl_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("leshi")) {
            return "zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("sougou")) {
            return "sg_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("sumsung")) {
            return "sx_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("youyi")) {
            return "yysc_yysc_zjzx";
        }else if (AppInfoUtil.getChannType(context).equals("zhuanghaojia")) {
            return "";
        }else if (AppInfoUtil.getChannType(context).equals("tbspc")) {
            return "tbspc_pczy";
        }else {
            return "";
        }
    }


    public static String getCityName(Context context) {
        return context.getSharedPreferences("city", 0).getString("cityName", "深圳");
    }

    public static void setCityName(Context context, String city) {
        context.getSharedPreferences("city", 0).edit().putString("cityName", city).commit();
    }


    public static String getCityInAnli(Context context) {
        return context.getSharedPreferences("city_anli", 0).getString("AnliCityName", "深圳");
    }

    public static void setCityNameInAnli(Context context, String city) {
        context.getSharedPreferences("city_anli", 0).edit().putString("AnliCityName", city).commit();
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

    // TODO: 2018/1/10  用户的基本信息↓↓↓↓
    //token
    public static String getToekn(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("token", "");
    }

    public static void setToken(Context context, String token) {
        context.getSharedPreferences("userInfo", 0).edit().putString("token", token).commit();
    }

    //用户的昵称
    public static String getUserNickname(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("nickname", "");
    }

    public static void setUserNickname(Context context, String nickname) {
        context.getSharedPreferences("userInfo", 0).edit().putString("nickname", nickname).commit();
    }

    //用户的头像
    public static String getUserIcon(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("icon", "");
    }

    public static void setUserIcon(Context context, String icon) {
        context.getSharedPreferences("userInfo", 0).edit().putString("icon", icon).commit();
    }

    //用户所在的城市
    public static String getUserCity(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("cityname", "");
    }

    public static void setUserCity(Context context, String cityname) {
        context.getSharedPreferences("userInfo", 0).edit().putString("cityname", cityname).commit();
    }

    //用户所在的省份
    public static String getUserProvince(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("province", "");
    }

    public static void setUserProvince(Context context, String province) {
        context.getSharedPreferences("userInfo", 0).edit().putString("province", province).commit();
    }

    //用户的Uuid
    public static String getUuid(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("id", "");
    }

    public static void setUuid(Context context, String id) {
        context.getSharedPreferences("userInfo", 0).edit().putString("id", id).commit();
    }

    //用户的id
    public static String getId(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("id", "");
    }

    public static void setId(Context context, String id) {
        context.getSharedPreferences("userInfo", 0).edit().putString("id", id).commit();
    }

    //用户的type_id
    public static String getTypeid(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("typeid", "");
    }

    public static void setTypeid(Context context, String typeid) {
        context.getSharedPreferences("userInfo", 0).edit().putString("typeid", typeid).commit();
    }

    //用户绑定的手机号码
    public static String getCellPhone(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("cellphone", "");
    }

    public static void setCellPhone(Context context, String cellphone) {
        context.getSharedPreferences("userInfo", 0).edit().putString("cellphone", cellphone).commit();
    }

    //用户身份的标识  其效果和type id 一致
    public static String getMark(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("mark", "0");
    }

    public static void setMark(Context context, String mark) {
        context.getSharedPreferences("userInfo", 0).edit().putString("mark", mark).commit();
    }

    //用户的Userid
    public static String getUserid(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("userid", "");
    }

    public static void setUserid(Context context, String userid) {
        context.getSharedPreferences("userInfo", 0).edit().putString("userid", userid).commit();
    }

    //用户的会员等级
    public static String getUserGrade(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("grade", "0");
    }

    public static void setUserGrade(Context context, String grade) {
        context.getSharedPreferences("userInfo", 0).edit().putString("grade", grade).commit();
    }

    //用户的是否绑定了手机号码
    public static String getUserCellphone_check(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("cellphone_check", "0");
    }

    public static void setUserCellphone_check(Context context, String cellphone_check) {
        context.getSharedPreferences("userInfo", 0).edit().putString("cellphone_check", cellphone_check).commit();
    }

    //用户的订单数
    public static String getUserOrder_count(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("order_count", "0");
    }

    public static void setUserOrder_count(Context context, String order_count) {
        context.getSharedPreferences("userInfo", 0).edit().putString("order_count", order_count).commit();
    }

    //用户的md5密码
    public static String getUserMd5PassWord(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("user_md5_password", "");
    }

    public static void setUserMd5PassWord(Context context, String user_md5_password) {
        context.getSharedPreferences("userInfo", 0).edit().putString("user_md5_password", user_md5_password).commit();
    }

    //用户的新订单数量
    public static String getUserNewOrderCount(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("new_order_count", "0");
    }

    public static void setUserNewOrderCount(Context context, String new_order_count) {
        context.getSharedPreferences("userInfo", 0).edit().putString("new_order_count", new_order_count).commit();
    }

    //用户的未量房数量
    public static String getUserNotLfOrderCount(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("not_lf_order_count", "0");
    }

    public static void setUserNotLfOrderCount(Context context, String not_lf_order_count) {
        context.getSharedPreferences("userInfo", 0).edit().putString("not_lf_order_count", not_lf_order_count).commit();
    }

    //用户的已量房数量
    public static String getUserLfOrderCount(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("lf_order_count", "0");
    }

    public static void setUserLfOrderCount(Context context, String lf_order_count) {
        context.getSharedPreferences("userInfo", 0).edit().putString("lf_order_count", lf_order_count).commit();
    }

    //用户是否有新消息
    public static String getUserIsNewSms(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("is_new_sms", "0");
    }

    public static void setUserIsNewSms(Context context, String is_new_sms) {
        context.getSharedPreferences("userInfo", 0).edit().putString("is_new_sms", is_new_sms).commit();
    }

    //用户未绑定手机号时用的uid
    public static String getUserBindId(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("user_bind_id", "");
    }

    public static void setUserBindId(Context context, String user_bind_id) {
        context.getSharedPreferences("userInfo", 0).edit().putString("user_bind_id", user_bind_id).commit();
    }
    // TODO: 2018/1/10  用户的基本信息↑↑↑↑
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

    //缓存逛图库的套图选择    add 2017.11.13
    public static void setSelectMsg(Context context, String selectMsg) {
        context.getSharedPreferences("SelectMsg", 0).edit().putString("selectMsgJson", selectMsg).commit();
    }

    //获取逛图库的套图缓存信息   add 2017.11.13
    public static String getSelectMsg(Context context) {
        return context.getSharedPreferences("SelectMsg", 0).getString("selectMsgJson", "");
    }

    //缓存逛图库的单图选择    add 2017.11.13
    public static void setSingleSelectMsg(Context context, String singleSelectMsg) {
        context.getSharedPreferences("SingleSelectMsg", 0).edit().putString("singleSelectMsgJson", singleSelectMsg).commit();
    }

    //获取逛图库的单图缓存信息   add 2017.11.13
    public static String getSingleSelectMsg(Context context) {
        return context.getSharedPreferences("SingleSelectMsg", 0).getString("singleSelectMsgJson", "");
    }

    //缓存逛图库列表的时间token   add 2017.11.13
    public static void setImageListDateToken(Context context, String ImageListDateToken) {
        context.getSharedPreferences("ImageListDateToken", 0).edit().putString("imageListdatetoken", ImageListDateToken).commit();
    }

    //缓存逛图库列表的时间token   add 2017.11.13
    public static String getImageListDateToken(Context context) {
        return context.getSharedPreferences("ImageListDateToken", 0).getString("imageListdatetoken", "");
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

    public static HashMap<String, Object> getPublicHashMapParams(Context context) {
        HashMap<String, Object> params = new HashMap<String, Object>();
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            SystemStatusManager tintManager = new SystemStatusManager(activity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.color_icon);
//            activity.getWindow().getDecorView().setFitsSystemWindows(true);
//        }
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
