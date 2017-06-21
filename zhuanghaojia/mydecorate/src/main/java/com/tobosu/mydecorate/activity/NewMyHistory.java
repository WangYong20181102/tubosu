package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.TempChildAdapter;
import com.tobosu.mydecorate.entity.BibleEntity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewMyHistory extends AppCompatActivity {
    private Context mContext;
    private String TAG = "NewMyHistory";
    private int mPage = 1;

    //控件
    private ImageView myHistoryBack;//返回键
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新控件
    private LinearLayout ll_data_empty;
    private RecyclerView myHistoryRecycle;//显示列表
    private LinearLayoutManager linearLayoutManager;
    private TempChildAdapter tempChildAdapter;//列表适配器
    private ArrayList<BibleEntity> bibleEntityList = new ArrayList<>();//数据表
    private ArrayList<BibleEntity> tempBibleEntityList = new ArrayList<>();//临时数据表用于加载更多承载体
    private int dataState = 1;//数据的状态  0--没有更多数据 1--正常加载数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_my_history);
        mContext = this;
        bindView();
        initView();
        initViewEvent();
        HttpGetMyHistory(mPage);
    }

    private void bindView() {
        myHistoryBack = (ImageView) findViewById(R.id.my_history_back);
        myHistoryRecycle = (RecyclerView) findViewById(R.id.my_history_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.my_history_swipe);
        ll_data_empty = (LinearLayout) findViewById(R.id.ll_data_empty);
    }

    private void initView() {
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        myHistoryRecycle.setLayoutManager(linearLayoutManager);
    }

    private void initViewEvent() {
        myHistoryBack.setOnClickListener(occl);
        myHistoryRecycle.setOnScrollListener(onScrollListener);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新重置
            mPage = 1;
            dataState = 1;
            bibleEntityList.clear();
            HttpGetMyHistory(mPage);
        }
    };
    //列表滑动监听事件  上拉加载更多数据
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 4 >= linearLayoutManager.getItemCount()
                    && !swipeRefreshLayout.isRefreshing() && dataState == 1) {
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
    //控件点击事件
    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.my_history_back:
                    finish();
                    break;
            }
        }
    };

    //加载更多
    private void loadMore() {
        mPage++;
        tempChildAdapter.showFootView();
        HttpGetMyHistory(mPage);
    }

    //网络请求数据
    private void HttpGetMyHistory(int page) {
        swipeRefreshLayout.setRefreshing(false);
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("page", page);
        param.put("uid", Util.getUserId(mContext));
        param.put("page_size", 10);
        okHttpUtil.post(Constant.MY_HISTORY_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "请求成功=====" + json);
                parseJson(json);
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {

                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() == 0 && mPage == 1) {
                    ll_data_empty.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < dataArray.length(); i++) {
                    tempBibleEntityList.add(new BibleEntity(dataArray.get(i).toString()));
                }
                bibleEntityList.addAll(tempBibleEntityList);
                tempBibleEntityList.clear();
                if (tempChildAdapter == null) {
                    tempChildAdapter = new TempChildAdapter(mContext, bibleEntityList);
                    tempChildAdapter.setOnItemClickListener(onRecyclerViewItemClickListener);
                    myHistoryRecycle.setAdapter(tempChildAdapter);
                } else {
                    tempChildAdapter.notifyDataSetChanged();
                }
                tempChildAdapter.hideFootView();
            } else if (status.equals("201")) {
                Toast.makeText(mContext, "没有更多数据！", Toast.LENGTH_SHORT).show();
                if (tempChildAdapter != null) {
                    tempChildAdapter.hideFootView();
                }
            } else {
                Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
                tempChildAdapter.hideFootView();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private TempChildAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = new TempChildAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerViewItemClick(View view, BibleEntity data) {
            int position = myHistoryRecycle.getChildPosition(view);
            //跳转至文章详情页
            Intent intent = new Intent(mContext, NewArticleDetailActivity.class);
            intent.putExtra("id", bibleEntityList.get(position).getAid());
            intent.putExtra("author_id", bibleEntityList.get(position).getAuthor_id());
            startActivity(intent);
        }
    };
}
