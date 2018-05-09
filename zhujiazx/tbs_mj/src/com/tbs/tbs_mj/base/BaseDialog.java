package com.tbs.tbs_mj.base;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Mr.Lin on 2018/1/23 15:19.
 */

public class BaseDialog extends Dialog {
    private int res;

    public BaseDialog(Context context, int theme, int res) {
        super(context, theme);
        // TODO 自动生成的构造函数存根
        setContentView(res);
        this.res = res;
        setCanceledOnTouchOutside(false);
    }
}
