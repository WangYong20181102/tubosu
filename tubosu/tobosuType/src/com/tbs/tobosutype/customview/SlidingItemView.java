package com.tbs.tobosutype.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.tbs.tobosutype.R;

/**
 * Created by Mr.Lin on 2017/6/27 09:12.
 */

public class SlidingItemView extends HorizontalScrollView {
    private ImageView mImageView_delete;//删除按钮
    private int mScrollWidth;//滑动的宽度
    private IonSlidingButtonListener mIonSlidingButtonListener;//滑动监听
    private Boolean isOpen = false;
    private Boolean once = false;

    public SlidingItemView(Context context) {
        this(context, null);
    }

    public SlidingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!once) {
            once = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
            //获取水平滚动条可以滑动的范围，即右侧按钮的宽度
            mScrollWidth = mImageView_delete.getWidth();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIonSlidingButtonListener.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollx();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mImageView_delete.setTranslationX(l - mScrollWidth);
    }

    //处理其他非滑动事件 进行删除按钮的关闭
    public void changeScrollx() {
        if (getScaleX() >= (mScrollWidth / 2)) {
            this.smoothScrollTo(mScrollWidth, 0);
            isOpen = true;
            mIonSlidingButtonListener.onMenuIsOpen(this);
        } else {
            this.smoothScrollTo(0, 0);
            isOpen = false;
        }
    }

    //显示删除按钮
    public void openMenu() {
        if (isOpen) {
            return;
        }
        this.smoothScrollTo(mScrollWidth, 0);
        isOpen = true;
        mIonSlidingButtonListener.onMenuIsOpen(this);
    }

    //关闭删除按钮
    public void closeMenu() {
        if (!isOpen) {
            return;
        }
        this.smoothScrollTo(0, 0);
        isOpen = false;
    }

    //实现接口
    public void setSlidingButtonListener(IonSlidingButtonListener listener) {
        mIonSlidingButtonListener = listener;
    }

    public interface IonSlidingButtonListener {
        void onMenuIsOpen(View view);

        void onDownOrMove(SlidingItemView slidingItemView);
    }
}
