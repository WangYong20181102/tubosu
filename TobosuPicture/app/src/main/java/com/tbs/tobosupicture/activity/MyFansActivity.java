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

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.MyFansAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._MyFans;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.CustomWaitDialog;

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
    private ArrayList<_MyFans> myFansList = new ArrayList<>();//填充RecycleView集合
    private ArrayList<_MyFans> tempMyFansList = new ArrayList<>();//装箱集合
    private int mPage = 1;
    private MyFansAdapter myFansAdapter;
    private CustomWaitDialog customWaitDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();//初始化相关控件的设置
    }

    @Override
    protected void onStart() {
        super.onStart();
        HttpGetMyFansList(mPage);
    }

    private void initViewEvent() {
        //显示加载浮层
        customWaitDialog = new CustomWaitDialog(mContext);
        customWaitDialog.show();

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
            mPage = 1;
        }
    };
    //上拉加载更多数据
    private RecyclerView.OnScrollListener onScrollLister = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //最后可见的子项
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !mfSwipRefresh.isRefreshing()) {
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

    //网络请求数据
    private void HttpGetMyFansList(int mPage) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("page", mPage);
        param.put("token", Utils.getDateToken());
        HttpUtils.doPost(UrlConstans.GET_MY_FANS_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "请求成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //处理请求回来的数据 将数据布局
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //加载更多数据
    private void loadMore() {
        mPage++;
        HttpGetMyFansList(mPage);
    }

    //清除各种加载状态
    private void cleanLoadAction() {
        if (mfSwipRefresh.isRefreshing()) {
            mfSwipRefresh.setRefreshing(false);
        }
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
        ButterKnife.bind(this).unbind();
    }
}
