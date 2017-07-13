package com.tbs.tobosupicture.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tbs.tobosupicture.R;


/**
 * Created by Mr.Lin on 2017/7/13 15:27.
 * 加载等待界面
 */

public class CustomWaitDialog {
    private Context mContext;
    private ImageView image;
    private ImageView loading_pic_bigView;
    private Dialog waitDialog;
    private Animation mAnimation;

    public CustomWaitDialog(Context mContext) {
        this.mContext = mContext;
        waitDialog = new Dialog(mContext, R.style.common_dialog);
        waitDialog.setContentView(R.layout.loading);
        waitDialog.setCanceledOnTouchOutside(false);

        /**
         * 设置幕布，也就是本dialog的背景层 dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的
         * ，1.0f时候，背景全部变黑暗。
         *
         * 如果要达到背景全部变暗的效果，需要设置
         * dialog.getWindow().addFlags(WindowManager.LayoutParams
         * .FLAG_DIM_BEHIND); ，否则，背景无效果。
         */
        Window window = waitDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.dimAmount = 0.8f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // waitDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        /**
         * 设置透明度，主要设置的是dialog自身的透明度
         */
        loading_pic_bigView = (ImageView) waitDialog
                .findViewById(R.id.loading_pic_bigView);
        loading_pic_bigView.setAlpha(0.6f);

        image = (ImageView) waitDialog.findViewById(R.id.loading_pic_big);
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading);
    }

    public void show() {
        image.startAnimation(mAnimation);
        waitDialog.show();
    }

    public void dismiss() {
        waitDialog.dismiss();
    }

    //用于网络请求中断操作
    public void setOnDismissListener(
            DialogInterface.OnDismissListener dismissListener) {
        waitDialog.setOnDismissListener(dismissListener);
    }
}
