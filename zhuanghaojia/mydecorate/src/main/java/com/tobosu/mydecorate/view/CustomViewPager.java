package com.tobosu.mydecorate.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dec on 2016/11/3.
 */

public class CustomViewPager extends ViewPager {


    public CustomViewPager(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean in = super.onInterceptTouchEvent(ev);
        if (in) {
            getParent().requestDisallowInterceptTouchEvent(true);
            this.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }
}
