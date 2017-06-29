package com.tbs.tobosutype.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Mr.Lin on 2017/6/9 13:52.
 */

@SuppressLint("AppCompatCustomView")
public class PmTextView extends TextView {
    public PmTextView(Context context) {
        super(context);
    }

    public PmTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PmTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(true, direction, previouslyFocusedRect);
    }
}
