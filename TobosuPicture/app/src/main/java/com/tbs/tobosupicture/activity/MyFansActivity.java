package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.MyFansAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._MyFans;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * create by lin
 * 我的图谜列表  显示可以加为图友以及显示是否为图友
 */
public class MyFansActivity extends BaseActivity {
    @BindView(R.id.mf_back)
    RelativeLayout mfBack;
    @BindView(R.id.mf_myfans_recyclelist)
    RecyclerView mfMyfansRecyclelist;
    @BindView(R.id.mf_swip_refresh)
    SwipeRefreshLayout mfSwipRefresh;

    private LinearLayoutManager mLinearLayoutManager;
    private Context mContext;
    private String TAG = "MyFansActivity";
    private Gson mGson;
    private ArrayList<_MyFans> myFansList = new ArrayList<>();//填充RecycleView集合
    private int mPage = 1;
    private MyFansAdapter myFansAdapter;
    private boolean isLoading = false;//是否正在上拉加载更多

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();//初始化相关控件的设置
        HttpGetMyFansList(mPage);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        initList();
    }

    private void initViewEvent() {
        mGson = new Gson();
        //显示加载浮层
        mfSwipRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        mfSwipRefresh.setBackgroundColor(Color.WHITE);
        mfSwipRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        mfSwipRefresh.setOnRefreshListener(onRefreshListener);
        //
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mfMyfansRecyclelist.setLayoutManager(mLinearLayoutManager);
        mfMyfansRecyclelist.setOnTouchListener(onTouchListener);
        mfMyfansRecyclelist.addOnScrollListener(onScrollLister);//上拉加载更多
    }

    //下拉刷新重置数据
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initList();
        }
    };

    private void initList() {
        mPage = 1;
        //重新加载数据
        if (!myFansList.isEmpty()) {
            myFansList.clear();
        }
        HttpGetMyFansList(mPage);
    }

    //上拉加载更多数据
    private RecyclerView.OnScrollListener onScrollLister = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //最后可见的子项
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !mfSwipRefresh.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
    //在加载数据时然界面失去点击效果
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mfSwipRefresh.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    //TODO 网络请求数据 当请求完成时要修改isLoading=false
    private void HttpGetMyFansList(final int mPage) {
        mfSwipRefresh.setRefreshing(false);
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("followed_user_type", "2");
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.GET_MY_FANS_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求失败===" + e.toString());
                isLoading = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (myFansAdapter != null) {
                            myFansAdapter.changLoadState(2);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "请求成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _MyFans myFans = mGson.fromJson(jsonArray.get(i).toString(), _MyFans.class);
                            myFansList.add(myFans);
                        }
                        //处理请求回来的数据 将数据布局
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (myFansAdapter == null) {
                                    myFansAdapter = new MyFansAdapter(mContext, MyFansActivity.this, myFansList);
                                    mfMyfansRecyclelist.setAdapter(myFansAdapter);
                                    myFansAdapter.notifyDataSetChanged();
                                } else {
                                    myFansAdapter.notifyDataSetChanged();
                                }
                                Log.e(TAG, "查看修改的数据==第三个是否互为好友==" + myFansList.get(2).getIs_friends());
                                if (myFansAdapter != null) {
                                    myFansAdapter.changLoadState(2);
                                }
                            }
                        });
                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有更多数据~", Toast.LENGTH_SHORT).show();
                                if (myFansAdapter != null) {
                                    myFansAdapter.changLoadState(2);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    isLoading = false;
                    e.printStackTrace();
                }
                isLoading = false;
            }
        });
    }

    //加载更多数据
    private void loadMore() {
        if (myFansAdapter != null) {
            myFansAdapter.changLoadState(1);
        }
        mPage++;
        HttpGetMyFansList(mPage);
    }

    @OnClick({R.id.mf_back})
    public void onViewClickedInMyFansActivity(View view) {
        switch (view.getId()) {
            case R.id.mf_back:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "我的图谜页销毁了。。。。");
        ButterKnife.bind(this).unbind();
    }

}
