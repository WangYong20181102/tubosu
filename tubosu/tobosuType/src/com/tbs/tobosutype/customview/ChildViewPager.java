package com.tbs.tobosutype.customview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ChildViewPager extends ViewPager{
    PointF downP = new PointF();
    PointF curP = new PointF(); 
    OnSingleTouchListener onSingleTouchListener;

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        curP.x = arg0.getX();
        curP.y = arg0.getY();

        if(arg0.getAction() == MotionEvent.ACTION_DOWN){
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if(arg0.getAction() == MotionEvent.ACTION_MOVE){
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if(arg0.getAction() == MotionEvent.ACTION_UP){
            if(downP.x==curP.x && downP.y==curP.y){
                onSingleTouch();
                return true;
            }
        }

        return super.onTouchEvent(arg0);
    }
    public void onSingleTouch() {
        if (onSingleTouchListener!= null) {
            onSingleTouchListener.onSingleTouch();
        }
    }
    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

}
