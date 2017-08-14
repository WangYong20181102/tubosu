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
import com.tbs.tobosupicture.adapter.MyOrginAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._DynamicBase;
import com.tbs.tobosupicture.bean._ReceiveMsg;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.EventBusUtil;
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
 * Created by Mr.Lin on 2017/8/3 11:04.
 * 我的发起  作用于以图会友关于我的模块
 */

public class MyOrginFragment extends BaseFragment {
    @BindView(R.id.my_orgin_recyclerview)
    RecyclerView myOrginRecyclerview;
    @BindView(R.id.my_orgin_swipe)
    SwipeRefreshLayout myOrginSwipe;
    Unbinder unbinder;
    @BindView(R.id.my_orgin_orgin_btn)
    TextView myOrginOrginBtn;
    @BindView(R.id.my_orgin_null_data)
    LinearLayout myOrginNullData;
    private Context mContext;
    private String TAG = "MyOrginFragment";
    private MyOrginAdapter myOrginAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading = false;//是否加载更多
    private int mPage = 1;
    private ArrayList<_DynamicBase> dynamicBaseArrayList = new ArrayList<>();
    private _ReceiveMsg receiveMsg;
    private Gson mGson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orgin, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        Log.e(TAG, "当前用户的uid=====" + SpUtils.getUserUid(mContext));
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

        myOrginSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        myOrginSwipe.setBackgroundColor(Color.WHITE);
        myOrginSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        myOrginSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        myOrginRecyclerview.setLayoutManager(mLinearLayoutManager);
        myOrginRecyclerview.setOnTouchListener(onTouchListener);
        myOrginRecyclerview.addOnScrollListener(onScrollListener);//上拉加载更多
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
            if (myOrginSwipe.isRefreshing()) {
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
                    && !myOrginSwipe.isRefreshing()
                    && !isLoading) {
                loadMore();
            }
        }
    };

    private void loadMore() {
        mPage++;
        if (myOrginAdapter != null) {
            myOrginAdapter.changeLoadState(1);
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
        myOrginSwipe.setRefreshing(true);
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
                        Log.e(TAG, "当前用户的“我的发起”消息数量======" + receiveMsg.getMy_sponsor().getMsg_count());
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
                                myOrginSwipe.setRefreshing(false);
                                Toast.makeText(mContext, "暂无更多数据~", Toast.LENGTH_SHORT).show();
                                if (dynamicBaseArrayList.isEmpty()) {
                                    myOrginNullData.setVisibility(View.VISIBLE);
                                } else {
                                    myOrginNullData.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else if (status.equals("0")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myOrginSwipe.setRefreshing(false);
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
        HttpUtils.doPost(UrlConstans.MY_SPONSOR, param, new Callback() {
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
                                if (myOrginAdapter == null || myOrginSwipe.isRefreshing()) {
                                    Log.e(TAG, "数据填充====adapter为null时我发起的数量===" + receiveMsg.getMy_sponsor().getMsg_count());
                                    myOrginAdapter = new MyOrginAdapter(mContext, getActivity(), receiveMsg.getMy_sponsor(), dynamicBaseArrayList);
                                    myOrginRecyclerview.setAdapter(myOrginAdapter);
                                    myOrginAdapter.notifyDataSetChanged();
                                } else {
                                    Log.e(TAG, "数据填充====adapter已经实例化时我发起的数量===" + receiveMsg.getMy_sponsor().getMsg_count());
                                    myOrginAdapter.notifyDataSetChanged();
                                }
                                if (myOrginAdapter != null) {
                                    myOrginAdapter.changeLoadState(2);
                                }
                                myOrginSwipe.setRefreshing(false);
                                myOrginNullData.setVisibility(View.GONE);
                            }
                        });
                    } else if (status.equals("201")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myOrginSwipe.setRefreshing(false);
                                if (myOrginAdapter != null) {
                                    myOrginAdapter.changeLoadState(2);
                                    Toast.makeText(mContext, "暂无更多数据~", Toast.LENGTH_SHORT).show();
                                }
                                if (dynamicBaseArrayList.isEmpty()) {
                                    myOrginNullData.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myOrginSwipe.setRefreshing(false);
                                if (myOrginAdapter != null) {
                                    myOrginAdapter.changeLoadState(2);
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
            case EC.EventCode.REFRESH_MY_ORGIN_NUM:
                //收到刷新通知
                initGetMsg();
                break;
        }
    }

    @OnClick(R.id.my_orgin_orgin_btn)
    public void onViewClickedInMyOrginFragment() {
        //点击发起动态
        Log.e(TAG, "点击了发起动态按钮======");
        EventBusUtil.sendEvent(new Event(EC.EventCode.GOTO_SEND_DYNAMIC));
    }
}
