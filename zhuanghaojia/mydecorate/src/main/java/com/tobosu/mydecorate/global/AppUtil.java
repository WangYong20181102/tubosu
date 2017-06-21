package com.tobosu.mydecorate.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by dec on 2016/10/11.
 */

public class AppUtil {

    public static String getToekn(Context context) {
        return context.getSharedPreferences("userInfo", 0).getString("token", "");
    }

    public static String getAppVersion(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {

        }
        return versionName;
    }
}
