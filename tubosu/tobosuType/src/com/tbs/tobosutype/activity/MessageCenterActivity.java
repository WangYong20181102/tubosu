package com.tbs.tobosutype.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.MessageCenterAdapter;
import com.tbs.tobosutype.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2018/12/3 16:47.
 */
public class MessageCenterActivity extends BaseActivity {
    @BindView(R.id.rv_reply)
    RecyclerView recyclerView;
    @BindView(R.id.dq_swipe)
    SwipeRefreshLayout dqSwipe;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.ll_message_center)
    LinearLayout llMessageCenter;
    private LinearLayoutManager layoutManager;
    private int mPage = 1;//用于分页的数据
    private boolean isDownRefresh = false;//是否是下拉刷新
    private int mPageSize = 15;//用于分页的数据
    private List<String> stringList;
    private MessageCenterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        llMessageCenter.setBackgroundColor(Color.WHITE);

        stringList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            stringList.add("afads");
        }


        //下拉刷新
        dqSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dqSwipe.setBackgroundColor(Color.WHITE);
        dqSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dqSwipe.setOnRefreshListener(onRefreshListener);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(onTouchListener);
        recyclerView.setOnScrollListener(onScrollListener);


        adapter = new MessageCenterAdapter(this,stringList);
        recyclerView.setAdapter(adapter);

    }
    //touch
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (dqSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (adapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastVisibleItemPosition() + 1 == adapter.getItemCount()) {
                    LoadMore();
                }
            }
        }
    };
    //加载更多数据
    private void LoadMore() {
        mPage++;
//        HttpGetImageList(mPage);
    }
    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initRequest();
        }

    };
    private void initRequest() {
        mPage = 1;
        isDownRefresh = true;
        dqSwipe.setRefreshing(true);
    }
    @OnClick({R.id.rl_back, R.id.ll_message_center})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_message_center:
                break;
        }
    }
}
