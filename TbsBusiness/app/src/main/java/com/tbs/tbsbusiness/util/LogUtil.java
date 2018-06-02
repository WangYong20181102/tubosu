package com.tbs.tbsbusiness.util;

import android.content.Context;
import android.util.Log;

/**
 * Created by Mr.Lin on 2018/6/2 17:16.
 */
public class LogUtil {
    //控制日志的输出
    private static boolean isShowErrorLog = true;

    public static void showErrorLog(String TAG, String LogMsg) {
        if (isShowErrorLog) {
            Log.e(TAG, LogMsg);
        }
    }
}
