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

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.ZuiXinAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean._ZuiXin;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

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
 * Created by Mr.Lin on 2017/7/17 15:50.
 */

public class ZuiXinFragment extends BaseFragment {
    @BindView(R.id.zuixin_recycle)
    RecyclerView zuixinRecycle;
    @BindView(R.id.zuixin_swipe)
    SwipeRefreshLayout zuixinSwipe;
    Unbinder unbinder;

    private Context mContext;
    private String TAG = "ZuiReFragment";
    private LinearLayoutManager mLinearLayoutManager;
    private ZuiXinAdapter mZuiXinAdapter;
    private boolean isLoading = false;//是否正在加载更多数据
    private int mPage = 1;
    private Gson gson;
    //人气榜
    private ArrayList<_ZuiXin.ActiveUser> activeUserArrayList = new ArrayList<>();
    //动态列表
    private ArrayList<_ZuiXin.Dynamic> dynamicArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zuixin, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        HttpGetZuiXinList(mPage);
        return view;
    }

    private void initView() {
        gson = new Gson();

        zuixinSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        zuixinSwipe.setBackgroundColor(Color.WHITE);
        zuixinSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        zuixinSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        zuixinRecycle.setLayoutManager(mLinearLayoutManager);
        zuixinRecycle.setOnTouchListener(onTouchListener);
        zuixinRecycle.addOnScrollListener(onScrollListener);//上拉加载更多
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
            HttpGetZuiXinList(mPage);
        }
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (zuixinSwipe.isRefreshing()) {
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
                    && !zuixinSwipe.isRefreshing()
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
        HttpGetZuiXinList(mPage);
    }

    //TODO 在这个请求中要区分用户的登录状态 已经登录传UID 去识别用户评论过或者点赞过的动态
    private void HttpGetZuiXinList(final int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        if (Utils.userIsLogin(mContext)) {
            param.put("uid", SpUtils.getUserUid(mContext));
        }
        param.put("type", "2");
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.SOCIAL_NEW_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isLoading = false;
                Log.e(TAG, "链接失败====" + e.toString());
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
                        final _ZuiXin zuiXin = gson.fromJson(data, _ZuiXin.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                zuixinSwipe.setRefreshing(false);
                                if (activeUserArrayList.isEmpty()) {
                                    activeUserArrayList.addAll(zuiXin.getActive_user());
                                }
                                dynamicArrayList.addAll(zuiXin.getDynamic());
                                if (mZuiXinAdapter == null) {
                                    mZuiXinAdapter = new ZuiXinAdapter(mContext, getActivity(), activeUserArrayList, dynamicArrayList);
                                    zuixinRecycle.setAdapter(mZuiXinAdapter);
                                    mZuiXinAdapter.notifyDataSetChanged();
                                } else {
                                    mZuiXinAdapter.notifyDataSetChanged();
                                }

                            }
                        });
                        isLoading = false;
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mZuiXinAdapter != null) {
                                    mZuiXinAdapter.changLoadState(2);
                                }
                            }
                        });
                        isLoading = false;
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
