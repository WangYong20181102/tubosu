package com.tbs.tobosutype.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tbs.tobosutype.R;

/**
 * 简单的对话框提示工具类
 */
public class DialogUtil {

    private static TextView shareMoney;
    private static Button btYesSend;

    // 因为本类不是activity所以通过继承接口的方法获取到点击的事件
    public interface OnClickYesListener {
        abstract void onClickYes();
    }

    public interface OnClickNoListener {
        abstract void onClickNo();
    }

    /**
     * 对话框的点击事件
     *
     * @param dialog
     * @param bt_yesSend
     * @param bt_noSend
     * @param listenerYes
     * @param listenerNo
     */
    private static void myOnClickListener(final Dialog dialog, Button bt_yesSend, Button bt_noSend, final OnClickYesListener listenerYes,
                                          final OnClickNoListener listenerNo) {
        /**
         * 正确
         */
        bt_yesSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerYes != null) {
                    listenerYes.onClickYes();
                }
                dialog.dismiss();
            }
        });
        /**
         * 错误
         */
        bt_noSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果取消被点击
                if (listenerNo != null) {
                    listenerNo.onClickNo();
                }
                dialog.dismiss();
            }
        });

    }


    /**
     * 底部往上滑动对话框
     *
     * @param context
     * @param layoutID
     * @return
     */
    public static Dialog upSlideDialog(Context context, View layoutID) {
        Dialog dialog = new Dialog(context, R.style.ShowPinglunDialog);
        dialog.setContentView(layoutID);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setWindowAnimations(R.style.FromDownToUpStyleAnimation);
        window.setGravity(Gravity.BOTTOM);
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            dialog.show();
        }

        return dialog;
    }

}
