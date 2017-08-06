package com.tbs.tobosupicture.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CollectSampleImgAdapter;
import com.tbs.tobosupicture.adapter.MyDesignerAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.DesignerListJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lie on 2017/8/4.
 */

public class MyDesignerListActivity extends BaseActivity {

    @BindView(R.id.relCollectDesignerBack)
    RelativeLayout relCollectDesignerBack;
    @BindView(R.id.collectionDesignerRecyclerView)
    RecyclerView collectionDesignerRecyclerView;
    @BindView(R.id.collectDesignerSwipeRefreshLayout)
    SwipeRefreshLayout collectDesignerSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<DesignerListJsonEntity.MyDesigner> myDesignerList;

    private int page = 1;
    private int pageSize = 10;
    private MyDesignerAdapter myDesignerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyDesignerListActivity.this;
        TAG = "MyDesignerListActivity";
        setContentView(R.layout.activity_my_designer_list);
        ButterKnife.bind(this);
        initSetting();
        getData();

    }

    private void getData() {
        if (Utils.isNetAvailable(mContext)) {
            if (Utils.userIsLogin(mContext)) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", Utils.getDateToken());
                hashMap.put("uid", SpUtils.getUserUid(mContext));
                hashMap.put("page", page);
                hashMap.put("page_size", pageSize);
                HttpUtils.doPost(UrlConstans.DESIGNER_LIST_COLLECT_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(mContext, "系统繁忙，稍后再试");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Utils.setErrorLog(TAG, json);
                        Gson gson = new Gson();
                        DesignerListJsonEntity entity = gson.fromJson(json, DesignerListJsonEntity.class);
                        if (entity.getStatus() == 200) {
                            myDesignerList = entity.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initAdapter();
                                }
                            });
                        } else {

                            // 占位图 FIXME
                            final String msg = entity.getMsg();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, msg);
                                    if(myDesignerAdapter!=null){
                                        myDesignerAdapter.hideLoadMoreMessage();
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                Utils.gotoLogin(mContext);
            }
        }
    }

    private void initAdapter() {
        if (myDesignerAdapter == null) {
            myDesignerAdapter = new MyDesignerAdapter(mContext, myDesignerList);
            collectionDesignerRecyclerView.setAdapter(myDesignerAdapter);
        } else {
            myDesignerAdapter.notifyDataSetChanged();
        }

    }


    private void initSetting() {
        // 设置下拉进度的背景颜色，默认就是白色的
        collectDesignerSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        collectDesignerSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        collectDesignerSwipeRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        collectionDesignerRecyclerView.setLayoutManager(linearLayoutManager);

        collectionDesignerRecyclerView.setOnScrollListener(onScrollListener);
        collectionDesignerRecyclerView.setOnTouchListener(onTouchListener);
    }


    //显示列表的滑动监听事件 上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= linearLayoutManager.getItemCount()
                    && !collectDesignerSwipeRefreshLayout.isRefreshing()) {
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (collectDesignerSwipeRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            myDesignerList.clear();
            page = 1;
            if (myDesignerAdapter != null) {
                myDesignerAdapter.hideLoadMoreMessage();
            }
            getData();

        }
    };

    private void loadMore() {
        page++;
        if (myDesignerAdapter != null) {
            myDesignerAdapter.showLoadMoreMessage();
        }

        collectDesignerSwipeRefreshLayout.setRefreshing(false);
        getData();
        System.out.println("-----**-onScrolled load more completed------");
    }


    @OnClick(R.id.relCollectDesignerBack)
    public void onViewClickedMyDesignerListActivity(View view) {
        switch (view.getId()){
            case R.id.relCollectDesignerBack:
                finish();
                break;
        }
    }
}
