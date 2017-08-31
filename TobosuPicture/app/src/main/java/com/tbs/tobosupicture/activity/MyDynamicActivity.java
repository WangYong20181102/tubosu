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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.MyDynamicAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._MyDynamic;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 我的动态页面
 */
public class MyDynamicActivity extends BaseActivity {

    @BindView(R.id.my_dynamic_back)
    LinearLayout myDynamicBack;
    @BindView(R.id.my_dynamic_recycler)
    RecyclerView myDynamicRecycler;
    @BindView(R.id.my_dynamic_swipe)
    SwipeRefreshLayout myDynamicSwipe;
    @BindView(R.id.my_dynamic_null)
    LinearLayout myDynamicNull;


    private Context mContext;
    private String TAG = "MyDynamicActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading = false;
    private int mPage = 1;//页数
    private Gson mGson;
    private MyDynamicAdapter myDynamicAdapter;
    private List<_MyDynamic> myDynamicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        HttpGetMyDynamicList(mPage);
    }

    @OnClick(R.id.my_dynamic_back)
    public void onViewClickedInMyDynamicActivity() {
        finish();
    }

    private void initViewEvent() {
        mGson = new Gson();
        //初始化下拉控件
        myDynamicSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        myDynamicSwipe.setBackgroundColor(Color.WHITE);
        myDynamicSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        myDynamicSwipe.setOnRefreshListener(onRefreshListener);

        //初始化列表
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        myDynamicRecycler.setLayoutManager(mLinearLayoutManager);
        myDynamicRecycler.setOnTouchListener(onTouchListener);
        myDynamicRecycler.addOnScrollListener(onScrollListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新 重置数据
            mPage = 1;
            if (!myDynamicList.isEmpty()) {
                myDynamicList.clear();
            }
            //网络请求
            HttpGetMyDynamicList(mPage);
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (myDynamicSwipe.isRefreshing()) {
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
                    && !myDynamicSwipe.isRefreshing()
                    && !isLoading) {
                loadMore();
            }
        }
    };

    private void loadMore() {
        //加载更多
        mPage++;
        //网络请求
        HttpGetMyDynamicList(mPage);
    }

    private void HttpGetMyDynamicList(int mPage) {
        isLoading = true;
        myDynamicSwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.MY_DYNAMIC, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _MyDynamic myDynamic = mGson.fromJson(jsonArray.get(i).toString(), _MyDynamic.class);
                            myDynamicList.add(myDynamic);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (myDynamicAdapter == null) {
                                    myDynamicAdapter = new MyDynamicAdapter(mContext,MyDynamicActivity.this, myDynamicList);
                                    myDynamicRecycler.setAdapter(myDynamicAdapter);
                                    myDynamicAdapter.notifyDataSetChanged();
                                    myDynamicAdapter.changeLoadState(2);
                                } else {
                                    myDynamicAdapter.notifyDataSetChanged();
                                    myDynamicAdapter.changeLoadState(2);
                                }
                            }
                        });
                    } else if (status.equals("201")) {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //清除加载状态  弹窗提示
                                if (myDynamicAdapter != null) {
                                    myDynamicAdapter.changeLoadState(2);
                                }
                                Toast.makeText(mContext, "没有更多数据~", Toast.LENGTH_SHORT).show();
                                if (myDynamicList.isEmpty()) {
                                    //TODO 显示没有数据的图层
                                    myDynamicNull.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                    isLoading = false;
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }
}
