package com.tobosu.mydecorate.view;

/**
 * Created by dec on 2016/11/8.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * 类说明: 自定义的上下拉刷新控件
 * Date: 2016/5/9 17:27
 */
public class PullRefreshView extends ViewGroup {

    private static final String TAG = PullRefreshView.class.getSimpleName();

    protected Context mContext;

    //头部下拉的最小高度
    private static final int HEAD_DEFAULT_HEIGHT = 100;

    //尾部上拉的最小高度
    private static final int TAIL_DEFAULT_HEIGHT = 100;

    /**
     * 头部容器
     */
    private LinearLayout mHeadLayout;

    /**
     * 头部View
     */
    private View mHead;

    /**
     * 头部的高度
     */
    private int mHeadHeight = HEAD_DEFAULT_HEIGHT;


    /**
     * 尾部容器
     */
    private LinearLayout mTailLayout;

    /**
     * 尾部View
     */
    private View mTail;

    /**
     * 尾部的高度
     */
    private int mTailHeight = TAIL_DEFAULT_HEIGHT;


    /**
     * 滑动的偏移量
     */
    private int mScrollOffset = 0;

    /**
     * 标记 无状态（既不是上拉 也 不是下拉）
     */
    private final int STATE_NOT = -1;

    /**
     * 标记 上拉状态
     */
    private final int STATE_UP = 1;

    /**
     * 标记 下拉状态
     */
    private final int STATE_DOWN = 2;

    /**
     * 当前状态
     */
    private int mCurrentState = STATE_NOT;

    /**
     * 是否处于下拉 正在更新状态
     */
    private boolean mIsPullDown = false;

    /**
     * 是否处于上拉 正在加载状态
     */
    private boolean mIsPullUp = false;

    /**
     * 是否启用下拉功能（默认不开启）
     */
    private boolean mIsDownRefresh = true;

    /**
     * 是否启用上拉功能（默认不开启）
     */
    private boolean mIsUpRefresh = false;

    /**
     * 加载状态
     */
    private boolean mIsLoading = false;

    private int mDamp = 4;

    /**
     * 头部状态监听器
     */
    private OnHeadStateListener mHeadStateListener;

    /**
     * 尾部状态监听器
     */
    private OnTailStateListener mTailStateListener;

    /**
     * 上拉监听器
     */
    private OnPullUpRefreshListener mPullUpRefreshListener;

    /**
     * 下拉监听器
     */
    private OnPullDownRefreshListener mPullDownRefreshListener;

    /**
     * 是否还有更多数据。
     */
    private boolean isMore = true;

    public PullRefreshView(Context context) {
        this(context, null);
    }

    public PullRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setClipToPadding(false);
        initHeadLayout();
        initTailLayout();
    }

    /**
     * 初始化头部
     */
    private void initHeadLayout() {
        mHeadLayout = new LinearLayout(mContext);
        LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mHeadLayout.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        mHeadLayout.setLayoutParams(lp);
        addView(mHeadLayout);
    }

    /**
     * 设置头部View
     *
     * @param head
     */
    public void setHead(View head) {
        mHead = head;
        mHeadLayout.removeAllViews();
        mHeadLayout.addView(mHead);

        //获取头部高度
        mHeadLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mHead.getHeight() > HEAD_DEFAULT_HEIGHT) {
                    mHeadHeight = mHead.getHeight();
                } else {
                    mHeadHeight = HEAD_DEFAULT_HEIGHT;
                }
                Log.e(TAG, "mHeadHeight" + mHeadHeight);
                //当获取到头部高度的时候，如果正处于下拉刷新状态，应该把头部打开。
                if (mIsPullDown) {
                    scroll(-mHeadHeight);
                }
                invalidate();
            }
        });
    }

    /**
     * 初始化尾部
     */
    private void initTailLayout() {
        mTailLayout = new LinearLayout(mContext);
        LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTailLayout.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        mTailLayout.setLayoutParams(lp);
        addView(mTailLayout);
    }

    /**
     * 设置尾部View
     *
     * @param tail
     */
    public void setTail(View tail) {
        mTail = tail;
        mTailLayout.removeAllViews();
        mTailLayout.addView(mTail);

        //获取尾部高度
        mTailLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mTail.getHeight() > TAIL_DEFAULT_HEIGHT) {
                    mTailHeight = mTail.getHeight();
                } else {
                    mTailHeight = TAIL_DEFAULT_HEIGHT;
                }
                Log.e(TAG, "mTailHeight" + mTailHeight);
                //当获取到尾部高度的时候，如果正处于上拉刷新状态，应该把尾部打开。
                if (mIsPullUp) {
                    scroll(mTailHeight);
                }
            }
        });
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //布局头部
        View head = getChildAt(0);
        head.layout(getPaddingLeft(), -mHeadHeight, getPaddingLeft() + head.getMeasuredWidth(), 0);

        //布局尾部
        View tail = getChildAt(1);
        tail.layout(getPaddingLeft(), getMeasuredHeight(), getPaddingLeft() + tail.getMeasuredWidth(), getMeasuredHeight() + mTailHeight);

        //布局内容容器
        int count = getChildCount();
        if (count > 2) {
            View content = getChildAt(2);
            content.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + content.getMeasuredWidth(), getPaddingTop() + content.getMeasuredHeight());
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //测量头部高度
        View head = getChildAt(0);
        measureChild(head, widthMeasureSpec, heightMeasureSpec);

        //测量尾部高度
        View tail = getChildAt(1);
        measureChild(tail, widthMeasureSpec, heightMeasureSpec);

        //测量内容容器宽高
        int count = getChildCount();
        int contentHeight = 0;
        int contentWidth = 0;
        if (count > 2) {
            View content = getChildAt(2);
            measureChild(content, widthMeasureSpec, heightMeasureSpec);
            contentHeight = content.getMeasuredHeight();
            contentWidth = content.getMeasuredWidth();
        }

        //设置PullRefresView的宽高
        setMeasuredDimension(measureWidth(widthMeasureSpec, contentWidth), measureHeigth(heightMeasureSpec, contentHeight));
    }

    private int measureWidth(int measureSpec, int contentWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = contentWidth + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeigth(int measureSpec, int contentHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = contentHeight + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    /**
     * 设置是否启用上下拉功能
     *
     * @param isDownRefresh 是否开启下拉功能 默认开启
     * @param isUpRefresh   是否开启上拉功能 默认不开启
     */
    public void setRefresh(boolean isDownRefresh, boolean isUpRefresh) {
        mIsDownRefresh = isDownRefresh;
        mIsUpRefresh = isUpRefresh;
    }

    /**
     * 还原
     */
    private void restore() {
        mCurrentState = STATE_NOT;
        scroll(0);
    }

    /**
     * 通知刷新完成
     */
    public void refreshFinish() {
        restore();
        if (mIsLoading) {
            mIsLoading = false;
            if (mIsPullUp) {
                mIsPullUp = false;
                if (mTailStateListener != null && isMore) {
                    mTailStateListener.onRetractTail(mTail);
                }
            } else if (mIsPullDown) {
                mIsPullDown = false;
                if (mHeadStateListener != null) {
                    mHeadStateListener.onRetractHead(mHead);
                }
            }
        }
    }

    public void isMore(boolean isMore) {
        this.isMore = isMore;
        if (mTailStateListener != null) {
            if (isMore) {
                mTailStateListener.onHasMore(mTail);
            } else {
                mTailStateListener.onNotMore(mTail);
            }
        }
    }

    /**
     * 触发下拉刷新
     */
    public void triggerPullDownRefresh() {

        if (!mIsDownRefresh) {
            return;
        }

        if (!mIsLoading) {
            mIsLoading = true;
            mIsPullDown = true;
            mCurrentState = STATE_NOT;
            scroll(-mHeadHeight);
            if (mHeadStateListener != null) {
                mHeadStateListener.onRefreshHead(mHead);
            }

            if (mPullDownRefreshListener != null) {
                mPullDownRefreshListener.onRefresh();
            }
        }
    }

    /**
     * 触发上拉刷新
     */
    public void triggerPullUpRefresh() {

        if (!mIsUpRefresh) {
            return;
        }

        if (!mIsLoading) {
            mIsLoading = true;
            mIsPullUp = true;
            mCurrentState = STATE_NOT;
            scroll(mTailHeight);
            if (mTailStateListener != null && isMore) {
                mTailStateListener.onRefreshTail(mTail);
            }

            if (isMore) {
                if (mPullUpRefreshListener != null) {
                    mPullUpRefreshListener.onRefresh();
                }
            } else {
                refreshFinish();
            }

        }
    }

    /**
     * @param offset
     */
    private void scroll(int offset) {

        if (offset < 0 && !mIsDownRefresh) {
            return;
        }

        if (offset > 0 && !mIsUpRefresh) {
            return;
        }

        scrollTo(0, offset);
        mScrollOffset = Math.abs(offset);

        if (mCurrentState == STATE_DOWN && mHeadStateListener != null) {
            mHeadStateListener.onScrollChange(mHead, mScrollOffset, mScrollOffset >= mHeadHeight ? 100 : mScrollOffset * 100 / mHeadHeight);
        }

        if (mCurrentState == STATE_UP && mTailStateListener != null && isMore) {
            mTailStateListener.onScrollChange(mTail, mScrollOffset, mScrollOffset >= mTailHeight ? 100 : mScrollOffset * 100 / mTailHeight);
        }
    }

    /**
     * 设置拉动阻力 （1到10）
     *
     * @param damp
     */
    public void setDamp(int damp) {
        if (damp < 1) {
            mDamp = 1;
        } else if (damp > 10) {
            mDamp = 10;
        } else {
            mDamp = damp;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mY > y) {
                    if (mCurrentState == STATE_UP) {
                        scroll((mY - y) / mDamp);
                    }
                } else if (mCurrentState == STATE_DOWN) {
                    scroll((mY - y) / mDamp);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsPullDown && !mIsPullUp) {
                    if (mCurrentState == STATE_DOWN) {
                        if (mScrollOffset < mHeadHeight) {
                            restore();
                        } else {
                            triggerPullDownRefresh();
                        }
                    } else if (mCurrentState == STATE_UP) {
                        if (mScrollOffset < mTailHeight) {
                            restore();
                        } else {
                            triggerPullUpRefresh();
                        }
                    } else {
                        restore();
                    }
                }
                mY = 0;

                break;
            default:

                break;
        }
        return super.onTouchEvent(event);
    }

    int mY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mY = (int) ev.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                if (mIsLoading) {
                    return false;
                }

                if (pullDown() && y - mY > 20) {
                    mCurrentState = STATE_DOWN;
                    return true;
                }

                if (pullUp() && mY - y > 20) {
                    mCurrentState = STATE_UP;
                    return true;
                }

                return false;
            case MotionEvent.ACTION_UP:

                return false;
        }

        return false;
    }

    protected boolean pullDown() {
        return mCurrentState != STATE_UP && mIsDownRefresh && isTop();
    }

    protected boolean pullUp() {
        return mCurrentState != STATE_DOWN && mIsUpRefresh && isBottom();
    }

    protected boolean isTop() {

        if (getChildCount() < 2) {
            return true;
        }

        View view = getChildAt(2);

        if (view instanceof ViewGroup) {

            if (view instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) view;
                return scrollView.getScrollY() <= 0;
            } else {
                return isChildTop((ViewGroup) view);
            }
        } else {
            return true;
        }
    }


    protected boolean isChildTop(ViewGroup viewGroup) {
        int minY = 0;
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            int topMargin = 0;
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                topMargin = ((MarginLayoutParams) lp).topMargin;
            }
            int top = view.getTop() - topMargin;
            minY = Math.min(minY, top);
        }
        return minY >= 0;
    }

    protected boolean isBottom() {

        if (getChildCount() < 2) {
            return false;
        }

        View view = getChildAt(2);

        if (view instanceof ViewGroup) {
            if (view instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) view;
                if (scrollView.getChildCount() > 0) {
                    return scrollView.getScrollY() >= scrollView.getChildAt(0).getHeight() - scrollView.getHeight();
                } else {
                    return true;
                }
            } else {
                return isChildBottom((ViewGroup) view);
            }
        } else {
            return true;
        }
    }


    protected boolean isChildBottom(ViewGroup viewGroup) {
        int maxY = 0;
        int count = viewGroup.getChildCount();

        if (count == 0) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            int bottomMargin = 0;
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                bottomMargin = ((MarginLayoutParams) lp).bottomMargin;
            }
            int bottom = view.getBottom() + bottomMargin;
            maxY = Math.max(maxY, bottom);
        }

        int h = viewGroup.getMeasuredHeight() - viewGroup.getPaddingBottom();

        return maxY <= h;
    }

    /**
     * 设置头部监听器
     *
     * @param listener
     */
    public void setOnHeadStateListener(OnHeadStateListener listener) {
        mHeadStateListener = listener;
    }

    /**
     * 设置尾部监听器
     *
     * @param listener
     */
    public void setOnTailStateListener(OnTailStateListener listener) {
        mTailStateListener = listener;
    }

    /**
     * 设置上拉监听器
     *
     * @param listener
     */
    public void setOnPullUpRefreshListener(OnPullUpRefreshListener listener) {
        mPullUpRefreshListener = listener;
    }

    /**
     * 设置下拉监听器
     *
     * @param listener
     */
    public void setOnPullDownRefreshListener(OnPullDownRefreshListener listener) {
        mPullDownRefreshListener = listener;
    }


    /**
     * 移除头部监听器
     */
    public void removeOnHeadStateListener() {
        mHeadStateListener = null;
    }

    /**
     * 移除尾部监听器
     */
    public void removeOnTailStateListener() {
        mTailStateListener = null;
    }

    /**
     * 移除上拉监听器
     */
    public void removeOnPullUpRefreshListener() {
        mPullUpRefreshListener = null;
    }

    /**
     * 移除下拉监听器
     */
    public void removeOnPullDownRefreshListener() {
        mPullDownRefreshListener = null;
    }


    //----------------  监听接口  -------------------//

    /**
     * 头部状态监听器
     */
    public interface OnHeadStateListener {

        /**
         * 头部滑动变化
         *
         * @param head         头部View
         * @param scrollOffset 滑动距离
         * @param scrollRatio  从开始到触发阀值的滑动比率（0到100）如果滑动到达了阀值，就算在滑动，这个值也是100
         */
        void onScrollChange(View head, int scrollOffset, int scrollRatio);

        /**
         * 头部处于刷新状态 （触发下拉刷新的时候调用）
         *
         * @param head 头部View
         */
        void onRefreshHead(View head);

        /**
         * 头部收起
         *
         * @param head 头部View
         */
        void onRetractHead(View head);

    }

    /**
     * 头部状态监听器
     */
    public interface OnTailStateListener {

        /**
         * 尾部滑动变化
         *
         * @param tail         尾部View
         * @param scrollOffset 滑动距离
         * @param scrollRatio  从开始到触发阀值的滑动比率（0到100）如果滑动到达了阀值，就算在滑动，这个值也是100
         */
        void onScrollChange(View tail, int scrollOffset, int scrollRatio);

        /**
         * 尾部处于加载状态 （触发上拉加载的时候调用）
         *
         * @param tail 尾部View
         */
        void onRefreshTail(View tail);

        /**
         * 尾部收起
         *
         * @param tail 尾部View
         */
        void onRetractTail(View tail);

        /**
         * 没有更多
         *
         * @param tail
         */
        void onNotMore(View tail);

        /**
         * 有更多
         *
         * @param tail
         */
        void onHasMore(View tail);
    }


    /**
     * 上拉加载监听器
     */
    public interface OnPullUpRefreshListener {
        void onRefresh();
    }

    /**
     * 下拉更新监听器
     */
    public interface OnPullDownRefreshListener {
        void onRefresh();
    }
}