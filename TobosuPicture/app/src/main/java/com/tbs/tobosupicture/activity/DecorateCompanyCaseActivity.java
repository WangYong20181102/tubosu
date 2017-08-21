package com.tbs.tobosupicture.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CompanySearchRecordAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.CompanySearchRecordJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lie on 2017/8/16.
 */

public class DecorateCompanyCaseActivity extends BaseActivity {

    @BindView(R.id.reDecorateCaseBack)
    RelativeLayout reDecorateCaseBack;
    @BindView(R.id.ivNoDecorateCaseData)
    ImageView ivNoDecorateCaseData;
    @BindView(R.id.DecorateCaseRecyclerView)
    RecyclerView DecorateCaseRecyclerView;
    @BindView(R.id.DecorateCaseSwipeRefreshLayout)
    SwipeRefreshLayout DecorateCaseSwipeRefreshLayout;

    private CompanySearchRecordAdapter recordAdapter;

    private int page = 1;
    private int pageSize = 10;

    private List<CompanySearchRecordJsonEntity.CompanySearchRecordEntity> companyDataList = new ArrayList<CompanySearchRecordJsonEntity.CompanySearchRecordEntity>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = DecorateCompanyCaseActivity.this;
        TAG = DecorateCompanyCaseActivity.class.getSimpleName();
        setContentView(R.layout.activity_decorate_company_case);
        ButterKnife.bind(this);
        initSetting();
        getDataFromNet();
    }

    private void getDataFromNet() {

        if(Utils.isNetAvailable(mContext)){
            if(Utils.userIsLogin(mContext)){
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", Utils.getDateToken());
                hashMap.put("uid", SpUtils.getUserUid(mContext));
                hashMap.put("page", page);
                hashMap.put("page_size", pageSize);
                HttpUtils.doPost(UrlConstans.SAME_CITY_DECORATE_CASE_LIST_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(mContext, "系统繁忙，稍后再试~");
                                ivNoDecorateCaseData.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Gson gson = new Gson();
                        CompanySearchRecordJsonEntity entity = gson.fromJson(json, CompanySearchRecordJsonEntity.class);
                        if(entity.getStatus() == 200){
                            companyDataList.addAll(entity.getData());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initComanyCaseAdapter();
                                }
                            });
                        }else {

                        }

                    }
                });
            }else{
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        }

    }

    private void initComanyCaseAdapter(){
        if(recordAdapter == null){
            recordAdapter = new CompanySearchRecordAdapter(mContext, companyDataList);
            DecorateCaseRecyclerView.setAdapter(recordAdapter);
        }else {
            recordAdapter.notifyDataSetChanged();
        }


    }

    @OnClick(R.id.reDecorateCaseBack)
    public void onDecorateCompanyCaseActivityViewClicked(View view) {
        switch (view.getId()){
            case R.id.reDecorateCaseBack:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    private LinearLayoutManager linearLayoutManager;
    private void initSetting(){
// 设置下拉进度的背景颜色，默认就是白色的
        DecorateCaseSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        DecorateCaseSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        DecorateCaseSwipeRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DecorateCaseRecyclerView.setLayoutManager(linearLayoutManager);

        DecorateCaseRecyclerView.addOnScrollListener(onScrollListener);
        DecorateCaseRecyclerView.setOnTouchListener(onTouchListener);
    }

    //显示列表的滑动监听事件 上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
//            Log.e(TAG, "最后可见目标===" + lastVisiableItem + "集合总数===" + mLinearLayoutManager.getItemCount() + "==newState==" + newState + "==刷新状态==" + swipeRefreshLayout.isRefreshing());
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= linearLayoutManager.getItemCount()
                    && !DecorateCaseSwipeRefreshLayout.isRefreshing()) {
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
            if (DecorateCaseSwipeRefreshLayout.isRefreshing()) {
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
            if(companyDataList!=null){
                companyDataList.clear();
            }

            page = 1;
            if(recordAdapter!=null){
                recordAdapter.hideLoadMoreMessage();
            }

            getDataFromNet();

        }
    };

    private void loadMore(){
        page++;
        if(recordAdapter!=null){
            recordAdapter.showLoadMoreMessage();
        }

        DecorateCaseSwipeRefreshLayout.setRefreshing(false);
        getDataFromNet();
        System.out.println("-----**-onScrolled load more completed------");
    }
}
