package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorationQuestionFragmentAdapter;
import com.tbs.tobosutype.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Wang on 2018/11/5 13:46.
 */
public class DecorationQuestionFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.fragment_dq_rv)
    RecyclerView fragmentDqRv;
    @BindView(R.id.dq_swipe)
    SwipeRefreshLayout dqSwipe;     //刷新
    private DecorationQuestionFragmentAdapter decorationQuestionFragmentAdapter;
    private List<String> list = new ArrayList<>();
    private Context context;
    private List<String> stringList;
    private int position;
    private int mPage = 1;//用于分页的数据
    private boolean isDownRefresh = false;//是否是下拉刷新
    private int mPageSize = 20;//用于分页的数据
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        stringList = bundle.getStringArrayList("stringList");
        position = bundle.getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decorationquestion, null);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        initViewEvent();
        return view;
    }


    /**
     * 构造单例
     *
     * @param stringList
     * @param position
     * @return
     */
    public static DecorationQuestionFragment newInstance(List<String> stringList, int position) {
        DecorationQuestionFragment newFragment = new DecorationQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("stringList", (ArrayList<String>) stringList);
        bundle.putInt("position", position);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    private void initViewEvent() {

        //下拉刷新
        dqSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dqSwipe.setBackgroundColor(Color.WHITE);
        dqSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dqSwipe.setOnRefreshListener(onRefreshListener);

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentDqRv.setLayoutManager(layoutManager);
        ((SimpleItemAnimator) fragmentDqRv.getItemAnimator()).setSupportsChangeAnimations(false);
        fragmentDqRv.setOnTouchListener(onTouchListener);
        fragmentDqRv.setOnScrollListener(onScrollListener);

        HttpGetImageList(mPage);

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
    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initData();
        }
    };

    private void initData() {
        mPage = 1;
        isDownRefresh = true;
        HttpGetImageList(mPage);
    }

    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastVisibleItemPosition() + 1 == decorationQuestionFragmentAdapter.getItemCount()) {
                LoadMore();
            }
        }
    };

    //加载更多数据
    private void LoadMore() {
        mPage++;
        HttpGetImageList(mPage);
    }

    //网络请求
    private void HttpGetImageList(int mPage) {
        Log.d("aaaaaaaaaaaaaaaaaaaaaa", mPage + "---" + position);


        //下拉刷新停止
        dqSwipe.setRefreshing(false);

        if (isDownRefresh){
            list.clear();
            list.add("34");
            list.add("56");
            list.add("24");
            list.add("31");
            list.add("78");
            list.add("90");
            list.add("123");
            list.add("5647");
            list.add("75");
            list.add("12");
            list.add("80");
        }
        else {
            list.add("001");
            list.add("002");
            list.add("003");
            list.add("004");
            list.add("005");
        }

        if (decorationQuestionFragmentAdapter == null) {
            decorationQuestionFragmentAdapter = new DecorationQuestionFragmentAdapter(getActivity(), list);
            fragmentDqRv.setAdapter(decorationQuestionFragmentAdapter);
            decorationQuestionFragmentAdapter.notifyDataSetChanged();
        }
        if (isDownRefresh) {
            isDownRefresh = false;
            fragmentDqRv.scrollToPosition(0);
            decorationQuestionFragmentAdapter.notifyDataSetChanged();
        } else {
            decorationQuestionFragmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
