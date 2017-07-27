package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.ReplyAdapter;
import com.tbs.tobosupicture.bean._Reply;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
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
 * 回复页面
 * 在动态详情中的回复列表中点击回复按钮可进入此页
 */
public class ReplyActivity extends AppCompatActivity {

    @BindView(R.id.reply_back)
    LinearLayout replyBack;
    @BindView(R.id.reply_num)
    TextView replyNum;
    @BindView(R.id.reply_recyclerview)
    RecyclerView replyRecyclerview;
    @BindView(R.id.reply_swipe)
    SwipeRefreshLayout replySwipe;
    @BindView(R.id.reply_revert)
    EditText replyRevert;
    @BindView(R.id.reply_send)
    TextView replySend;

    private Context mContext;
    private String TAG = "ReplyActivity";
    private int mPage = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private ReplyAdapter mReplyAdapter;
    private List<_Reply.Comment> commentList = new ArrayList<>();//回复的列表数据
    private _Reply mReply;//回复的内容
    private Gson mGson;
    private boolean isLoading = false;//是否正在加载中
    //TODO 要进入这个界面必须传 comment_id被评论的id号 和dynamic_id 动态id号
    private String comment_id;//被评论的Id号
    private String dynamic_id;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        //初始化基本事务
        mIntent = getIntent();
        comment_id = mIntent.getStringExtra("comment_id");
        dynamic_id = mIntent.getStringExtra("dynamic_id");
        mGson = new Gson();
        //下拉控件初始化
        replySwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        replySwipe.setBackgroundColor(Color.WHITE);
        replySwipe.setSize(SwipeRefreshLayout.DEFAULT);
        replySwipe.setOnRefreshListener(onRefreshListener);
        //列表初始化
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        replyRecyclerview.setLayoutManager(mLinearLayoutManager);
        replyRecyclerview.setOnTouchListener(onTouchListener);
        replyRecyclerview.addOnScrollListener(onScrollListener);

        HttpGetReplyList(mPage);
    }

    //下拉刷新事件
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initHttpGet();
        }
    };

    //初始化请求
    private void initHttpGet() {
        //重置页码  清空数据 重新请求
        Log.e(TAG, "重新请求接口之前的数据===" + mReply.getCommented().getPraise_count());
        mPage = 1;
        mReply = null;
        if (!commentList.isEmpty()) {
            commentList.clear();
        }
        //TODO 重新请求数据
        HttpGetReplyList(mPage);
    }

    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (replySwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !replySwipe.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        //进行数据的加载
        if (mReplyAdapter != null) {
            mReplyAdapter.changeAdapState(1);
        }
        HttpGetReplyMoreList(mPage);
    }

    //TODO 网络请求数据  当前传入的是固定的uid=23109 当用户没有登录时要进行用户的登录判断
    private void HttpGetReplyList(final int mPage) {
        isLoading = true;
        replySwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("comment_id", comment_id);
        param.put("uid", "23109");
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.REPLY_DYNAMIC_COMMENT_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接数据成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        if (mReply != null) {
                            mReply = null;
                        }
                        mReply = mGson.fromJson(data, _Reply.class);
                        Log.e(TAG, "重新请求接口之后的数据===" + mReply.getCommented().getPraise_count());
                        commentList.addAll(mReply.getComment());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if (mReplyAdapter == null) {
                                mReplyAdapter = new ReplyAdapter(mContext, ReplyActivity.this, mReply, commentList, dynamic_id);
                                replyRecyclerview.setAdapter(mReplyAdapter);
                                mReplyAdapter.notifyDataSetChanged();
                                mReplyAdapter.changeAdapState(2);
//                                } else {
//                                    mReplyAdapter.notifyDataSetChanged();
//                                    mReplyAdapter.changeAdapState(2);
//                                }
                            }
                        });

                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mReplyAdapter != null) {
                                    mReplyAdapter.changeAdapState(2);
                                }
                                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    isLoading = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "抛出异常=====" + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mReplyAdapter != null) {
                                mReplyAdapter.changeAdapState(2);
                            }

                        }
                    });
                    isLoading = false;
                }

            }
        });
    }

    private void HttpGetReplyMoreList(final int mPage) {
        isLoading = true;
        replySwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("comment_id", comment_id);
        param.put("uid", "23109");
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.REPLY_DYNAMIC_COMMENT_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接数据成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        if (mReply != null) {
                            mReply = null;
                        }
                        mReply = mGson.fromJson(data, _Reply.class);
                        Log.e(TAG, "重新请求接口之后的数据===" + mReply.getCommented().getPraise_count());
                        commentList.addAll(mReply.getComment());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mReplyAdapter == null) {
                                    mReplyAdapter = new ReplyAdapter(mContext, ReplyActivity.this, mReply, commentList, dynamic_id);
                                    replyRecyclerview.setAdapter(mReplyAdapter);
                                    mReplyAdapter.notifyDataSetChanged();
                                    mReplyAdapter.changeAdapState(2);
                                } else {
                                    mReplyAdapter.notifyDataSetChanged();
                                    mReplyAdapter.changeAdapState(2);
                                }
                            }
                        });

                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mReplyAdapter != null) {
                                    mReplyAdapter.changeAdapState(2);
                                }
                                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    isLoading = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "抛出异常=====" + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mReplyAdapter != null) {
                                mReplyAdapter.changeAdapState(2);
                            }

                        }
                    });
                    isLoading = false;
                }

            }
        });
    }

    @OnClick({R.id.reply_back, R.id.reply_revert, R.id.reply_send})
    public void onViewClickedInReplyActivity(View view) {
        switch (view.getId()) {
            case R.id.reply_back:
                finish();
                break;
            case R.id.reply_revert:
                //TODO 点击输入框要判断用户是否登录
                if (false) {
                    //TODO 用户没有登录 跳转登录页面
                }
                break;
            case R.id.reply_send:
                //TODO 将写入的内容请求接口发送
                if (!TextUtils.isEmpty(replyRevert.getText().toString())) {
                    HttpReplyComment(replyRevert.getText().toString());
                } else {
                    Toast.makeText(mContext, "请输入评论的内容", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //TODO 回复评论接口 其中UID=23109
    private void HttpReplyComment(String content) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", "23109");
        param.put("dynamic_id", dynamic_id);
        param.put("comment_id", comment_id);
        param.put("commented_uid", mReply.getCommented().getUid());
        Log.e(TAG, "回复用户的 commented_uid====" + mReply.getCommented().getUid());
        param.put("is_virtual_user", mReply.getCommented().getUser_type());
        param.put("content", content);
        HttpUtils.doPost(UrlConstans.REPLY_COMMENT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "评论成功！", Toast.LENGTH_SHORT).show();
                                replyRevert.setText("");
                            }
                        });
                        //重新请求刷新数据
                        initHttpGet();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ButterKnife.bind(this).unbind();
    }
}
