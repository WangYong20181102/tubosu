package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CaseAdapter;
import com.tbs.tobosupicture.adapter.OwenerSearchRecordCaseAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.CaseJsonEntity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.HttpUtils;
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
 * 查看案例搜索记录列表 （业主）
 * Created by Lie on 2017/8/32.
 */

public class DecorateCaseSearchActivity extends BaseActivity {

    @BindView(R.id.recordBack)
    LinearLayout recordBack;
    @BindView(R.id.ivNoNewCaseData)
    ImageView ivNoNewCaseData;
    @BindView(R.id.newCaseRecyclerView)
    RecyclerView newCaseRecyclerView;
    @BindView(R.id.newCaseSwipRefreshLayout)
    SwipeRefreshLayout newCaseSwipRefreshLayout;
    @BindView(R.id.linearLayoutNewCaseData)
    LinearLayout linearLayoutNewCaseData;
    
    private int page = 1;
    private int pageSize = 10;

    private LinearLayoutManager linearLayoutManager;
    private OwenerSearchRecordCaseAdapter caseAdapter;
    private ArrayList<CaseJsonEntity.CaseEntity> caseList = new ArrayList<CaseJsonEntity.CaseEntity>();
    private CaseJsonEntity caseJsonEntity;

    private String text; // 接收的id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "DecorateCaseSearchActivity";
        mContext = DecorateCaseSearchActivity.this;
        setContentView(R.layout.decoration_case_search_record_activity);
        ButterKnife.bind(this);
        initListViewSetting();
        getDataFromNet();
    }

    private void getDataFromNet() {
        Intent it = getIntent();
        if(it!=null && it.getStringExtra("case_group_id")!=null){
            text = it.getStringExtra("case_group_id");
            Utils.setErrorLog(TAG, "接收需要传的id是" + text);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Utils.getDateToken());
            hashMap.put("id", text);
            hashMap.put("page", page);
            hashMap.put("page_size", pageSize);
            HttpUtils.doPost(UrlConstans.GET_USER_SEARCH_RECORD, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(mContext, "系统繁忙，稍后再试!");
                            Utils.setErrorLog(TAG, "服务端有问题");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Utils.setErrorLog(TAG, json);
                    caseJsonEntity = new CaseJsonEntity(json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (caseJsonEntity.getStatus() == 200) {
                                caseList.addAll(caseJsonEntity.getDataList());
                                linearLayoutNewCaseData.setVisibility(View.VISIBLE);
                                ivNoNewCaseData.setVisibility(View.GONE);
                                initListView();
                            } else {
                                if(caseAdapter!=null){
                                    caseAdapter.noMoreData();
                                    caseAdapter.hideLoadMoreMessage();
                                }
                                if(caseList.size()==0){
                                    linearLayoutNewCaseData.setVisibility(View.GONE);
                                    ivNoNewCaseData.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            EventBusUtil.sendEvent(new Event(EC.EventCode.UPDATE_OWNER_SEARCH_CASE, text));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initListViewSetting() {
        // 设置下拉进度的背景颜色，默认就是白色的
        newCaseSwipRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        newCaseSwipRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        newCaseSwipRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(mContext);
        newCaseRecyclerView.setLayoutManager(linearLayoutManager);

        newCaseRecyclerView.setOnScrollListener(onScrollListener);
        newCaseRecyclerView.setOnTouchListener(onTouchListener);
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
                    && !newCaseSwipRefreshLayout.isRefreshing()) {
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
            if (newCaseSwipRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void initListView() {
        newCaseSwipRefreshLayout.setRefreshing(false);
        if (caseAdapter == null) {
            caseAdapter = new OwenerSearchRecordCaseAdapter(mContext, caseList);
            newCaseRecyclerView.setAdapter(caseAdapter);
            caseAdapter.hideLoadMoreMessage();
        } else {
            caseAdapter.notifyDataSetChanged();
            caseAdapter.hideLoadMoreMessage();
        }

        EventBusUtil.sendEvent(new Event(EC.EventCode.UPDATE_OWNER_SEARCH_CASE_STATUS, caseList));
    }

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            caseList.clear();
            if (caseAdapter != null) {
                caseAdapter.hideLoadMoreMessage();
            }
            getDataFromNet();
        }
    };

    private void loadMore() {
        page++;
        if (caseAdapter != null) {
            caseAdapter.showLoadMoreMessage();
        }

        newCaseSwipRefreshLayout.setRefreshing(false);
        getDataFromNet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick(R.id.recordBack)
    public void onViewClickedDecorateCaseSearchActivity(View view) {
        switch (view.getId()) {
            case R.id.recordBack:
                EventBusUtil.sendEvent(new Event(EC.EventCode.UPDATE_OWNER_SEARCH_CASE, text));
                finish();
                break;
        }
    }
}
