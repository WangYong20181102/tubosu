package com.tobosu.mydecorate.view;

/**
 * Created by dec on 2016/11/8.
 */

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 类说明：下拉刷新上拉加载更多的RecyclerView
 * Date: 2016/5/9 18:09
 */
public class PullRefreshRecyclerView extends PullRefreshView {

    /**
     * 内置的RecyclerView；
     */
    private RecyclerView mRecyclerView;

    /**
     * 可见的最后一个item
     */
    private int lastVisibleItem;

    /**
     * 可见的第一个item
     */
    private int firstVisibleItem;

    /**
     * 空数据提示布局容器
     */
    private LinearLayout mEmptyLayout;

    /**
     * 是否自动上拉刷新
     */
    private boolean isAutomaticUp = false;

    public PullRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        this.addView(mRecyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initListener();
    }

    private void initListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isAutomaticUp && newState == RecyclerView.SCROLL_STATE_IDLE && pullUp()) {
                    triggerPullUpRefresh();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                getVisibleItem();
            }
        });
    }

    private int getMax(int[] arr) {
        int max = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > max)
                max = arr[x];
        }
        return max;
    }

    private int getMin(int[] arr) {
        int min = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < min)
                min = arr[x];
        }
        return min;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getVisibleItem();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取当前第一个显示的item 和 最后一个显示的item.
     */
    private void getVisibleItem() {
        RecyclerView.LayoutManager layout = mRecyclerView.getLayoutManager();

        if (layout != null) {
            if (layout instanceof LinearLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) layout).findLastVisibleItemPosition();
                firstVisibleItem = ((LinearLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof GridLayoutManager) {
                lastVisibleItem = ((GridLayoutManager) layout).findLastVisibleItemPosition();
                firstVisibleItem = ((GridLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof StaggeredGridLayoutManager) {
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layout).getSpanCount()];
                ((StaggeredGridLayoutManager) layout).findLastVisibleItemPositions(lastPositions);
                lastVisibleItem = getMax(lastPositions);
                int[] firstPositions = new int[((StaggeredGridLayoutManager) layout).getSpanCount()];
                ((StaggeredGridLayoutManager) layout).findFirstVisibleItemPositions(firstPositions);
                firstVisibleItem = getMin(lastPositions);
            }
        }
    }

    @Override
    protected boolean isChildBottom(ViewGroup viewGroup) {

        boolean resultValue = false;
        int childNum = viewGroup.getChildCount();

        if (childNum == 0 || mRecyclerView.getChildCount() == 0) {
            resultValue = false;
        } else {

            if (mRecyclerView.equals(getChildAt(2)) && lastVisibleItem != mRecyclerView.getAdapter().getItemCount() - 1) {
                return false;
            }

            View view = viewGroup.getChildAt(childNum - 1);
            int bottomMargin = ((MarginLayoutParams) view.getLayoutParams()).bottomMargin;
            if (view.getBottom() + bottomMargin + viewGroup.getPaddingBottom() <= getHeight()) {
                resultValue = true;
            }
        }
        return resultValue;
    }

    @Override
    protected boolean isChildTop(ViewGroup viewGroup) {
        boolean resultValue = false;
        int childNum = viewGroup.getChildCount();

        if (childNum == 0) {
            resultValue = true;
        } else {
            if (mRecyclerView.equals(getChildAt(2)) && firstVisibleItem != 0) {
                return false;
            }

            View view = viewGroup.getChildAt(0);
            int topMargin = ((MarginLayoutParams) view.getLayoutParams()).topMargin;

            if (view.getTop() - topMargin - viewGroup.getPaddingTop() >= 0) {
                resultValue = true;
            }
        }
        return resultValue;
    }

    @Override
    public void triggerPullDownRefresh() {
        mRecyclerView.scrollToPosition(0);
        super.triggerPullDownRefresh();
    }

    /**
     * 设置空布局
     *
     * @param emptyView
     * @param layoutGravity 空布局在父布局的方向
     */
    public void setEmptyView(View emptyView, int layoutGravity) {
        if (mEmptyLayout == null) {
            initEmptyLayout();
        }
        mEmptyLayout.setGravity(layoutGravity);
        mEmptyLayout.addView(emptyView);
    }

    /**
     * 显示空布局
     */
    public void showEmptyView() {
        if (mEmptyLayout != null && mEmptyLayout.getParent() == null) {
            addView(mEmptyLayout, 2);
        }
        if (mRecyclerView.getParent() != null) {
            removeView(mRecyclerView);
        }
    }

    /**
     * 隐藏空布局
     */
    public void hideEmptyView() {
        if (mRecyclerView.getParent() == null) {
            addView(mRecyclerView, 2);
        }
        if (mEmptyLayout != null && mEmptyLayout.getParent() != null) {
            removeView(mEmptyLayout);
        }
    }

    private void initEmptyLayout() {
        mEmptyLayout = new LinearLayout(mContext);
        LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mEmptyLayout.setLayoutParams(lp);
        mEmptyLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * 滑动到底部时，是否自动触发上拉加载更多。
     *
     * @param isAutomaticUp
     */
    public void isAutomaticUp(boolean isAutomaticUp) {
        this.isAutomaticUp = isAutomaticUp;
    }

    //提供获取内置RecyclerView的方法
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    //********* 提供一系列对内置RecyclerView的操作方法 *********//

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        mRecyclerView.addItemDecoration(decor, index);
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        mRecyclerView.setPadding(left, top, right, bottom);
    }

}
