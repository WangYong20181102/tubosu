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
import com.tbs.tobosupicture.adapter.ZuiReAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean._ZuiRe;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.CustomWaitDialog;

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
 * Created by Mr.Lin on 2017/7/17 15:46.
 */

public class ZuiReFragment extends BaseFragment {
    @BindView(R.id.zuire_recycle)
    RecyclerView zuireRecycle;
    Unbinder unbinder;
    @BindView(R.id.zuire_swipe)
    SwipeRefreshLayout zuireSwipe;

    private Context mContext;
    private String TAG = "ZuiReFragment";
    private LinearLayoutManager mLinearLayoutManager;
    private ZuiReAdapter mZuiReAdapter;
    private boolean isLoading = false;//是否正在加载更多数据
    private int mPage = 1;
    //人气榜
    private ArrayList<_ZuiRe.ActiveUser> activeUserArrayList = new ArrayList<>();
    //动态列表
    private ArrayList<_ZuiRe.Dynamic> dynamicArrayList = new ArrayList<>();
    //正在加载的动画
    private CustomWaitDialog customWaitDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zuire, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        customWaitDialog.show();//初次进来Loading的图案
        HttpGetZuiReList(mPage);
        return view;
    }

    private void initView() {
        zuireSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        zuireSwipe.setBackgroundColor(Color.WHITE);
        zuireSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        zuireSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        zuireRecycle.setLayoutManager(mLinearLayoutManager);
        zuireRecycle.setOnTouchListener(onTouchListener);
        zuireRecycle.addOnScrollListener(onScrollListener);//上拉加载更多

        customWaitDialog = new CustomWaitDialog(mContext);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新  数据重置
            mPage = 1;
            if (!activeUserArrayList.isEmpty()) {
                activeUserArrayList.clear();
            }
            if (!dynamicArrayList.isEmpty()) {
                dynamicArrayList.clear();
            }
            HttpGetZuiReList(mPage);
        }
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (zuireSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !zuireSwipe.isRefreshing()
                    && !isLoading) {
                loadMore();
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        HttpGetZuiReList(mPage);
    }

    //TODO 在这个请求中要区分用户的登录状态 已经登录传UID 去识别用户评论过或者点赞过的动态
    private void HttpGetZuiReList(final int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("page", mPage);
        if (Utils.userIsLogin(mContext)) {
            param.put("uid", SpUtils.getUserUid(mContext));
        }
        param.put("page_size", "10");
        param.put("type", "2");
        HttpUtils.doPost(UrlConstans.IMAGE_TO_FRIEND_ZUIRE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isLoading = false;
                Log.e(TAG, "链接失败====" + e.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "服务器链接失败！", Toast.LENGTH_SHORT).show();
                        customWaitDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e(TAG, "链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        final _ZuiRe zuiRe = gson.fromJson(data, _ZuiRe.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                zuireSwipe.setRefreshing(false);
                                if (activeUserArrayList.isEmpty()) {
                                    activeUserArrayList.addAll(zuiRe.getActive_user());
                                }
                                dynamicArrayList.addAll(zuiRe.getDynamic());
                                if (mZuiReAdapter == null) {
                                    mZuiReAdapter = new ZuiReAdapter(mContext, getActivity(), activeUserArrayList, dynamicArrayList);
                                    zuireRecycle.setAdapter(mZuiReAdapter);
                                    mZuiReAdapter.notifyDataSetChanged();
                                } else {
                                    mZuiReAdapter.notifyDataSetChanged();
                                }
                                customWaitDialog.dismiss();
                            }
                        });
                        isLoading = false;
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mZuiReAdapter != null) {
                                    mZuiReAdapter.changLoadState(2);
                                }
                                customWaitDialog.dismiss();
                            }
                        });
                        isLoading = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customWaitDialog.dismiss();
                        }
                    });
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
