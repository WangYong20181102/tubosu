package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.HisFriendsAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._HisFriends;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.CustomWaitDialog;

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
 * 他人或者是我的的图友页面
 * 从个人主页点击进入
 */
public class HisFriendsActivity extends BaseActivity {

    @BindView(R.id.friends_back)
    RelativeLayout friendsBack;
    @BindView(R.id.friends_data_null)
    LinearLayout friendsDataNull;
    @BindView(R.id.friends_recyclelist)
    RecyclerView friendsRecyclelist;
    @BindView(R.id.friends_swip_refresh)
    SwipeRefreshLayout friendsSwipRefresh;

    private LinearLayoutManager mLinearLayoutManager;
    private Context mContext;
    private String TAG = "HisFriendsActivity";
    private ArrayList<_HisFriends> hisFansArrayList = new ArrayList<>();//布局显示用的集合
    private int mPage = 1;//加载更多用到的页码
    private CustomWaitDialog customWaitDialog;//正在加载的蒙层
    private boolean isLoading = false;//是否正在上拉加载更多数据
    private HisFriendsAdapter hisFriendsAdapter;//适配器
    private Intent mIntent;
    private String uId;//被查看用户的id
    private String is_virtual_user;//是否是虚拟用户
    private Gson mGson;//json解析

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_friends);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        HttpGetHisFriendsList(mPage);
    }

    @OnClick(R.id.friends_back)
    public void onViewClickedInHisFiiendsActivity() {
        finish();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        uId = mIntent.getStringExtra("uid");
        is_virtual_user = mIntent.getStringExtra("user_type");
        mGson = new Gson();
        //加载的蒙层
        customWaitDialog = new CustomWaitDialog(mContext);
        customWaitDialog.show();
        //初始化下拉控件
        friendsSwipRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        friendsSwipRefresh.setBackgroundColor(Color.WHITE);
        friendsSwipRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        friendsSwipRefresh.setOnRefreshListener(onRefreshListener);

        //初始化列表
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        friendsRecyclelist.setLayoutManager(mLinearLayoutManager);
        friendsRecyclelist.setOnTouchListener(onTouchListener);
        friendsRecyclelist.addOnScrollListener(onScrollListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新事件 重置数据
            mPage = 1;
            if (!hisFansArrayList.isEmpty()) {
                hisFansArrayList.clear();
            }
            //重新请求
            HttpGetHisFriendsList(mPage);
        }
    };
    //控件触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (friendsSwipRefresh.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //列表上拉事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !friendsSwipRefresh.isRefreshing()
                    && !isLoading) {
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        if (hisFriendsAdapter != null) {
            hisFriendsAdapter.changeAdapterState(1);
        }
        HttpGetHisFriendsList(mPage);
    }

    //清除加载状态
    private void cleanLoadAction() {
        if (hisFriendsAdapter != null) {
            hisFriendsAdapter.changeAdapterState(2);
        }
        if (friendsSwipRefresh.isRefreshing()) {
            friendsSwipRefresh.setRefreshing(false);
        }
        customWaitDialog.dismiss();
    }


    //TODO 网络请求数据 当请求完成时要修改isLoading=false 现在暂时用的是固定的UId=23109
    private void HttpGetHisFriendsList(int page) {
        cleanLoadAction();
        isLoading = true;//正在加载数据
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", uId);
        param.put("user_type", is_virtual_user);
        param.put("page", page);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.GET_FRIENDS_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求连接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "请求连接成功==" + json);
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //请求数据成功 将数据布局
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    friendsDataNull.setVisibility(View.GONE);
                                    JSONArray dataArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        _HisFriends hisFriends = mGson.fromJson(dataArray.get(i).toString(), _HisFriends.class);
                                        hisFansArrayList.add(hisFriends);
                                    }
                                    if (hisFriendsAdapter == null) {
                                        hisFriendsAdapter = new HisFriendsAdapter(mContext, hisFansArrayList);
                                        friendsRecyclelist.setAdapter(hisFriendsAdapter);
                                        hisFriendsAdapter.changeAdapterState(2);
                                        hisFriendsAdapter.notifyDataSetChanged();
                                    } else {
                                        hisFriendsAdapter.changeAdapterState(2);
                                        hisFriendsAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else if (status.equals("201")) {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有更多数据~", Toast.LENGTH_SHORT).show();
                                if (hisFansArrayList.isEmpty()) {
                                    //TODO 显示没有数据的图层
                                    friendsDataNull.setVisibility(View.VISIBLE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
