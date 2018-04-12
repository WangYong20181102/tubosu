package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.OrderNoticeAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._OrderNotice;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.MD5Util;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;

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
 * 装修公司 通知列表页 3.7.2新增
 * 展示装修公司新的通知
 */
public class OrderNoteActivity extends BaseActivity {
    @BindView(R.id.order_note_back_rl)
    RelativeLayout orderNoteBackRl;
    @BindView(R.id.order_note_banner_rl)
    RelativeLayout orderNoteBannerRl;
    @BindView(R.id.order_note_recycler)
    RecyclerView orderNoteRecycler;
    @BindView(R.id.order_note_swipe)
    SwipeRefreshLayout orderNoteSwipe;
    @BindView(R.id.order_note_zaneutongzhi)
    RelativeLayout orderNoteZaneutongzhi;
    private Context mContext;
    private String TAG = "OrderNoteActivity";
    private ArrayList<_OrderNotice> mNoticeArrayList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private OrderNoticeAdapter mOrderNoticeAdapter;
    private Gson mGson;
    private boolean isLoading = false;
    private int mPage = 1;
    private int mPageSize = 20;
    private Intent mIntent;
    private String back_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_note);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "通知列表执行的方法=============更新数据");
        initNetDate();
    }


    private void initViewEvent() {
        mGson = new Gson();
        mIntent = getIntent();
        back_key = mIntent.getStringExtra("back_key");
        //初始化下拉刷新控件
        orderNoteSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        orderNoteSwipe.setBackgroundColor(Color.WHITE);
        orderNoteSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        orderNoteSwipe.setOnRefreshListener(onRefreshListener);
        //设置RecyclerView的相关事务
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        orderNoteRecycler.setLayoutManager(mLinearLayoutManager);
        orderNoteRecycler.addOnScrollListener(onScrollListener);
        orderNoteRecycler.setOnTouchListener(onTouchListener);
        //加载数据
        HttpGetOrderNoticeList(mPage);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 1 >= mLinearLayoutManager.getItemCount()
                    && !orderNoteSwipe.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    private void loadMore() {
        mPage++;
        HttpGetOrderNoticeList(mPage);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (orderNoteSwipe.isRefreshing() || isLoading) {
                return true;
            } else {
                return false;
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (!isLoading) {
                initNetDate();
            } else {
                orderNoteSwipe.setRefreshing(false);
            }
        }
    };

    //重置数据
    private void initNetDate() {
        mPage = 1;
        if (!mNoticeArrayList.isEmpty()) {
            mNoticeArrayList.clear();
        }
        orderNoteZaneutongzhi.setVisibility(View.GONE);
        HttpGetOrderNoticeList(mPage);
    }

    //加载数据
    private void HttpGetOrderNoticeList(int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", AppInfoUtil.getId(mContext));
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        OKHttpUtil.post(Constant.GET_ORDER_NOTICE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==========" + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderNoteSwipe.setRefreshing(false);
                        isLoading = false;
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "获取数据成功================" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //数据获取正确
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _OrderNotice orderNotice = mGson.fromJson(jsonArray.get(i).toString(), _OrderNotice.class);
                            mNoticeArrayList.add(orderNotice);
                        }
                        //将数据布局
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mOrderNoticeAdapter == null) {
                                    mOrderNoticeAdapter = new OrderNoticeAdapter(mContext, mNoticeArrayList);
                                    orderNoteRecycler.setAdapter(mOrderNoticeAdapter);
                                    mOrderNoticeAdapter.setOnOrderNoticeClickLister(onOrderNoticeClickLister);
                                    mOrderNoticeAdapter.notifyDataSetChanged();
                                    orderNoteSwipe.setRefreshing(false);
                                    isLoading = false;
                                } else {
                                    mOrderNoticeAdapter.notifyDataSetChanged();
                                    orderNoteSwipe.setRefreshing(false);
                                    isLoading = false;
                                }
                            }
                        });
                    } else {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!mNoticeArrayList.isEmpty()) {
                                    Toast.makeText(mContext, "当前没有更多数据~", Toast.LENGTH_SHORT).show();
                                    orderNoteSwipe.setRefreshing(false);
                                    isLoading = false;
                                } else {
                                    //没有任何数据显示占位图
                                    orderNoteZaneutongzhi.setVisibility(View.VISIBLE);
                                    orderNoteSwipe.setRefreshing(false);
                                    isLoading = false;
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                    orderNoteSwipe.setRefreshing(false);
                }

            }
        });
    }

    //列表子项点击事件
    private OrderNoticeAdapter.OnOrderNoticeClickLister onOrderNoticeClickLister = new OrderNoticeAdapter.OnOrderNoticeClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            if (!MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                showChadanPassWordPop(position);
            } else {
                intoNewOrderDetailActivity(position);
            }

        }
    };

    private void intoNewOrderDetailActivity(int position) {
        //点击进入订单详情页
        Intent intent = new Intent(mContext, NewOrderDetailActivity.class);
        intent.putExtra("mOrderId", mNoticeArrayList.get(position).getCom_order_id());
        startActivity(intent);
        //标记消息已读
        HttpReadSmsPush(mNoticeArrayList.get(position).getId(), position);
    }

    //标记消息已读
    private void HttpReadSmsPush(String id, final int position) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", id);
        OKHttpUtil.post(Constant.READ_SMS_PUSH, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=============" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
//                    Log.e(TAG, "已读返回数据==================" + json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //改变现有的数据集合
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!mNoticeArrayList.isEmpty()) {
                                    mNoticeArrayList.get(position).setIs_read("1");
                                    mOrderNoticeAdapter.notifyDataSetChanged();
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


    private void showChadanPassWordPop(final int position) {
        /**
         * 用户输入了密码之后 在App没有kill 是可以直接打开订单查询的
         */
        View popview = View.inflate(mContext, R.layout.pop_check_pass_word, null);
        TextView pop_check_pw_quxiao = (TextView) popview.findViewById(R.id.pop_check_pw_quxiao);
        TextView pop_check_pw_ok = (TextView) popview.findViewById(R.id.pop_check_pw_ok);
        final EditText pop_check_pw_edit = popview.findViewById(R.id.pop_check_pw_edit);
        //全局蒙层
        RelativeLayout phone_pop_window_rl = (RelativeLayout) popview.findViewById(R.id.phone_pop_window_rl);
        //白色显示层
        RelativeLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //点击确认按钮
        pop_check_pw_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pop_check_pw_edit.getText().toString())) {
                    Toast.makeText(mContext, "请输入查单密码~", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> param = new HashMap<>();
                    param.put("token", Util.getDateToken());
                    param.put("id", AppInfoUtil.getUuid(mContext));
                    param.put("password", MD5Util.md5(pop_check_pw_edit.getText().toString()));
                    OKHttpUtil.post(Constant.CHECK_ORDER_PWD, param, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "链接服务器失败~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = new String(response.body().string());
                            Log.e(TAG, "验证订单密码链接成功=============" + json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String status = jsonObject.optString("status");
                                final String msg = jsonObject.optString("msg");
                                if (status.equals("200")) {
                                    //验证密码成功 跳转查单页
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD = true;
                                            intoNewOrderDetailActivity(position);
                                            popupWindow.dismiss();
                                        }
                                    });

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        //取消
        pop_check_pw_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        phone_pop_window_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }


    @OnClick(R.id.order_note_back_rl)
    public void onViewClickedOnOrderNoteActivity() {
        if (back_key != null && back_key.equals("1")) {
            //返回 启动App
            startActivity(new Intent(mContext, WelcomeActivity.class));
            finish();
        } else {
            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && back_key != null && back_key.equals("1")) {
            //启动App
            startActivity(new Intent(mContext, WelcomeActivity.class));
            finish();
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
