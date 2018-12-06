package com.tbs.tobosutype.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.MessageCenterAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.MessageCenterBean;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/12/3 16:47.
 */
public class MessageCenterActivity extends BaseActivity {
    @BindView(R.id.rv_reply)
    RecyclerView recyclerView;
    @BindView(R.id.dq_swipe)
    SwipeRefreshLayout dqSwipe;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.ll_message_center)
    LinearLayout llMessageCenter;
    private LinearLayoutManager layoutManager;
    private int mPage = 1;//用于分页的数据
    private boolean isDownRefresh = false;//是否是下拉刷新
    private int mPageSize = 15;//用于分页的数据
    private List<MessageCenterBean> messageCenterBeanList;
    private MessageCenterAdapter adapter;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        gson = new Gson();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        llMessageCenter.setBackgroundColor(Color.WHITE);
        messageCenterBeanList = new ArrayList<>();

        //下拉刷新
        dqSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dqSwipe.setBackgroundColor(Color.WHITE);
        dqSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dqSwipe.setOnRefreshListener(onRefreshListener);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(onTouchListener);
        recyclerView.setOnScrollListener(onScrollListener);

        messageCenterHttpRequest();

    }

    //touch
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (dqSwipe.isRefreshing()) {
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
            if (adapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastVisibleItemPosition() + 1 == adapter.getItemCount()) {
                    LoadMore();
                }
            }
        }
    };

    //加载更多数据
    private void LoadMore() {
        mPage++;
        messageCenterHttpRequest();
    }

    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initRequest();
        }

    };

    /**
     * 下拉刷新
     */
    private void initRequest() {
        mPage = 1;
        isDownRefresh = true;
        dqSwipe.setRefreshing(true);
        if (adapter != null) {
            adapter = null;
        }
        if (!messageCenterBeanList.isEmpty()) {
            messageCenterBeanList.clear();
        }
        messageCenterHttpRequest();
    }

    /**
     * 网络请求
     */
    private void messageCenterHttpRequest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("uid", AppInfoUtil.getUserid(mContext));
        params.put("page", mPage);
        params.put("pagesize", mPageSize);
        OKHttpUtil.post(Constant.MAPP_APPWENDAPUSH_MESSAGECENTER, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isDownRefresh = false;
                        dqSwipe.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            MessageCenterBean bean = gson.fromJson(jsonArray.get(i).toString(), MessageCenterBean.class);
                            messageCenterBeanList.add(bean);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter == null) {
                                    adapter = new MessageCenterAdapter(MessageCenterActivity.this, messageCenterBeanList);
                                    adapter.setOnMessageCenterClickListener(onMessageCenterClickListener);
                                    recyclerView.setAdapter(adapter);
                                }
                                if (isDownRefresh) {
                                    isDownRefresh = false;
                                    recyclerView.scrollToPosition(0);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    adapter.notifyItemInserted(messageCenterBeanList.size() - mPageSize);
                                }
                                dqSwipe.setRefreshing(false);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(mContext, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private MessageCenterAdapter.OnMessageCenterClickListener onMessageCenterClickListener = new MessageCenterAdapter.OnMessageCenterClickListener() {
        @Override
        public void onClickPosition(int mPosition) {
            if (messageCenterBeanList.get(mPosition).getIs_see().equals("1")) {  //1代表消息未查看，进行消息查看网络请求
                isSeeHttpRequest(mPosition);
            }
            Intent intent = new Intent(mContext, AnswerItemDetailsActivity.class);
            intent.putExtra("question_id", messageCenterBeanList.get(mPosition).getQuestion_id());
            startActivity(intent);
        }
    };

    /**
     * 消息已查看网络请求
     *
     * @param position
     */
    private void isSeeHttpRequest(int position) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("id", messageCenterBeanList.get(position).getId());
        OKHttpUtil.post(Constant.MAPP_APPWENDAPUSH_ISSEE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    @OnClick({R.id.rl_back, R.id.ll_message_center})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_message_center:
                break;
        }
    }
}
