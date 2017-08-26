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
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.PersonHomePageAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._PersonHomePage;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
 * 点击任何有头像的地方查看用户个人主页的页面  php=preson home page
 * 进入这个页面要传的值 homepageUid主页用户的Id  is_virtual_user是否是虚拟用户
 * creat by lin
 */
public class PersonHomePageActivity extends BaseActivity {

    @BindView(R.id.php_recyclerview)
    RecyclerView phpRecyclerview;
    @BindView(R.id.php_swipe)
    SwipeRefreshLayout phpSwipe;

    private Context mContext;
    private String TAG = "PersonHomePageActivity";
    private boolean isLoading = false;//是否正在加载数据
    private LinearLayoutManager mLinearLayoutManager;
    private Intent mIntent;
    private String homepageUid;//当前主页用户的id 从上一个界面传来
    private String is_virtual_user;//是否是虚拟用户  从上一个界面传来
    private int mPage = 1;
    private PersonHomePageAdapter personHomePageAdapter;
    //添加动态的数据集
    private List<_PersonHomePage.Dynamic> dynamicList = new ArrayList<>();
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_home_page);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        HttpGetHomepageMsg();
    }


    private void initViewEvent() {
        mIntent = getIntent();
        homepageUid = mIntent.getStringExtra("homepageUid");
        is_virtual_user = mIntent.getStringExtra("is_virtual_user");

        phpSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        phpSwipe.setBackgroundColor(Color.WHITE);
        phpSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        phpSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        phpRecyclerview.setLayoutManager(mLinearLayoutManager);
        phpRecyclerview.setOnTouchListener(onTouchListener);
        phpRecyclerview.addOnScrollListener(onScrollListener);

        gson = new Gson();
    }

    //下拉刷新事件  重置数据
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initData();
        }
    };

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    //重置页面的数据
    private void initData() {
        mPage = 1;
        if (dynamicList != null) {
            dynamicList.clear();
        }
        HttpGetHomepageMsg();
    }

    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (phpSwipe.isRefreshing()) {
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
                    && !phpSwipe.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        if (personHomePageAdapter != null) {
            personHomePageAdapter.changeLoadState(1);
        }
        HttpGetMoreMsg(mPage);
    }

    //一进来加载的数据
    private void HttpGetHomepageMsg() {
        isLoading = true;
        phpSwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", homepageUid);//查看当前主页用户的uid
        //TODO 这里要做用户的登录判断 以及传递是否是真实的用户
        if (Utils.userIsLogin(mContext)) {
            param.put("login_uid", SpUtils.getUserUid(mContext));//登录的用户的id
        }
        param.put("is_virtual_user", is_virtual_user);//是否是虚拟用户
        HttpUtils.doPost(UrlConstans.HOME_PAGE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===" + json);
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    final String status = jsonObject.getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status.equals("200")) {
                                //拿到正确的数据  进行数据的填充
                                try {
                                    String data = jsonObject.getString("data");
                                    _PersonHomePage personHomePage = gson.fromJson(data, _PersonHomePage.class);
                                    dynamicList.addAll(personHomePage.getDynamic());
//                                    if (personHomePageAdapter == null) {
                                    personHomePageAdapter = new PersonHomePageAdapter(mContext, PersonHomePageActivity.this, personHomePage, dynamicList);
                                    phpRecyclerview.setAdapter(personHomePageAdapter);
                                    personHomePageAdapter.notifyDataSetChanged();
//                                    } else {
//                                        personHomePageAdapter.notifyDataSetChanged();
//                                    }
                                    isLoading = false;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    isLoading = false;
                                }
                            } else {
                                isLoading = false;
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }
            }
        });
    }

    //加载更多的数据
    private void HttpGetMoreMsg(final int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", homepageUid);
        param.put("is_virtual_user", is_virtual_user);
        if (Utils.userIsLogin(mContext)) {
            param.put("login_uid", SpUtils.getUserUid(mContext));
        }
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.RELATE_DYNAMIC, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    final String status = jsonObject.getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status.equals("200")) {
                                personHomePageAdapter.changeLoadState(2);
                                //将数据布局到列表
                                try {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        _PersonHomePage.Dynamic dynamic = gson.fromJson(jsonArray.get(i).toString(), _PersonHomePage.Dynamic.class);
                                        dynamicList.add(dynamic);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (personHomePageAdapter == null) {
                                    personHomePageAdapter = new PersonHomePageAdapter(mContext, PersonHomePageActivity.this, dynamicList);
                                    phpRecyclerview.setAdapter(personHomePageAdapter);
                                    personHomePageAdapter.notifyDataSetChanged();
                                } else {
                                    personHomePageAdapter.notifyDataSetChanged();
                                }
                            } else if (status.equals("201")) {
                                //没有更多数据
                                personHomePageAdapter.changeLoadState(2);
                                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    isLoading = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            personHomePageAdapter.changeLoadState(2);
                        }
                    });
                    isLoading = false;
                }
            }
        });
    }
    //EventBus事件处理

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.LOGIN_INITDATA:
                initData();
                break;
        }
    }
}
