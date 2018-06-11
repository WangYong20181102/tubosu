package com.tbs.tbsbusiness.base;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Mr.Lin on 2018/6/6 16:07.
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
