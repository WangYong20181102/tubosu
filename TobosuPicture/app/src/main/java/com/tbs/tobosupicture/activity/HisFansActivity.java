package com.tbs.tobosupicture.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.HisFansAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._HisFans;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * create by lin
 * 他人的图谜列表
 * 用户点击进入查看他人的粉丝列表
 */
public class HisFansActivity extends BaseActivity {

    @BindView(R.id.hf_back)
    RelativeLayout hfBack;//返回
    @BindView(R.id.hf_myfans_recyclelist)
    RecyclerView hfMyfansRecyclelist;//粉丝列表
    @BindView(R.id.hf_swip_refresh)
    SwipeRefreshLayout hfSwipRefresh;//下拉刷新控件
    @BindView(R.id.hf_title)
    TextView hfTitle;
    @BindView(R.id.hf_data_null)
    LinearLayout hfDataNull;

    private LinearLayoutManager mLinearLayoutManager;
    private Context mContext;
    private String TAG = "HisFansActivity";
    private ArrayList<_HisFans> hisFansArrayList = new ArrayList<>();//布局显示用的集合
    private int mPage = 1;//加载更多用到的页码
    private CustomWaitDialog customWaitDialog;//正在加载的蒙层
    private boolean isLoading = false;//是否正在上拉加载更多数据
    private HisFansAdapter hisFansAdapter;//适配器
    private Intent mIntent;
    private String mTitle;//页面标题
    private String uId;//被查看用户的id
    private String is_virtual_user;//是否是虚拟用户
    private Gson mGson;//json解析

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_fans);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();//初始化控件
        HttpGetHisFansList(mPage);
    }


    @OnClick({R.id.hf_back})
    public void onViewClickedInHisFansActivity(View view) {
        switch (view.getId()) {
            case R.id.hf_back:
                finish();
                break;
        }
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mTitle = mIntent.getStringExtra("title");
        hfTitle.setText("" + mTitle);
        uId = mIntent.getStringExtra("uid");
        is_virtual_user = mIntent.getStringExtra("is_virtual_user");
        mGson = new Gson();
        //加载的蒙层
        customWaitDialog = new CustomWaitDialog(mContext);
        customWaitDialog.show();
        //初始化下拉控件
        hfSwipRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        hfSwipRefresh.setBackgroundColor(Color.WHITE);
        hfSwipRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        hfSwipRefresh.setOnRefreshListener(onRefreshListener);

        //初始化列表
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        hfMyfansRecyclelist.setLayoutManager(mLinearLayoutManager);
        hfMyfansRecyclelist.setOnTouchListener(onTouchListener);
        hfMyfansRecyclelist.addOnScrollListener(onScrollListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新事件 重置数据
            mPage = 1;
            if (!hisFansArrayList.isEmpty()) {
                hisFansArrayList.clear();
            }
            //重新请求
            HttpGetHisFansList(mPage);
        }
    };
    //控件触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (hfSwipRefresh.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //列表上拉事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !hfSwipRefresh.isRefreshing()
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
        if (hisFansAdapter != null) {
            hisFansAdapter.changeAdapterState(1);
        }
        HttpGetHisFansList(mPage);
    }

    //清除加载状态
    private void cleanLoadAction() {
        if (hisFansAdapter != null) {
            hisFansAdapter.changeAdapterState(2);
        }
        if (hfSwipRefresh.isRefreshing()) {
            hfSwipRefresh.setRefreshing(false);
        }
        customWaitDialog.dismiss();
    }

    //TODO 网络请求数据 当请求完成时要修改isLoading=false 现在暂时用的是固定的UId=23109
    private void HttpGetHisFansList(int page) {
        cleanLoadAction();
        isLoading = true;//正在加载数据
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", uId);
        param.put("followed_user_type", is_virtual_user);
        param.put("page", page);
        param.put("page_size", "10");
        Log.e(TAG, "参数uid==" + uId);
        Log.e(TAG, "参数是否是虚拟用户==" + is_virtual_user);
        HttpUtils.doPost(UrlConstans.GET_HIS_FANS_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求连接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "请求连接成功==" + json);
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //请求数据成功 将数据布局
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    hfDataNull.setVisibility(View.GONE);
                                    JSONArray dataArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        _HisFans hisFans = mGson.fromJson(dataArray.get(i).toString(), _HisFans.class);
                                        hisFansArrayList.add(hisFans);
                                    }
                                    if (hisFansAdapter == null) {
                                        hisFansAdapter = new HisFansAdapter(mContext, hisFansArrayList);
                                        hfMyfansRecyclelist.setAdapter(hisFansAdapter);
                                        hisFansAdapter.changeAdapterState(2);
                                        hisFansAdapter.notifyDataSetChanged();
                                    } else {
                                        hisFansAdapter.changeAdapterState(2);
                                        hisFansAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else if (status.equals("201")) {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有更多数据~", Toast.LENGTH_SHORT).show();
                                if (hisFansArrayList.isEmpty()) {
                                    //TODO 显示没有数据的图层
                                    hfDataNull.setVisibility(View.VISIBLE);
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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
