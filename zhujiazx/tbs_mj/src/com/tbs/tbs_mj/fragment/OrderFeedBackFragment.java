package com.tbs.tbs_mj.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.NewOrderDetailActivity;
import com.tbs.tbs_mj.adapter.OrderFeedMsgBackAdapter;
import com.tbs.tbs_mj.base.BaseFragment;
import com.tbs.tbs_mj.bean._OrderFeedBackMsg;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2018/3/26 16:30.
 * 装修公司反馈页面
 */

public class OrderFeedBackFragment extends BaseFragment {
    @BindView(R.id.frag_order_feedback_recycler)
    RecyclerView fragOrderFeedbackRecycler;
    @BindView(R.id.frag_order_feedback_input_et)
    EditText fragOrderFeedbackInputEt;
    @BindView(R.id.frag_order_feedback_send_tv)
    TextView fragOrderFeedbackSendTv;
    @BindView(R.id.frag_order_feedback_input_rl)
    RelativeLayout fragOrderFeedbackInputRl;
    Unbinder unbinder;
    private String TAG = "OrderFeedBackFragment";
    private Context mContext;
    private String mOrderId;
    private LinearLayoutManager mLinearLayoutManager;
    private int mPage = 1;
    private int mPageSize = 20;
    private Gson mGson;
    private boolean isLoading = false;
    private NewOrderDetailActivity mNewOrderDetailActivity;
    //是否要监听键盘的弹起
    private boolean isListerSoftShowing = false;
    //正式显示的列表
    private ArrayList<_OrderFeedBackMsg> mOrderFeedBackMsgArrayList = new ArrayList<>();
    //列表适配器
    private OrderFeedMsgBackAdapter mOrderFeedMsgBackAdapter;

    public OrderFeedBackFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNewOrderDetailActivity = (NewOrderDetailActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNewOrderDetailActivity = null;
    }

    public static OrderFeedBackFragment newInstance(String mOrderId) {
        OrderFeedBackFragment orderFeedBackFragment = new OrderFeedBackFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mOrderId", mOrderId);
        orderFeedBackFragment.setArguments(bundle);
        return orderFeedBackFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mOrderId = args.getString("mOrderId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_feedback, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initViewEvent();
        return view;
    }

    //初始化页面
    private void initViewEvent() {
        mGson = new Gson();
        mNewOrderDetailActivity = new NewOrderDetailActivity();
        //设置Recycleview事务
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        fragOrderFeedbackRecycler.setLayoutManager(mLinearLayoutManager);
        fragOrderFeedbackRecycler.addOnScrollListener(onScrollListener);
        fragOrderFeedbackRecycler.setOnTouchListener(onTouchListener);
        //输入框监听
        fragOrderFeedbackInputEt.setOnFocusChangeListener(onFocusChangeListener);
        //请求数据
        HttpGerFeedBackMsg(mPage);
    }

    //输入框监听事件
    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.e(TAG, "输入框获取焦点=================");
                CheckSoftIsShowing();
            } else {
                isListerSoftShowing = false;
            }
        }
    };

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 4 >= mLinearLayoutManager.getItemCount()
                    && !isLoading) {
                //加载更多
                loadMore();
            }

        }
    };

    //判断软键盘是否开启
    private boolean isSoftShowing() {
        if (getActivity() != null) {
            int screenHeight = getActivity().getWindow().getDecorView().getHeight();
            Rect rect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            return screenHeight - rect.bottom != 0;
        } else {
            return false;
        }
    }

    //监听键盘开启的监听器
    private void CheckSoftIsShowing() {
        Log.e(TAG, "执行监听----------");
        isListerSoftShowing = true;//开始监听键盘的弹起状态
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isListerSoftShowing) {
                    try {
                        Thread.sleep(100);
                        if (isSoftShowing()) {
                            //键盘处于开启的状态
                            mNewOrderDetailActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //键盘处于开启状态
                                    fragOrderFeedbackRecycler.scrollToPosition(0);
                                }
                            });
                        } else {
                            //键盘属于收起的状态

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

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

    private void loadMore() {
        mPage++;
        HttpGerFeedBackMsg(mPage);
    }

    private void HttpGerFeedBackMsg(final int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", mOrderId);
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.FEEDBACK_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==============" + e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = jsonArray.length() - 1; i >= 0; i--) {
                            _OrderFeedBackMsg orderFeedBackMsg = mGson.fromJson(jsonArray.get(i).toString(), _OrderFeedBackMsg.class);
                            mOrderFeedBackMsgArrayList.add(orderFeedBackMsg);
                        }

                        //将数据布局到页面上
                        mNewOrderDetailActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mOrderFeedMsgBackAdapter == null) {
                                    mOrderFeedMsgBackAdapter = new OrderFeedMsgBackAdapter(mContext, mOrderFeedBackMsgArrayList);
                                    fragOrderFeedbackRecycler.setAdapter(mOrderFeedMsgBackAdapter);
                                    mOrderFeedMsgBackAdapter.notifyDataSetChanged();
                                    isLoading = false;
                                } else {
                                    mOrderFeedMsgBackAdapter.notifyDataSetChanged();
                                    isLoading = false;
                                }
                            }
                        });
                    } else {
                        if (!mOrderFeedBackMsgArrayList.isEmpty()) {
                            mNewOrderDetailActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "当前没有更多数据~", Toast.LENGTH_SHORT).show();

                                    isLoading = false;
                                }
                            });
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void sendFeedBackMsg(final String msg) {
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, "请输入反馈内容！", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> param = new HashMap<>();
            param.put("token", Util.getDateToken());
            param.put("id", mOrderId);
            param.put("content", msg);
            OKHttpUtil.post(Constant.ADD_FEEDBACK_MSG, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "链接失败===========" + e.getMessage());
                    //发送失败
                    mNewOrderDetailActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "请求超时，请检查网络是否连接！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = new String(response.body().string());
                    Log.e(TAG, "消息反馈发送成功=====================" + json);

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.optString("status");
                        if (status.equals("200")) {
                            //发送成功
                            mNewOrderDetailActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    _OrderFeedBackMsg orderFeedBackMsg = new _OrderFeedBackMsg();
                                    orderFeedBackMsg.setContent(msg);
                                    orderFeedBackMsg.setSend_type(1);
                                    if (mOrderFeedBackMsgArrayList.isEmpty()) {
                                        mOrderFeedBackMsgArrayList.add(orderFeedBackMsg);
                                    } else {
                                        mOrderFeedBackMsgArrayList.add(0, orderFeedBackMsg);
                                    }

                                    Log.e(TAG, "消息的集合长度===============" + mOrderFeedBackMsgArrayList.size());
                                    if (mOrderFeedMsgBackAdapter == null) {
                                        mOrderFeedMsgBackAdapter = new OrderFeedMsgBackAdapter(mContext, mOrderFeedBackMsgArrayList);
                                        fragOrderFeedbackRecycler.setAdapter(mOrderFeedMsgBackAdapter);
                                        mOrderFeedMsgBackAdapter.notifyDataSetChanged();
                                        if (!mOrderFeedBackMsgArrayList.isEmpty()) {
                                            fragOrderFeedbackRecycler.scrollToPosition(0);
                                        }
                                    } else {
                                        mOrderFeedMsgBackAdapter.notifyDataSetChanged();
                                        if (!mOrderFeedBackMsgArrayList.isEmpty()) {
                                            fragOrderFeedbackRecycler.scrollToPosition(0);
                                        }
                                    }
                                    fragOrderFeedbackInputEt.setText("");
                                }
                            });
                        } else if (status.equals("0")) {
                            //发送失败
                            mNewOrderDetailActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "发送失败，请检查网络是否连接！", Toast.LENGTH_SHORT).show();
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

    @OnClick({R.id.frag_order_feedback_input_et, R.id.frag_order_feedback_send_tv, R.id.frag_order_feedback_input_rl})
    public void onViewClickedOnOrderFeedBack(View view) {
        switch (view.getId()) {
//            case R.id.frag_order_feedback_input_rl:
//            case R.id.frag_order_feedback_input_et:
////                if (!mOrderFeedBackMsgArrayList.isEmpty()) {
////                    mOrderFeedMsgBackAdapter.notifyDataSetChanged();
////                    fragOrderFeedbackRecycler.scrollToPosition(mOrderFeedBackMsgArrayList.size() - 1);
////                }
//                break;
            case R.id.frag_order_feedback_send_tv:
                sendFeedBackMsg(fragOrderFeedbackInputEt.getText().toString());
                break;
        }
    }
}
