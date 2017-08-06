package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.DynamicDetailAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._DynamicDetail;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * create by lin
 * 动态详情页
 * 用户在以图会友页面中 的最热 最新 的列表中点击回复可以进入到该页面
 */
public class DynamicDetailActivity extends BaseActivity {

    @BindView(R.id.dynd_back)
    RelativeLayout dyndBack;
    @BindView(R.id.dynd_recycle)
    RecyclerView dyndRecycle;
    @BindView(R.id.dynd_revert)
    EditText dyndRevert;
    @BindView(R.id.dynd_send)
    TextView dyndSend;
    @BindView(R.id.dynd_swipe)
    SwipeRefreshLayout dyndSwipe;

    private Context mContext;
    private String TAG = "DynamicDetailActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private int mPage = 1;
    private Intent mIntent;
    //动态id
    private String dynamicId;
    //被评论的用户Id号
    private String commentedUid;
    //被评论的用户是否是虚拟用户 1--是 2--否
    private String is_virtual_user;
    //回复列表
    private ArrayList<_DynamicDetail.Comment> commentArrayList = new ArrayList<>();
    private ArrayList<_DynamicDetail.Comment> tempCommentArrayList = new ArrayList<>();
    //是否正在加载数据
    private boolean isLoading = false;
    //列表适配器
    private DynamicDetailAdapter dynamicDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        ButterKnife.bind(this);
        mContext = this;
        mIntent = getIntent();
        initViewEvent();
    }

    private void initViewEvent() {
        dynamicId = mIntent.getStringExtra("dynamic_id");
        commentedUid = mIntent.getStringExtra("commented_uid");
        is_virtual_user = mIntent.getStringExtra("is_virtual_user");

        dyndSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dyndSwipe.setBackgroundColor(Color.WHITE);
        dyndSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dyndSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        dyndRecycle.setLayoutManager(mLinearLayoutManager);
        dyndRecycle.setOnTouchListener(onTouchListener);
        dyndRecycle.addOnScrollListener(onScrollListener);

        HttpGetDynamic();
    }

    //上拉刷新
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !dyndSwipe.isRefreshing()
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
    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (dyndSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            reFresh();
        }
    };

    //下拉刷新数据
    private void reFresh() {
        mPage = 1;
        if (!commentArrayList.isEmpty()) {
            commentArrayList.clear();
        }
        HttpGetDynamic();
    }

    //加载更多数据
    private void loadMore() {
        mPage++;
        adapterLoadState();
        //加载更多数据调取另一个接口
        HttpGetComment(mPage);
    }

    @OnClick({R.id.dynd_back, R.id.dynd_send, R.id.dynd_revert})
    public void onViewClickedInDynamicDetailActivity(View view) {
        switch (view.getId()) {
            case R.id.dynd_back:
                finish();
                break;
            case R.id.dynd_send:
                //发送评论
                if (Utils.userIsLogin(mContext)) {
                    sendComment();
                } else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.dynd_revert:
                //点击了回复
                if (!Utils.userIsLogin(mContext)) {
                    Log.e(TAG, "当前的用户id为空");
                    Utils.gotoLogin(mContext);
                }
                break;
        }
    }

    //发送评论
    private void sendComment() {
        String comment = dyndRevert.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(mContext, "当前您没有输入评论内容~", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpSendComment(comment);
    }

    //发送评论动态
    private void HttpSendComment(String comment) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("user_type", "2");
        param.put("dynamic_id", dynamicId);
        param.put("commented_uid", commentedUid);
        param.put("is_virtual_user", is_virtual_user);
        param.put("content", comment);
        HttpUtils.doPost(UrlConstans.USER_SEND_COMMENT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "评论失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //评论成功  强刷数据
                                Toast.makeText(mContext, "评论成功！", Toast.LENGTH_SHORT).show();
                                dyndRevert.setText("");
                                reFresh();
                            }
                        });
                    } else {
                        Toast.makeText(mContext, "评论失败！", Toast.LENGTH_SHORT).show();
                        dyndRevert.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //查看详情页根据需求暂时先传固定的uid用于点击进入详情页判断当前登录的用户是否点过赞或者评论过
    private void HttpGetDynamic() {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("dynamic_id", dynamicId);
        if (Utils.userIsLogin(mContext)) {
            param.put("uid", SpUtils.getUserUid(mContext));
        }

        HttpUtils.doPost(UrlConstans.DYNAMIC_DETAIL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        final _DynamicDetail dynamicDetail = gson.fromJson(data, _DynamicDetail.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dyndSwipe.setRefreshing(false);
                                commentArrayList.addAll(dynamicDetail.getComment());
                                dynamicDetailAdapter = new DynamicDetailAdapter(mContext, DynamicDetailActivity.this, dynamicDetail, commentArrayList);
                                dyndRecycle.setAdapter(dynamicDetailAdapter);
                                dynamicDetailAdapter.notifyDataSetChanged();
                                if (dynamicDetailAdapter != null) {
                                    dynamicDetailAdapter.changeAdapterState(2);
                                }
                            }
                        });
                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dynamicDetailAdapter != null) {
                                    dynamicDetailAdapter.changeAdapterState(2);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }
                isLoading = false;
            }
        });
    }

    //获取更多的评论内容
    private void HttpGetComment(int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("dynamic_id", dynamicId);
        param.put("page", mPage);
        param.put("page_size", 10);
        HttpUtils.doPost(UrlConstans.DYNAMIC_COMMETN_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功
                        Gson gson = new Gson();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonArray.get(i).toString();
                            _DynamicDetail.Comment comment = gson.fromJson(jsonArray.get(i).toString(), _DynamicDetail.Comment.class);
                            tempCommentArrayList.add(comment);
                        }
                        commentArrayList.addAll(tempCommentArrayList);
                        tempCommentArrayList.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dynamicDetailAdapter == null) {
                                    dynamicDetailAdapter = new DynamicDetailAdapter(mContext, DynamicDetailActivity.this, commentArrayList);
                                    dyndRecycle.setAdapter(dynamicDetailAdapter);
                                    dynamicDetailAdapter.notifyDataSetChanged();
                                } else {
                                    dynamicDetailAdapter.notifyDataSetChanged();
                                }
                                adapterRevert();
                            }
                        });
                    } else if (status.equals("201")) {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!commentArrayList.isEmpty()) {
                                    Toast.makeText(mContext, "没有更多评论~", Toast.LENGTH_SHORT).show();
                                }
                                adapterRevert();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //获取数据失败
                                Toast.makeText(mContext, "亲~数据获取出了问题，估计是服务器挂了！Waiting...", Toast.LENGTH_SHORT).show();
                                adapterRevert();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }
                isLoading = false;
            }
        });
    }

    //适配器恢复正常状态
    private void adapterRevert() {
        if (dynamicDetailAdapter != null) {
            dynamicDetailAdapter.changeAdapterState(2);
        }
    }

    //适配器处于加载状态
    private void adapterLoadState() {
        if (dynamicDetailAdapter != null) {
            dynamicDetailAdapter.changeAdapterState(1);
        }
    }
}
