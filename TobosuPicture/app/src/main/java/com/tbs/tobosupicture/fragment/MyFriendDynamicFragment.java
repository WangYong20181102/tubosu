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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.DynamicBaseAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean._DynamicBase;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/8/3 11:14.
 */

public class MyFriendDynamicFragment extends BaseFragment {
    @BindView(R.id.mfd_recycleview)
    RecyclerView mfdRecycleview;
    @BindView(R.id.mfd_swipe)
    SwipeRefreshLayout mfdSwipe;
    Unbinder unbinder;
    private Context mContext;
    private String TAG = "MyFriendDynamicFragment";
    private int mPage = 1;
    private Gson mGson;
    private boolean isLoading = false;
    private LinearLayoutManager mLinearLayoutManager;
    private DynamicBaseAdapter dynamicBaseAdapter;
    private ArrayList<_DynamicBase> dynamicBaseArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friend_dynamic, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initViewEvent();
        HttpGetDynamicList(mPage);
        return view;
    }

    private void initViewEvent() {
        mGson = new Gson();

        mfdSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        mfdSwipe.setBackgroundColor(Color.WHITE);
        mfdSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        mfdSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mfdRecycleview.setLayoutManager(mLinearLayoutManager);
        mfdRecycleview.setOnTouchListener(onTouchListener);
        mfdRecycleview.addOnScrollListener(onScrollListener);//上拉加载更多
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新重置数据
            mPage = 1;
            if (!dynamicBaseArrayList.isEmpty()) {
                dynamicBaseArrayList.clear();
            }
            HttpGetDynamicList(mPage);
        }
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mfdSwipe.isRefreshing()) {
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
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !isLoading
                    && !mfdSwipe.isRefreshing()) {
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        if (dynamicBaseAdapter != null) {
            dynamicBaseAdapter.changeAdapterState(1);
        }
        HttpGetDynamicList(mPage);
    }

    //获取列表
    private void HttpGetDynamicList(int mPage) {
        isLoading = true;

        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("page", mPage);
        param.put("page_size", "10");
        param.put("uid", SpUtils.getUserUid(mContext));
        HttpUtils.doPost(UrlConstans.MY_FRIENDS_DYNAMIC, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====");
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _DynamicBase dynamicBase = mGson.fromJson(jsonArray.get(i).toString(), _DynamicBase.class);
                            dynamicBaseArrayList.add(dynamicBase);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dynamicBaseAdapter == null) {
                                    dynamicBaseAdapter = new DynamicBaseAdapter(mContext, getActivity(), dynamicBaseArrayList);
                                    mfdRecycleview.setAdapter(dynamicBaseAdapter);
                                    dynamicBaseAdapter.notifyDataSetChanged();
                                } else {
                                    dynamicBaseAdapter.notifyDataSetChanged();
                                }
                                if (dynamicBaseAdapter != null) {
                                    dynamicBaseAdapter.changeAdapterState(2);
                                }
                            }
                        });

                    } else if (status.equals("201")) {
                        //没有更多数据
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "暂无更多数据。", Toast.LENGTH_SHORT).show();
                                if (dynamicBaseAdapter != null) {
                                    dynamicBaseAdapter.changeAdapterState(2);
                                }
                                if (dynamicBaseArrayList.isEmpty()) {
                                    //添加占位图
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    isLoading = false;
                    e.printStackTrace();
                }
                isLoading = false;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mfdSwipe.setRefreshing(false);
                    }
                });

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
