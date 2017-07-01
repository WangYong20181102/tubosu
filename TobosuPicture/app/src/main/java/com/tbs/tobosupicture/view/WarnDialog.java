package com.tbs.tobosupicture.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by Mr.Lin on 2017/6/30 17:31.
 */

public class WarnDialog extends Dialog {
    public WarnDialog(@NonNull Context context) {
        super(context);
    }

    public WarnDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected WarnDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
