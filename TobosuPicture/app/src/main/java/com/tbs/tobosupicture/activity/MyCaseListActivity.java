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
import com.tbs.tobosupicture.adapter.CollectCaseImgAdapter;
import com.tbs.tobosupicture.adapter.CollectSampleImgAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.CollectCaseJsonEntity;
import com.tbs.tobosupicture.bean.CollectionSampleJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

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
 * 案例收藏
 * Created by Lie on 2017/8/4.
 */

public class MyCaseListActivity extends BaseActivity {


    @BindView(R.id.relCollectCaseBack)
    RelativeLayout relCollectCaseBack;
    @BindView(R.id.collectionCaseRecyclerView)
    RecyclerView collectionCaseRecyclerView;
    @BindView(R.id.CollectCaseSwipeRefreshLayout)
    SwipeRefreshLayout CollectCaseSwipeRefreshLayout;
    private ArrayList<CollectCaseJsonEntity.CollectCaseEntity> casePicList;

    private int page = 1;
    private int pageSize = 10;

    private CollectCaseImgAdapter casePicAdapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyCaseListActivity.this;
        TAG = "MySampleListActivity";
        setContentView(R.layout.activity_case_list);
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
                HttpUtils.doPost(UrlConstans.CASE_COLLECT_LIST_URL, hashMap, new Callback() {
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
                        CollectCaseJsonEntity entity = gson.fromJson(json, CollectCaseJsonEntity.class);
                        if (entity.getStatus() == 200) {
                            casePicList = entity.getData();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initAdapter();
                                }
                            });
                        } else {
                            final String msg = entity.getMsg();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, msg);
                                    if(casePicAdapter!=null){
                                        casePicAdapter.hideLoadMoreMessage();
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
        if (casePicAdapter == null) {
            casePicAdapter = new CollectCaseImgAdapter(mContext, casePicList);
            collectionCaseRecyclerView.setAdapter(casePicAdapter);
        } else {
            casePicAdapter.notifyDataSetChanged();
        }
        casePicAdapter.hideLoadMoreMessage();
    }


    private void initSetting() {
        // 设置下拉进度的背景颜色，默认就是白色的
        CollectCaseSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        CollectCaseSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        CollectCaseSwipeRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        collectionCaseRecyclerView.setLayoutManager(linearLayoutManager);

        collectionCaseRecyclerView.setOnScrollListener(onScrollListener);
        collectionCaseRecyclerView.setOnTouchListener(onTouchListener);
    }


    //显示列表的滑动监听事件 上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= linearLayoutManager.getItemCount()
                    && !CollectCaseSwipeRefreshLayout.isRefreshing()) {
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
            if (CollectCaseSwipeRefreshLayout.isRefreshing()) {
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
            casePicList.clear();
            page = 1;
            if (casePicAdapter != null) {
                casePicAdapter.hideLoadMoreMessage();
            }
            getData();

        }
    };

    private void loadMore() {
        page++;
        if (casePicAdapter != null) {
            casePicAdapter.showLoadMoreMessage();
        }

        CollectCaseSwipeRefreshLayout.setRefreshing(false);
        getData();
        System.out.println("-----**-onScrolled load more completed------");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick(R.id.relCollectCaseBack)
    public void onViewClickedMyDesignerListActivity(View view) {
        switch (view.getId()) {
            case R.id.relCollectCaseBack:
                finish();
                break;
        }
    }

}
