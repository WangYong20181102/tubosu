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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.MyFriendAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean._MyFriend;
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
 * Created by Mr.Lin on 2017/7/15 16:22.
 * 我的图友 显示我的图友列表Fragment
 * 含有下拉刷新以及上拉刷新
 */

public class MyFriendFragment extends BaseFragment {
    @BindView(R.id.myfriend_fragment_recyclelist)
    RecyclerView myfriendFragmentRecyclelist;
    Unbinder unbinder;
    @BindView(R.id.myfriend_swip_refresh)
    SwipeRefreshLayout myfriendSwipRefresh;
    @BindView(R.id.myfriend_none_friend)
    LinearLayout myfriendNoneFriend;

    private Context mContext;
    private String TAG = "MyFriendFragment";
    private int mPage = 1;//加载更多的页码数
    private Gson mGson;
    private MyFriendAdapter myFriendAdapter;
    private ArrayList<_MyFriend> myFriendArrayList = new ArrayList<>();//正式布局用的集合
    private LinearLayoutManager mLinearLayoutManager;
    private CustomWaitDialog customWaitDialog;
    private boolean isLoading = false;//是否正在上拉加载更多数据 防止page一直在++

    public MyFriendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfriend, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        HttpGetMyFriendList(mPage);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //初始化相关事件
    private void initView() {
        mGson = new Gson();
        //加载浮层
        customWaitDialog = new CustomWaitDialog(mContext);
        customWaitDialog.show();
        //下拉刷新的配置
        myfriendSwipRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        myfriendSwipRefresh.setBackgroundColor(Color.WHITE);
        myfriendSwipRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        myfriendSwipRefresh.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        myfriendFragmentRecyclelist.setLayoutManager(mLinearLayoutManager);
        myfriendFragmentRecyclelist.setOnTouchListener(onTouchListener);
        myfriendFragmentRecyclelist.addOnScrollListener(onScrollListener);
    }

    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //重置页码数 进行网络加载
            mPage = 1;
            if (!myFriendArrayList.isEmpty()) {
                myFriendArrayList.clear();
            }
            HttpGetMyFriendList(mPage);
        }
    };
    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (myfriendSwipRefresh.isRefreshing()) {
                return true;
            } else {
                return false;
            }

        }
    };
    //列表上拉加载事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !myfriendSwipRefresh.isRefreshing()
                    && !isLoading) {
                //TODO 加载更多 加载更多这一块有个问题得处理防止用户一直上拉导致mPage一直在++一直在请求数据  添加一个Bool值判断是否正在网络加载中
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void loadMore() {
        mPage++;
        if (myFriendAdapter != null) {
            myFriendAdapter.changLoadState(1);
        }
        HttpGetMyFriendList(mPage);
    }

    //网络加载数据
    private void HttpGetMyFriendList(final int mPage) {
        isLoading = true;//网络正在加载中
        customWaitDialog.dismiss();//TODO 图层消失 应该在加载完数据之后到时候修改
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("user_type", "2");
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.GET_MY_FRIENDS, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
                if (myFriendAdapter != null) {
                    myFriendAdapter.changLoadState(2);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //TODO 请求数据成功将数据布局在列表上 在处理数据时将正在加载 isLoading的值变换
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _MyFriend myFriend = mGson.fromJson(jsonArray.get(i).toString(), _MyFriend.class);
                            myFriendArrayList.add(myFriend);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myfriendSwipRefresh.setRefreshing(false);
                                if (myFriendAdapter == null) {
                                    myFriendAdapter = new MyFriendAdapter(mContext, getActivity(), myFriendArrayList);
                                    myfriendFragmentRecyclelist.setAdapter(myFriendAdapter);
                                    myFriendAdapter.notifyDataSetChanged();
                                } else {
                                    myFriendAdapter.notifyDataSetChanged();
                                }
                                if (myFriendAdapter != null) {
                                    myFriendAdapter.changLoadState(2);
                                }
                                isLoading = false;
                            }
                        });
                    } else if (status.equals("201")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isLoading = false;
                                if (myFriendAdapter != null) {
                                    myFriendAdapter.changLoadState(2);
                                }
                                if (myFriendArrayList.isEmpty()) {
                                    myfriendNoneFriend.setVisibility(View.VISIBLE);
                                } else {
                                    myfriendNoneFriend.setVisibility(View.GONE);
                                    Toast.makeText(mContext, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
