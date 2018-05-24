package com.tbs.tbs_mj.activity;

import android.app.Activity;
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
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.DecorationCaseAdapter;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.bean._DecorationCaseItem;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.Util;

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
 * 土拨鼠3.4版本 新增
 * 案例列表页 creat by lin
 */
public class DecorationCaseActivity extends com.tbs.tbs_mj.base.BaseActivity {
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
    @BindView(R.id.deco_case_banner_rl)
    RelativeLayout decoCaseBannerRl;

    private Context mContext;
    private String TAG = "DecorationCaseActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private Intent mIntent;
    private Gson mGson;
    private boolean isLoading = false;//是否正在加载数据
    private int mPage = 1;//加载更多数据的页数
    private DecorationCaseAdapter mDecorationCaseAdapter;
    private ArrayList<_DecorationCaseItem> decorationCaseItemArrayList = new ArrayList<>();

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
        decoCaseBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
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
            mPage = 1;
            if (!decorationCaseItemArrayList.isEmpty()) {
                decorationCaseItemArrayList.clear();
            }
            HttpGetDecorationCase(mPage);
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
        mPage++;
        adapterLoadMore();
        HttpGetDecorationCase(mPage);
    }

    //列表的变化--加载更多
    private void adapterLoadMore() {
        isLoading = true;
        if (mDecorationCaseAdapter != null) {
            mDecorationCaseAdapter.changeAdapterState(1);
        }
    }

    //列表恢复原状
    private void adapterRecovery() {
        isLoading = false;
        if (mDecorationCaseAdapter != null) {
            mDecorationCaseAdapter.changeAdapterState(2);
        }
    }

    @OnClick({R.id.deco_case_back, R.id.deco_case_find_price, R.id.deco_case_find_price_rl})
    public void onViewClickedInDecorationCaseActivity(View view) {
        switch (view.getId()) {
            case R.id.deco_case_back:
                finish();
                break;
            case R.id.deco_case_find_price_rl:
            case R.id.deco_case_find_price:
                /// TODO: 2017/10/24  跳转到免费报价发单页暂时写固定url
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", SpUtil.getzjzxaj30(mContext));
                mContext.startActivity(intent);
                break;
        }
    }

    //网络请求数据
    private void HttpGetDecorationCase(int page) {
        decoCaseSwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("city_name", AppInfoUtil.getCityInAnli(mContext));
        Log.e(TAG, "案例获取本地存储的城市==============" + AppInfoUtil.getCityName(mContext));
        Log.e(TAG, "案例获取本地存储的城市======案例案例========" + AppInfoUtil.getCityInAnli(mContext));
        param.put("page", page);
        param.put("page_size", "10");
        OKHttpUtil.post(Constant.CASE_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterRecovery();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _DecorationCaseItem decorationCaseItem = mGson.fromJson(jsonArray.get(i).toString(), _DecorationCaseItem.class);
                            decorationCaseItemArrayList.add(decorationCaseItem);
                        }
                        //获取数据成功将数据布局在页面中
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mDecorationCaseAdapter == null) {
                                    mDecorationCaseAdapter = new DecorationCaseAdapter(mContext, decorationCaseItemArrayList);
                                    decoCaseRecycler.setAdapter(mDecorationCaseAdapter);
                                    mDecorationCaseAdapter.notifyDataSetChanged();
                                } else {
                                    mDecorationCaseAdapter.notifyDataSetChanged();
                                }
                                adapterRecovery();
                            }
                        });
                    } else {
                        //当前没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterRecovery();
                                Toast.makeText(mContext, "当前没有更多数据~", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
