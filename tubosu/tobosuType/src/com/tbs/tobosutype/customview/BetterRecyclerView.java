package com.tbs.tobosutype.customview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Lie on 2017/10/26.
 */

public class BetterRecyclerView extends RecyclerView {

    private ViewGroup parent;

    public BetterRecyclerView(Context context) {
        super(context);
    }

    public BetterRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNestParent(ViewGroup parent) {
        this.parent = parent;
    }

    private  int lastX = -1;
    private  int lastY = -1;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
//                Log.i("dispatchTouchEvent", "dealtX:=" + dealtX);
//                Log.i("dispatchTouchEvent", "dealtY:=" + dealtY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
