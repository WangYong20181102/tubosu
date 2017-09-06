package com.tbs.tobosupicture.activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.OwenerSearchRecordAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.OwnerSearchRecordJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 * 业主案例记录
 * Created by Lie on 2017/8/16.
 */

public class OwnerCaseActivity extends BaseActivity {

    @BindView(R.id.reOwenerCaseBack)
    RelativeLayout reOwenerCaseBack;
    @BindView(R.id.ivNoOwenerCaseData)
    ImageView ivNoOwenerCaseData;
    @BindView(R.id.OwenerCaseRecyclerView)
    RecyclerView OwenerCaseRecyclerView;
    @BindView(R.id.OwenerCaseSwipeRefreshLayout)
    SwipeRefreshLayout OwenerCaseSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private OwenerSearchRecordAdapter adapter;
    private List<OwnerSearchRecordJsonEntity.OwnerSearchRecordEntity> ownerSearchRecordList = new ArrayList<OwnerSearchRecordJsonEntity.OwnerSearchRecordEntity>();
    private int page = 1;
    private int pageSize = 10;
    private boolean isFirstLoad = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = OwnerCaseActivity.this;
        TAG = OwnerCaseActivity.class.getSimpleName();
        setContentView(R.layout.activity_owner_case);
        ButterKnife.bind(this);
        initListViewSetting();
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
                HttpUtils.doPost(UrlConstans.SAME_CITY_OWENER_CASE_LIST_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(mContext, "系统繁忙，稍后再试~");
                                ivNoOwenerCaseData.setVisibility(View.VISIBLE);
                                if (adapter != null) {
                                    adapter.hideLoadMoreMessage();
                                }
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Utils.setErrorLog(TAG, json);
                        Gson gson = new Gson();
                        final OwnerSearchRecordJsonEntity entity = gson.fromJson(json, OwnerSearchRecordJsonEntity.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(entity.getStatus() == 200){
                                    ownerSearchRecordList.addAll(entity.getData());
                                    OwenerCaseSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                    ivNoOwenerCaseData.setVisibility(View.GONE);
                                    initListView();
                                }else {
                                    if(ownerSearchRecordList.size()==0){
                                        if(adapter!=null){
                                            adapter.noMoreData();
                                            adapter.hideLoadMoreMessage();
                                        }
                                        OwenerCaseSwipeRefreshLayout.setVisibility(View.GONE);
                                        ivNoOwenerCaseData.setVisibility(View.VISIBLE);

                                    }
                                }
                            }
                        });
                    }
                });
            }
        }

    }

    private void initListViewSetting() {
        // 设置下拉进度的背景颜色，默认就是白色的
        OwenerCaseSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        OwenerCaseSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        OwenerCaseSwipeRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(mContext);
        OwenerCaseRecyclerView.setLayoutManager(linearLayoutManager);
        OwenerCaseRecyclerView.setOnScrollListener(onScrollListener);
        OwenerCaseRecyclerView.setOnTouchListener(onTouchListener);
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
                    && !OwenerCaseSwipeRefreshLayout.isRefreshing()) {
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
            if (OwenerCaseSwipeRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };



    private void initListView() {
        OwenerCaseSwipeRefreshLayout.setRefreshing(false);
        if (adapter == null) {
            adapter = new OwenerSearchRecordAdapter(mContext, ownerSearchRecordList);
            OwenerCaseRecyclerView.setAdapter(adapter);
            adapter.hideLoadMoreMessage();
        } else {
            adapter.notifyDataSetChanged();
        }

        adapter.hideLoadMoreMessage();
        getData();
    }

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            ownerSearchRecordList.clear();
            page = 1;
            if (adapter != null) {
                adapter.hideLoadMoreMessage();
            }
            getDataFromNet();
        }
    };

    private void getData(){
        if(isFirstLoad){
            isFirstLoad = !isFirstLoad;
            //下拉刷新数据 重新初始化各种数据
            ownerSearchRecordList.clear();
            page = 1;
            if (adapter != null) {
                adapter.hideLoadMoreMessage();
            }
            getDataFromNet();
        }

    }

    private void loadMore() {
        page++;
        if (adapter != null) {
            adapter.showLoadMoreMessage();
        }
        OwenerCaseSwipeRefreshLayout.setRefreshing(false);
        getDataFromNet();
        if (adapter != null) {
            adapter.hideLoadMoreMessage();
        }
    }
    

    @OnClick(R.id.reOwenerCaseBack)
    public void onOwenerCaseActivityViewClicked(View view) {
        switch (view.getId()){
            case R.id.reOwenerCaseBack:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
