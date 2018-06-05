package com.tbs.tbsbusiness.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.activity.OrderActivity;
import com.tbs.tbsbusiness.activity.OrderDetailActivity;
import com.tbs.tbsbusiness.adapter.OrderAdapter;
import com.tbs.tbsbusiness.base.BaseFragment;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._OrderItem;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.config.MyApplication;
import com.tbs.tbsbusiness.util.EventBusUtil;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2018/6/5 14:49.
 */
public class OrderFragment extends BaseFragment {
    @BindView(R.id.frag_null_order_rl)
    RelativeLayout fragNullOrderRl;
    @BindView(R.id.frag_order_recycler)
    RecyclerView fragOrderRecycler;
    @BindView(R.id.frag_order_swip_layout)
    SwipeRefreshLayout fragOrderSwipLayout;
    Unbinder unbinder;
    private Context mContext;
    private String TAG = "OrderFragment";
    private String mOrderType;
    private Gson mGson;
    private int mPage = 1;//用于分页
    private int mPageSize = 20;//分页数据
    private LinearLayoutManager mLinearLayoutManager;//布局管理
    private boolean isLoading = false;//是否正在加载数据
    private ArrayList<_OrderItem> mOrderItemArrayList = new ArrayList<>();
    private OrderAdapter mOrderAdapter;
    private AppCompatActivity mAllOrderActivity;

    public OrderFragment() {

    }

    public static OrderFragment newInstance(String mOrderType) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderType", mOrderType);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.NOTE_ORDER_FRAGMENT_UPDATE_DATA:
                if (!isLoading) {
                    Log.e(TAG, "重新加载数据===============");
                    initNetData();
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAllOrderActivity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAllOrderActivity = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mOrderType = args.getString("orderType");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        iniViewEvent();
        return view;
    }

    private void iniViewEvent() {
        mGson = new Gson();
        mAllOrderActivity = new OrderActivity();
        //初始化下拉刷新控件
        fragOrderSwipLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        fragOrderSwipLayout.setBackgroundColor(Color.WHITE);
        fragOrderSwipLayout.setSize(SwipeRefreshLayout.DEFAULT);
        fragOrderSwipLayout.setOnRefreshListener(onRefreshListener);
        //设置RecyclerView的相关事务
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        fragOrderRecycler.setLayoutManager(mLinearLayoutManager);
        fragOrderRecycler.addOnScrollListener(onScrollListener);
        fragOrderRecycler.setOnTouchListener(onTouchListener);
        //请求数据
        HttpGetOrderList(mPage);
    }

    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (!isLoading) {
                initNetData();
            } else {
                fragOrderSwipLayout.setRefreshing(false);
            }
        }
    };

    //touch事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (fragOrderSwipLayout.isRefreshing() || isLoading) {
                return true;
            } else {
                return false;
            }
        }
    };


    //列表滑动监听
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 1 >= mLinearLayoutManager.getItemCount()
                    && fragOrderSwipLayout != null
                    && !fragOrderSwipLayout.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        HttpGetOrderList(mPage);
    }

    //初始化网络加载数据
    private void initNetData() {
        mPage = 1;
        if (!mOrderItemArrayList.isEmpty()) {
            mOrderItemArrayList.clear();
        }
        fragNullOrderRl.setVisibility(View.GONE);
        HttpGetOrderList(mPage);
    }

    //获取网络数据
    private void HttpGetOrderList(int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", SpUtil.getCompany_id(mContext));
        param.put("state", mOrderType);
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.GET_ORDER_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===========" + e.getMessage());
                mAllOrderActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragOrderSwipLayout.setRefreshing(false);
                        isLoading = false;
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功，获取json=========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //得到正确的数据
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _OrderItem orderItem = mGson.fromJson(jsonArray.get(i).toString(), _OrderItem.class);
                            mOrderItemArrayList.add(orderItem);
                        }
                        if (mAllOrderActivity == null) {
                            return;
                        }
                        //将数据布局到页面上
                        mAllOrderActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mOrderAdapter == null) {
                                    mOrderAdapter = new OrderAdapter(mContext, mOrderItemArrayList);
                                    fragOrderRecycler.setAdapter(mOrderAdapter);
                                    mOrderAdapter.setOnOrderItemClickLister(onOrderItemClickLister);
                                    mOrderAdapter.notifyDataSetChanged();
                                    fragOrderSwipLayout.setRefreshing(false);
                                    isLoading = false;
                                } else {
                                    mOrderAdapter.notifyDataSetChanged();
                                    fragOrderSwipLayout.setRefreshing(false);
                                    isLoading = false;
                                }
                            }
                        });
                    } else {
                        if (mAllOrderActivity == null) {
                            return;
                        }
                        //没有更多的数据
                        mAllOrderActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!mOrderItemArrayList.isEmpty()) {
                                    Toast.makeText(mContext, "当前没有更多数据~", Toast.LENGTH_SHORT).show();
                                    fragOrderSwipLayout.setRefreshing(false);
                                    isLoading = false;
                                } else {
                                    //没有任何数据
                                    fragNullOrderRl.setVisibility(View.VISIBLE);
                                    fragOrderSwipLayout.setRefreshing(false);
                                    isLoading = false;
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                    fragOrderSwipLayout.setRefreshing(false);
                }
            }
        });
    }

    private OrderAdapter.OnOrderItemClickLister onOrderItemClickLister = new OrderAdapter.OnOrderItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            switch (view.getId()) {
                case R.id.item_order_all_ll:
                    if (MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                        //已经验证密码订单成功
                        btnClickEvent(0, position);
                    } else {
                        //未验密码
                        showChadanPassWordPop(0, position);
                    }

                    break;
                //按键处理模式根据数据类型
                case R.id.item_order_right_01:
                    if (MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                        //已经验证了密码
                        btnClickEvent(1, position);
                    } else {
                        //未验证密码
                        showChadanPassWordPop(1, position);
                    }
                    break;
                case R.id.item_order_right_02:
                    if (MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                        btnClickEvent(2, position);
                    } else {
                        showChadanPassWordPop(2, position);
                    }
                    break;
                case R.id.item_order_right_03:
                    if (MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                        btnClickEvent(3, position);
                    } else {
                        showChadanPassWordPop(3, position);
                    }
                    break;
            }
        }
    };

    /**
     * @param kind     处理事件类型
     * @param position item所在的位置
     */
    private void btnClickEvent(int kind, int position) {
        switch (kind) {
            case 0:
                //进入详情页
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("mOrderId", mOrderItemArrayList.get(position).getId());
                intent.putExtra("mShowingOrderId", mOrderItemArrayList.get(position).getOrder_id());//显示的订单id
                intent.putExtra("mOrderType", mOrderType);
                startActivity(intent);
                break;
            case 1:
                //最右侧按钮处理事件
                if (mOrderItemArrayList.get(position).getState().equals("1")) {
                    //todo 新订单处理模式 点击进入查看
//                        Toast.makeText(mContext, "查看", Toast.LENGTH_SHORT).show();
                    //跳转页面
                    Intent intentToOrderDetail = new Intent(mContext, OrderDetailActivity.class);
                    intentToOrderDetail.putExtra("mOrderId", mOrderItemArrayList.get(position).getId());//操作数据的id
                    intentToOrderDetail.putExtra("mShowingOrderId", mOrderItemArrayList.get(position).getOrder_id());//显示的订单id
                    intentToOrderDetail.putExtra("mOrderType", mOrderType);
                    startActivity(intentToOrderDetail);
                    //通知刷新 并改变订单的状态
                    HttpChangeOrderState(mOrderItemArrayList.get(position).getId(), mOrderItemArrayList.get(position).getState(), 1, position, "");
                } else if (mOrderItemArrayList.get(position).getState().equals("2")) {
                    //未量房处理模式
//                        Toast.makeText(mContext, "确认量房", Toast.LENGTH_SHORT).show();
//                        showQuedingLiangfang(mOrderItemArrayList.get(position).getId(), position, mOrderItemArrayList.get(position).getState());
                    showReasonPop(mOrderItemArrayList.get(position).getId(), position, 3, mOrderItemArrayList.get(position).getState());
                } else if (mOrderItemArrayList.get(position).getState().equals("3")) {
                    //已量房处理模式
//                        Toast.makeText(mContext, "确认签单", Toast.LENGTH_SHORT).show();
//                        showQuedingqiandan(mOrderItemArrayList.get(position).getId(), position, mOrderItemArrayList.get(position).getState());
                    showReasonPop(mOrderItemArrayList.get(position).getId(), position, 4, mOrderItemArrayList.get(position).getState());
                } else if (mOrderItemArrayList.get(position).getState().equals("4")) {
                    //已签单处理模式
//                        Toast.makeText(mContext, "联系业主", Toast.LENGTH_SHORT).show();
                    showCallPhonePopWindow(mOrderItemArrayList.get(position).getCellphone());
                } else if (mOrderItemArrayList.get(position).getState().equals("5")) {
                    //  跳转到反馈界面
//                        Toast.makeText(mContext, "反馈", Toast.LENGTH_SHORT).show();
                    Intent intentToFeedBack = new Intent(mContext, OrderDetailActivity.class);
                    intentToFeedBack.putExtra("mOrderId", mOrderItemArrayList.get(position).getId());
                    intentToFeedBack.putExtra("mShowingOrderId", mOrderItemArrayList.get(position).getOrder_id());//显示的订单id
                    intentToFeedBack.putExtra("mOrderType", mOrderType);
                    intentToFeedBack.putExtra("mViewPagerPosition", "1");
                    mContext.startActivity(intentToFeedBack);
                }
                break;
            case 2:
                //中间处理
                if (mOrderItemArrayList.get(position).getState().equals("1")) {
                    //新订单处理模式  没有这个按钮
                } else if (mOrderItemArrayList.get(position).getState().equals("2")) {
                    //未量房处理模式
//                        Toast.makeText(mContext, "量房失败", Toast.LENGTH_SHORT).show();
//                        showLiangfangshibai(mOrderItemArrayList.get(position).getId(), position, mOrderItemArrayList.get(position).getState());
                    showReasonPop(mOrderItemArrayList.get(position).getId(), position, 2, mOrderItemArrayList.get(position).getState());
                } else if (mOrderItemArrayList.get(position).getState().equals("3")) {
                    //已量房处理模式
                    showReasonPop(mOrderItemArrayList.get(position).getId(), position, 6, mOrderItemArrayList.get(position).getState());
                } else if (mOrderItemArrayList.get(position).getState().equals("4")) {
                    //已签单处理模式 没有这个按钮
                } else if (mOrderItemArrayList.get(position).getState().equals("5")) {
                    //未签单处理模式  没有这个按钮
                }
                break;
            case 3:
                //最左侧处理
                if (mOrderItemArrayList.get(position).getState().equals("1")) {
                    //新订单处理模式 没有这个按钮
                } else if (mOrderItemArrayList.get(position).getState().equals("2")) {
                    //未量房处理模式
//                        Toast.makeText(mContext, "联系业主", Toast.LENGTH_SHORT).show();
                    showCallPhonePopWindow(mOrderItemArrayList.get(position).getCellphone());
                } else if (mOrderItemArrayList.get(position).getState().equals("3")) {
                    //已量房处理模式
//                        Toast.makeText(mContext, "联系业主", Toast.LENGTH_SHORT).show();
                    showCallPhonePopWindow(mOrderItemArrayList.get(position).getCellphone());
                } else if (mOrderItemArrayList.get(position).getState().equals("4")) {
                    //未签单处理模式 没有这个按钮
                } else if (mOrderItemArrayList.get(position).getState().equals("5")) {
                    //已签单处理模式 没有这个按钮
                }
                break;
        }
    }

    //处理类型
    private void showChadanPassWordPop(final int kind, final int position) {
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
                            mAllOrderActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "链接服务器失败~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(final Call call, Response response) throws IOException {
                            String json = new String(response.body().string());
                            Log.e(TAG, "验证订单密码链接成功=============" + json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String status = jsonObject.optString("status");
                                final String msg = jsonObject.optString("msg");
                                if (status.equals("200")) {
                                    //验证密码成功 跳转查单页
                                    mAllOrderActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD = true;
                                            //订单验证成功
                                            switch (kind) {
                                                case 0:
                                                    btnClickEvent(0, position);
                                                    break;
                                                case 1:
                                                    btnClickEvent(1, position);
                                                    break;
                                                case 2:
                                                    btnClickEvent(2, position);
                                                    break;
                                                case 3:
                                                    btnClickEvent(3, position);
                                                    break;
                                            }
                                            popupWindow.dismiss();
                                        }
                                    });

                                } else {
                                    mAllOrderActivity.runOnUiThread(new Runnable() {
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


    //拨打业主电话
    private void showCallPhonePopWindow(final String phoneNum) {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        TextView phone_num = popview.findViewById(R.id.phone_num);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        LinearLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //设置电话号码
        phone_num.setText("" + phoneNum);
        //打电话
        open_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        pop_phone_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }


    // TODO: 2018/5/4  显示输入原因的弹框
    private void showReasonPop(final String id, final int position, final int oprate_type, final String order_state) {
        View popview = View.inflate(mContext, R.layout.pop_order_reason, null);
        //关闭按钮
        RelativeLayout order_reason_pop_close_rl = popview.findViewById(R.id.order_reason_pop_close_rl);
        //确定提交按钮
        TextView order_reason_pop_ok_btn = (TextView) popview.findViewById(R.id.order_reason_pop_ok_btn);
        //整个黑框
        RelativeLayout order_reason_pop_rl = (RelativeLayout) popview.findViewById(R.id.order_reason_pop_rl);
        //整个输入原因的框
        LinearLayout order_reason_pop_ll = popview.findViewById(R.id.order_reason_pop_ll);
        //输入框
        final EditText order_reason_pop_input_et = popview.findViewById(R.id.order_reason_pop_input_et);

        order_reason_pop_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //确定
        order_reason_pop_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(order_reason_pop_input_et.getText().toString())) {
                    HttpChangeOrderState(id, order_state, oprate_type, position, order_reason_pop_input_et.getText().toString());
                    popupWindow.dismiss();
                } else {
                    Toast.makeText(mContext, "请输入反馈信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消
        order_reason_pop_close_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        order_reason_pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);
    }

    /**
     * 改变订单的状态
     * 操作类型operate_type：1：查看；2：量房失败；3：确定量房；4：未签单；5：已签单
     * 当前订单的状态 order_state
     */
    private void HttpChangeOrderState(String id, final String order_state, final int operate_type, final int position, String content) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", id);
        param.put("state", order_state);//订单状态
        param.put("operate_type", operate_type);
        param.put("content", content);
        OKHttpUtil.post(Constant.CHANGE_ORDER_STATE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "连接失败=========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "连接成功=============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        if (mAllOrderActivity == null) {
                            return;
                        }
                        //操作成功 移除操作项
                        mAllOrderActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (operate_type != 1) {
                                    Toast.makeText(mContext, "订单状态已修改", Toast.LENGTH_SHORT).show();
                                }
                                if (!mOrderType.equals("0")) {
                                    //除了在全部页面，其他页面做了数据操作都得移除数据
                                    Log.e(TAG, "更改了订单的状态此时的列表长度==============" + mOrderItemArrayList.size());
                                    mOrderItemArrayList.remove(position);
                                    mOrderAdapter.notifyItemRemoved(position);
                                    mOrderAdapter.notifyItemRangeChanged(position, mOrderItemArrayList.size());
                                } else {
                                    //在全部页面处理了直接刷新数据
                                    if (!isLoading) {
                                        initNetData();
                                    }
                                }
                            }
                        });

                    } else if (status.equals("205")) {
                        if (mAllOrderActivity == null) {
                            return;
                        }
                        //当前订单数据已经发生变化
                        mAllOrderActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!order_state.equals("1")) {
                                    showOrderChangePopWindow(msg);
                                } else {
                                    initNetData();
                                }
                            }
                        });
                    } else {
                        if (mAllOrderActivity == null) {
                            return;
                        }
                        mAllOrderActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "操作失败~", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //订单发生变化弹窗
    private void showOrderChangePopWindow(String msg) {
        View popview = View.inflate(mContext, R.layout.pop_order_change, null);
        //确定按钮
        TextView queding_order_change = (TextView) popview.findViewById(R.id.queding_order_change);
        //显示提示
        TextView order_change_msg_tv = popview.findViewById(R.id.order_change_msg_tv);
        //整个面板
        RelativeLayout order_change_pop_rl = (RelativeLayout) popview.findViewById(R.id.order_change_pop_rl);
        LinearLayout order_change_ll = popview.findViewById(R.id.order_change_ll);
        order_change_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //设置电话号码
        order_change_msg_tv.setText("" + msg);
        //确定按钮
        queding_order_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新数据
                initNetData();
                popupWindow.dismiss();
            }
        });
        order_change_pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
