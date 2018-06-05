package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import com.tbs.tbsbusiness.adapter.OrderAdapter;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._OrderItem;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.util.EventBusUtil;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderSearchResultActivity extends BaseActivity {
    @BindView(R.id.search_order_back_rl)
    RelativeLayout searchOrderBackRl;
    @BindView(R.id.order_seach_null_data)
    RelativeLayout orderSeachNullData;
    @BindView(R.id.search_order_result_recycler)
    RecyclerView searchOrderResultRecycler;
    private Context mContext;
    private String TAG = "OrderSearchResultActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private Gson mGson;
    private int mPage = 1;
    private int mPageSize = 20;//分页处理
    private boolean isLoading = false;
    private ArrayList<_OrderItem> mOrderItemArrayList = new ArrayList<>();
    private OrderAdapter mOrderAdapter;
    private String mSearchInfo = "";//查询条件
    private Intent mIntent;
    private String mOrderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_search_result);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        //初始化页面数据
        mGson = new Gson();
        mIntent = getIntent();
        mSearchInfo = mIntent.getStringExtra("mSearchInfo");
        mOrderType = "1";
        //初始化列表
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        searchOrderResultRecycler.setLayoutManager(mLinearLayoutManager);
        searchOrderResultRecycler.addOnScrollListener(onScrollListener);
        searchOrderResultRecycler.setOnTouchListener(onTouchListener);
        //获取数据
        HttpGetNetData(mPage, mSearchInfo);
    }

    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (isLoading) {
                return true;
            } else {
                return false;
            }
        }
    };

    //刷新数据
    private void initNetData() {
        mPage = 1;
        if (!mOrderItemArrayList.isEmpty()) {
            mOrderItemArrayList.clear();
        }
        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTE_ORDER_FRAGMENT_UPDATE_DATA));
        HttpGetNetData(mPage, mSearchInfo);
    }

    //加载更多数据
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        HttpGetNetData(mPage, mSearchInfo);
    }

    //获取数据
    private void HttpGetNetData(int mPage, String mSearchInfo) {
        orderSeachNullData.setVisibility(View.GONE);
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", SpUtil.getCompany_id(mContext));
        param.put("key_word", mSearchInfo);
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.GET_ORDER_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接数据失败==============" + e.getMessage());
                isLoading = false;

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "数据链接成功===============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //数据链接成功
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _OrderItem orderItem = mGson.fromJson(jsonArray.get(i).toString(), _OrderItem.class);
                            mOrderItemArrayList.add(orderItem);
                        }
                        //将数据布局到页面上
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mOrderAdapter == null) {
                                    mOrderAdapter = new OrderAdapter(mContext, mOrderItemArrayList);
                                    searchOrderResultRecycler.setAdapter(mOrderAdapter);
                                    mOrderAdapter.setOnOrderItemClickLister(onOrderItemClickLister);
                                    mOrderAdapter.notifyDataSetChanged();
                                    isLoading = false;
                                } else {
                                    mOrderAdapter.notifyDataSetChanged();
                                    isLoading = false;
                                }
                            }
                        });
                    } else {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!mOrderItemArrayList.isEmpty()) {
                                    Toast.makeText(mContext, "当前没有更多数据~", Toast.LENGTH_SHORT).show();
                                    isLoading = false;
                                } else {
                                    //没有任何数据显示的占位图
                                    orderSeachNullData.setVisibility(View.VISIBLE);
                                    isLoading = false;
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }
            }
        });
    }

    //子项点击事件
    private OrderAdapter.OnOrderItemClickLister onOrderItemClickLister = new OrderAdapter.OnOrderItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            //点击事件
            switch (view.getId()) {
                case R.id.item_order_all_ll:
                    //进入详情页
                    btnClickEvent(0, position);
                    break;
                //按键处理模式根据数据类型
                case R.id.item_order_right_01:
                    //最右侧按钮处理事件
                    btnClickEvent(1, position);
                    break;
                case R.id.item_order_right_02:
                    //中间处理
                    btnClickEvent(2, position);
                    break;
                case R.id.item_order_right_03:
                    //最左侧处理
                    btnClickEvent(3, position);
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
                //在处理新订单时要对数据操作
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
                        //操作成功 移除操作项
                        runOnUiThread(new Runnable() {
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

                        //当前订单数据已经发生变化
                        runOnUiThread(new Runnable() {
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
                        runOnUiThread(new Runnable() {
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
    protected boolean isRegisterEventBus() {
        return true;
    }

    @OnClick(R.id.search_order_back_rl)
    public void onViewClicked() {
        finish();
    }
}
