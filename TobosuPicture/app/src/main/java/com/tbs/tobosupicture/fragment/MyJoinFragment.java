package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.MyJoinAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._DynamicBase;
import com.tbs.tobosupicture.bean._ReceiveMsg;
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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/8/3 11:10.
 * 我参与的fragment 作用于我的模块
 */

public class MyJoinFragment extends BaseFragment {
    @BindView(R.id.my_join_join_btn)
    TextView myJoinJoinBtn;
    @BindView(R.id.my_join_null_data)
    LinearLayout myJoinNullData;
    @BindView(R.id.my_join_recyclerview)
    RecyclerView myJoinRecyclerview;
    @BindView(R.id.my_join_swipe)
    SwipeRefreshLayout myJoinSwipe;
    Unbinder unbinder;
    private Context mContext;
    private String TAG = "MyJoinFragment";
    private MyJoinAdapter myJoinAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading = false;//是否加载更多
    private int mPage = 1;
    private ArrayList<_DynamicBase> dynamicBaseArrayList = new ArrayList<>();
    private _ReceiveMsg receiveMsg;
    private Gson mGson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_join, null);
        mContext = getActivity();
        unbinder = ButterKnife.bind(this, view);
        initViewEvent();
        HttpGetMsg();
        return view;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initViewEvent() {
        mGson = new Gson();

        myJoinSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        myJoinSwipe.setBackgroundColor(Color.WHITE);
        myJoinSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        myJoinSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        myJoinRecyclerview.setLayoutManager(mLinearLayoutManager);
        myJoinRecyclerview.setOnTouchListener(onTouchListener);
        myJoinRecyclerview.addOnScrollListener(onScrollListener);//上拉加载更多
    }

    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initGetMsg();
        }
    };

    //初始化获取数据
    private void initGetMsg() {
        mPage = 1;
        if (!dynamicBaseArrayList.isEmpty()) {
            dynamicBaseArrayList.clear();
        }
        if (receiveMsg != null) {
//            receiveMsg.getMy_sponsor().setMsg_count("0");
        }
        HttpGetMsg();
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (myJoinSwipe.isRefreshing()) {
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
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !myJoinSwipe.isRefreshing()
                    && !isLoading) {
                loadMore();
            }
        }
    };

    private void loadMore() {
        mPage++;
        if (myJoinAdapter != null) {
            myJoinAdapter.changeLoadState(1);
        }
        HttpGetDynamicList(mPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //链式请求
    private void HttpGetMsg() {
        myJoinSwipe.setRefreshing(true);
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("is_icon", "1");
        HttpUtils.doPost(UrlConstans.RECEIVE_INFORMATION, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取成功
                        String data = jsonObject.getString("data");
                        receiveMsg = mGson.fromJson(data, _ReceiveMsg.class);
                        Log.e(TAG, "当前用户的“我参与的”消息数量======" + receiveMsg.getMy_participation().getMsg_count());
                        //将数据导入开始下一个请求
                        HttpGetDynamicList(1);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    } else if (status.equals("201")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myJoinSwipe.setRefreshing(false);
                                Toast.makeText(mContext, "暂无更多数据~", Toast.LENGTH_SHORT).show();
                                if (dynamicBaseArrayList.isEmpty()) {
                                    myJoinNullData.setVisibility(View.VISIBLE);
                                } else {
                                    myJoinNullData.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else if (status.equals("0")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myJoinSwipe.setRefreshing(false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //请求列表数据
    private void HttpGetDynamicList(int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.MY_PARTICIPATION, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _DynamicBase dynamicBase = mGson.fromJson(jsonArray.get(i).toString(), _DynamicBase.class);
                            dynamicBaseArrayList.add(dynamicBase);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //将数据填充
                                if (myJoinAdapter == null || myJoinSwipe.isRefreshing()) {
                                    Log.e(TAG, "数据填充====adapter为null时我发起的数量===" + receiveMsg.getMy_sponsor().getMsg_count());
                                    myJoinAdapter = new MyJoinAdapter(mContext, getActivity(), receiveMsg.getMy_participation(), dynamicBaseArrayList);
                                    myJoinRecyclerview.setAdapter(myJoinAdapter);
                                    myJoinAdapter.notifyDataSetChanged();
                                } else {
                                    Log.e(TAG, "数据填充====adapter已经实例化时我发起的数量===" + receiveMsg.getMy_sponsor().getMsg_count());
                                    myJoinAdapter.notifyDataSetChanged();
                                }
                                if (myJoinAdapter != null) {
                                    myJoinAdapter.changeLoadState(2);
                                }
                                myJoinSwipe.setRefreshing(false);
                                myJoinNullData.setVisibility(View.GONE);
                            }
                        });
                    } else if (status.equals("201")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myJoinSwipe.setRefreshing(false);
                                if (myJoinAdapter != null) {
                                    myJoinAdapter.changeLoadState(2);
                                    Toast.makeText(mContext, "暂无更多数据~", Toast.LENGTH_SHORT).show();
                                }
                                if (dynamicBaseArrayList.isEmpty()) {
                                    myJoinNullData.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myJoinSwipe.setRefreshing(false);
                                if (myJoinAdapter != null) {
                                    myJoinAdapter.changeLoadState(2);
                                }
                            }
                        });
                    }
                    isLoading = false;
                } catch (JSONException e) {
                    isLoading = false;
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.REFRESH_MY_JOIN_NUM:
                //收到刷新通知
                initGetMsg();
                break;
        }
    }

    @OnClick(R.id.my_join_join_btn)
    public void onViewClickedInMyJoinFragment() {
        //点击去最新 去浏览最新消息
    }
}
