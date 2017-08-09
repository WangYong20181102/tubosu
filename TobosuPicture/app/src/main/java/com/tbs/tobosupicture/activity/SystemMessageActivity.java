package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.SystemMessageAdapter;
import com.tbs.tobosupicture.bean._SystemMessage;
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

public class SystemMessageActivity extends AppCompatActivity {

    @BindView(R.id.sysm_back)
    LinearLayout sysmBack;
    @BindView(R.id.sysm_recyclerview)
    RecyclerView sysmRecyclerview;
    @BindView(R.id.sysm_swipe)
    SwipeRefreshLayout sysmSwipe;
    @BindView(R.id.sysm_null_data)
    LinearLayout sysmNullData;

    private Context mContext;
    private String TAG = "SystemMessageActivity";
    private Gson mGson;
    private LinearLayoutManager mLinearLayoutManager;
    private int mPage = 1;
    private List<_SystemMessage> systemMessageList = new ArrayList<>();
    private SystemMessageAdapter systemMessageAdapter;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        HttpGetSystemMessageList(mPage);
    }

    private void initViewEvent() {
        mGson = new Gson();
        //显示加载浮层
        sysmSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        sysmSwipe.setBackgroundColor(Color.WHITE);
        sysmSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        sysmSwipe.setOnRefreshListener(onRefreshListener);
        //
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        sysmRecyclerview.setLayoutManager(mLinearLayoutManager);
        sysmRecyclerview.setOnTouchListener(onTouchListener);
        sysmRecyclerview.addOnScrollListener(onScrollListener);//上拉加载更多

    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //最后可见的子项
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !sysmSwipe.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        HttpGetSystemMessageList(mPage);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (sysmSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initList();
        }
    };

    private void initList() {
        mPage = 1;
        //重载数据
        if (!systemMessageList.isEmpty()) {
            systemMessageList.clear();
        }
        //请求数据
        HttpGetSystemMessageList(mPage);
    }

    @OnClick(R.id.sysm_back)
    public void onViewClickedInSystemMessageActivity() {
        finish();
    }

    private void HttpGetSystemMessageList(int page) {
        sysmSwipe.setRefreshing(false);
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("page", page);
        param.put("page_size", "10");
        if (Utils.userIsLogin(mContext)) {
            param.put("uid", SpUtils.getUserUid(mContext));
            param.put("user_type", SpUtils.getUserType(mContext));
        }
        HttpUtils.doPost(UrlConstans.SYSTEM_MSG, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接成功=====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _SystemMessage systemMessage = mGson.fromJson(jsonArray.get(i).toString(), _SystemMessage.class);
                            systemMessageList.add(systemMessage);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (systemMessageAdapter == null) {
                                    systemMessageAdapter = new SystemMessageAdapter(mContext, systemMessageList);
                                    sysmRecyclerview.setAdapter(systemMessageAdapter);
                                    systemMessageAdapter.notifyDataSetChanged();
                                } else {
                                    systemMessageAdapter.notifyDataSetChanged();
                                }
                            }
                        });

                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                                if (systemMessageList.isEmpty()) {
                                    sysmNullData.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isLoading = false;
            }
        });
    }
}
