package com.tbs.tbsbusiness.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Mr.Lin on 2018/6/5 10:15.
 */
public class ToastUtil {
    private static boolean isShowToast = true;

    public static void showToast(Context context, String msg) {
        if (isShowToast) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
