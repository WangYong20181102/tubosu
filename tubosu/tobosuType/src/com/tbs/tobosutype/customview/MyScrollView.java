package com.tbs.tobosutype.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Mr.Lin on 2017/12/6 16:25.
 */

public class MyScrollView extends ScrollView {

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public interface OnScrollLister {
        void onScroll(int scrollY);
    }

    private OnScrollLister listener = null;

    public void setOnScrollListener(OnScrollLister listener) {
        this.listener = listener;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (listener != null) {
            listener.onScroll(getScrollY());
        }
    }
}
