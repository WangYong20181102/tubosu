package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.DialogInterface;
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
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.adapter.OrderNoticeAdapter;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._OrderNotice;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.config.MyApplication;
import com.tbs.tbsbusiness.customview.CustomDialog;
import com.tbs.tbsbusiness.util.MD5Util;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.Util;

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
 * 消息模块
 */
public class MessageActivity extends BaseActivity {
    @BindView(R.id.order_note_back_rl)
    RelativeLayout orderNoteBackRl;
    @BindView(R.id.order_note_banner_rl)
    RelativeLayout orderNoteBannerRl;
    @BindView(R.id.order_note_zaneutongzhi)
    RelativeLayout orderNoteZaneutongzhi;
    @BindView(R.id.order_note_recycler)
    RecyclerView orderNoteRecycler;
    @BindView(R.id.order_note_swipe)
    SwipeRefreshLayout orderNoteSwipe;
    private Context mContext;
    private String TAG = "MessageActivity";
    private ArrayList<_OrderNotice> mNoticeArrayList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private OrderNoticeAdapter mOrderNoticeAdapter;
    private Gson mGson;
    private boolean isLoading = false;
    private int mPage = 1;
    private int mPageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        mContext = this;
        Log.e(TAG, "==onCreate==");
        initViewEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    private void initViewEvent() {
        mGson = new Gson();
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
        param.put("id", SpUtil.getCompany_id(mContext));
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
                    param.put("id", SpUtil.getCompany_id(mContext));
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

    private void intoNewOrderDetailActivity(int position) {
        //点击进入订单详情页
        Intent intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra("mOrderId", mNoticeArrayList.get(position).getCom_order_id());
        intent.putExtra("mShowingOrderId", mNoticeArrayList.get(position).getOrder_id());
        if (mNoticeArrayList.get(position).getNotice_type().equals("5")) {
            //消息反馈类型为客服消息
            intent.putExtra("mViewPagerPosition", "1");
        }
        startActivity(intent);
        //标记消息已读
        Util.msgIsRead(mNoticeArrayList.get(position).getId());
        if (!mNoticeArrayList.isEmpty() && mNoticeArrayList.get(position) != null) {
            mNoticeArrayList.get(position).setIs_read("1");
            mOrderNoticeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.USER_LOGIN_OUT:
                Log.e(TAG, "MessageActivity退出====");
                finish();
                break;
            case EC.EventCode.INIT_MESSAGE_IF_NULL:
                //当消息数量为空时进行数据刷新
                if (mNoticeArrayList != null && mNoticeArrayList.isEmpty()&&!isLoading) {
                    initNetDate();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "==onStart==");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "==onStop==");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "==onResume==");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "==onRestart==");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "==onPause==");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "==onDestroy==");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("你确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //退出前将数据上传
                                    finish();
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("再看看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.order_note_zaneutongzhi)
    public void onViewClicked() {
    }
}
