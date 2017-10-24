package com.tbs.tobosutype.activity;

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

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorationCaseAdapter;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.Util;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 土拨鼠3.4版本 新增
 * 案例列表页 creat by lin
 */
public class DecorationCaseActivity extends BaseActivity {
    @BindView(R.id.deco_case_back)
    LinearLayout decoCaseBack;
    @BindView(R.id.deco_case_recycler)
    RecyclerView decoCaseRecycler;
    @BindView(R.id.deco_case_swipe)
    SwipeRefreshLayout decoCaseSwipe;
    @BindView(R.id.deco_case_find_price)
    TextView decoCaseFindPrice;
    @BindView(R.id.deco_case_find_price_rl)
    RelativeLayout decoCaseFindPriceRl;

    private Context mContext;
    private String TAG = "DecorationCaseActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private Intent mIntent;
    private Gson mGson;
    private boolean isLoading = false;//是否正在加载数据
    private int mPage = 1;//加载更多数据的页数
    private DecorationCaseAdapter mDecorationCaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration_case);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvet();
    }

    private void initViewEvet() {
        //实例化相关工具
        mGson = new Gson();
        //初始化下拉刷新控件
        decoCaseSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        decoCaseSwipe.setBackgroundColor(Color.WHITE);
        decoCaseSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        decoCaseSwipe.setOnRefreshListener(onRefreshListener);
        //设置RecycleView相关事务
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        decoCaseRecycler.setLayoutManager(mLinearLayoutManager);
        decoCaseRecycler.setOnTouchListener(onTouchListener);
        decoCaseRecycler.setOnScrollListener(onScrollListener);
        //请求网络获取数据
        HttpGetDecorationCase(mPage);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            /// TODO: 2017/10/24  下拉刷新事件  1.重置页码 2.清空列表 3.网络请求数据
        }
    };
    //加载时的触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (decoCaseSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //列表上拉加载更多事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !decoCaseSwipe.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    private void loadMore() {
        // TODO: 2017/10/24  加载更多布局的变化  适配器出现加载更多图标
        mPage++;
        HttpGetDecorationCase(mPage);
    }

    //列表的变化--加载更多
    private void adapterLoadMore() {
        if (mDecorationCaseAdapter != null) {
            mDecorationCaseAdapter.changeAdapterState(2);
        }
    }

    @OnClick({R.id.deco_case_back, R.id.deco_case_find_price})
    public void onViewClickedInDecorationCaseActivity(View view) {
        switch (view.getId()) {
            case R.id.deco_case_back:
                finish();
                break;
            case R.id.deco_case_find_price_rl:
            case R.id.deco_case_find_price:
                /// TODO: 2017/10/24  跳转到发单页
                break;
        }
    }

    //网络请求数据
    private void HttpGetDecorationCase(int page) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        OKHttpUtil.post(Constant.DECORATION_CASE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功========" + json);
                /// TODO: 2017/10/24 将数据布局 
            }
        });

    }
}
