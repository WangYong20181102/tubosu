package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.DynamicMsgAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._DynamicMsg;
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
 * 我的消息列表页 关于我的/我的参与的消息提示中传来
 */
public class DynamicMsgActivity extends BaseActivity {

    @BindView(R.id.dm_back)
    LinearLayout dmBack;
    @BindView(R.id.dm_recycler)
    RecyclerView dmRecycler;
//    @BindView(R.id.dm_swipe)
//    SwipeRefreshLayout dmSwipe;

    private Context mContext;
    private String TAG = "DynamicMsgActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading = false;//是否正在加载数据
    private List<_DynamicMsg> dynamicMsgList = new ArrayList<>();
    private DynamicMsgAdapter dynamicMsgAdapter;
    private Gson mGson;
    private int mPage = 1;
    private Intent mIntent;
    private String type = "";//查看类型  1--我的发起  2--我的参与

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_msg);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        HttpGetDynamicMsgList(mPage);
    }

    private void initViewEvent() {
        mGson = new Gson();
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");

//        dmSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
//        dmSwipe.setBackgroundColor(Color.WHITE);
//        dmSwipe.setSize(SwipeRefreshLayout.DEFAULT);
//        dmSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        dmRecycler.setLayoutManager(mLinearLayoutManager);
//        dmRecycler.setOnTouchListener(onTouchListener);
//        dmRecycler.addOnScrollListener(onScrollListener);//上拉加载更多
    }

//    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//        @Override
//        public void onRefresh() {
//            mPage = 1;
//            if (!dynamicMsgList.isEmpty()) {
//                dynamicMsgList.clear();
//            }
//        }
//    };

    private void HttpGetDynamicMsgList(int mPage) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("type", type);
        param.put("page", mPage);
        param.put("page_size", "200");
        HttpUtils.doPost(UrlConstans.MY_MESSAGE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败======" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //数据获取成功
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _DynamicMsg dynamicMsg = mGson.fromJson(jsonArray.get(i).toString(), _DynamicMsg.class);
                            dynamicMsgList.add(dynamicMsg);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dynamicMsgAdapter == null) {
                                    dynamicMsgAdapter = new DynamicMsgAdapter(mContext, dynamicMsgList);
                                    dmRecycler.setAdapter(dynamicMsgAdapter);
                                    dynamicMsgAdapter.notifyDataSetChanged();
                                } else {
                                    dynamicMsgAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.dm_back)
    public void onViewClickedInDynamicMsgActivity() {
        finish();
    }
}
