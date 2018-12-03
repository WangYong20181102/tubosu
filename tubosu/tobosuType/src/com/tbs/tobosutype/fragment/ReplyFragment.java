package com.tbs.tobosutype.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.ReplyFragmentAdapter;
import com.tbs.tobosutype.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Wang on 2018/12/3 13:55.
 */
public class ReplyFragment extends BaseFragment {
    @BindView(R.id.rv_reply)
    RecyclerView recyclerView;
    @BindView(R.id.dq_swipe)
    SwipeRefreshLayout dqSwipe;
    private ReplyFragmentAdapter adapter;
    private List<String> stringList;
    private Unbinder unbinder;
    private int mPage = 1;//用于分页的数据
    private boolean isDownRefresh = false;//是否是下拉刷新
    private int mPageSize = 15;//用于分页的数据
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, null);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        recyclerView.setBackgroundColor(Color.WHITE);
        //下拉刷新
        dqSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dqSwipe.setBackgroundColor(Color.WHITE);
        dqSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dqSwipe.setOnRefreshListener(onRefreshListener);

        stringList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            stringList.add("afads");
        }

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(onTouchListener);
        recyclerView.setOnScrollListener(onScrollListener);


        adapter = new ReplyFragmentAdapter(getActivity(), stringList);
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
