package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.RecommendAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean._RecommendFriend;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.CustomWaitDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/7/15 16:24.
 * 推荐关注的列表页
 */

public class RecommendFragment extends BaseFragment {
    @BindView(R.id.recommend_recyclelist)
    RecyclerView recommendRecyclelist;
    @BindView(R.id.recommend_swip_refresh)
    SwipeRefreshLayout recommendSwipRefresh;
    Unbinder unbinder;

    private Context mContext;
    private String TAG = "RecommendFragment";
    private Gson mGson;
    private int mPage = 1;//加载更多的页码
    private LinearLayoutManager mLinearLayoutManager;
    private CustomWaitDialog customWaitDialog;
    private RecommendAdapter recommendAdapter;
    private boolean isLoading = false;//是否正在上拉加载更多数据 防止page一直在++
    //布局显示列表
    private ArrayList<_RecommendFriend> recommendFriensArrayList = new ArrayList<>();

    public RecommendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        HttpGetRecommendList(mPage);
        return view;
    }

    private void initView() {
        mGson = new Gson();
        //正在加载的图层
        customWaitDialog = new CustomWaitDialog(mContext);
        customWaitDialog.show();
        //下拉刷新配置
        recommendSwipRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        recommendSwipRefresh.setBackgroundColor(Color.WHITE);
        recommendSwipRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        recommendSwipRefresh.setOnRefreshListener(onRefreshListener);
        //配置列表
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recommendRecyclelist.setLayoutManager(mLinearLayoutManager);
        recommendRecyclelist.setOnTouchListener(onTouchListener);
        recommendRecyclelist.addOnScrollListener(onScrollListener);
    }

    //下拉刷新设置
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //重置数据
            mPage = 1;
            if (!recommendFriensArrayList.isEmpty()) {
                recommendFriensArrayList.clear();
            }
            HttpGetRecommendList(mPage);
        }
    };
    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (recommendSwipRefresh.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //上拉加载更多事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 0
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !recommendSwipRefresh.isRefreshing()
                    && !isLoading) {
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    //加载更多
    private void loadMore() {
        mPage++;
        HttpGetRecommendList(mPage);
    }

    private void HttpGetRecommendList(int mPage) {
        isLoading = true;
        customWaitDialog.dismiss();
		 recommendSwipRefresh.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("user_type", "2");
        param.put("page", mPage);
        param.put("pageSize", 10);
        HttpUtils.doPost(UrlConstans.GET_RECOMMEND_FRIENDS, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
                isLoading = false;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      
                        if (recommendAdapter != null) {
                            recommendAdapter.changLoadState(2);
                        }
                        Toast.makeText(mContext, "服务器链接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //数据请求成功 将正在加载的状态isLoading=false更新
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _RecommendFriend recommendFriend = mGson.fromJson(jsonArray.get(i).toString(), _RecommendFriend.class);
                            recommendFriensArrayList.add(recommendFriend);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               
                                if (recommendAdapter == null) {
                                    recommendAdapter = new RecommendAdapter(mContext, getActivity(), recommendFriensArrayList);
                                    recommendRecyclelist.setAdapter(recommendAdapter);
                                    recommendAdapter.notifyDataSetChanged();
                                } else {
                                    recommendAdapter.notifyDataSetChanged();
                                }
                                isLoading = false;
                            }
                        });

                    } else if (status.equals("201")) {
                        //暂无更多数据
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isLoading = false;
                                Toast.makeText(mContext, "暂无更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
