package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.CoYouHuiAdapter;
import com.tbs.tobosutype.bean._YouHui;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

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
 * 优惠活动页 3.6版本新增
 * 从装修公司主页进入
 * create by lin
 */
public class CoYouHuiActivity extends AppCompatActivity {
    @BindView(R.id.co_youhui_back_ll)
    LinearLayout coYouhuiBackLl;
    @BindView(R.id.co_youhui_recycler)
    RecyclerView coYouhuiRecycler;
    @BindView(R.id.banner_dever)
    View bannerDever;
    private String TAG = "CoYouHuiActivity";
    private Context mContext;
    private String mCompanyId;//装修公司的id 从上一个界面传来
    private Intent mIntent;
    private boolean isLoading = false;//是否在加载数据
    private LinearLayoutManager mLinearLayoutManager;
    private int mPage = 1;//加载页数
    private int mPageSize = 10;
    private ArrayList<_YouHui> mYouHuiArrayList = new ArrayList<>();
    private Gson mGson;
    private CoYouHuiAdapter mCoYouHuiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_you_hui);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mCompanyId = mIntent.getStringExtra("mCompanyId");
        mGson = new Gson();
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        coYouhuiRecycler.setLayoutManager(mLinearLayoutManager);
        coYouhuiRecycler.addOnScrollListener(onScrollListener);
        HttpGetListData(mPage);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    private void loadMore() {
        mPage++;
        HttpGetListData(mPage);
    }

    @OnClick(R.id.co_youhui_back_ll)
    public void onViewClickedInCoYouHuiActivity() {
        finish();
    }

    private void HttpGetListData(int mPage) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", mCompanyId);
        param.put("type", "1");
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.PROMOTION_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败====" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //数据获取成
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _YouHui youHui = mGson.fromJson(jsonArray.get(i).toString(), _YouHui.class);
                            mYouHuiArrayList.add(youHui);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //设置适配器
                                if (mCoYouHuiAdapter == null) {
                                    mCoYouHuiAdapter = new CoYouHuiAdapter(mContext, mYouHuiArrayList);
                                    coYouhuiRecycler.setAdapter(mCoYouHuiAdapter);
                                    mCoYouHuiAdapter.notifyDataSetChanged();
                                    mCoYouHuiAdapter.setOnYouHuiItemClickLister(onYouHuiItemClickLister);
                                } else {
                                    mCoYouHuiAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private CoYouHuiAdapter.OnYouHuiItemClickLister onYouHuiItemClickLister = new CoYouHuiAdapter.OnYouHuiItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            /// TODO: 2017/12/5 跳转到Web页面 进行发单处理
            Intent intent = new Intent(mContext, NewWebViewActivity.class);
//            String mUrl = "http://m.tobosu.com/free_price_page/";
            String mUrl = mYouHuiArrayList.get(position).getOrder_page_url() + "channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext());
            Log.e(TAG, "返回的url========" + mUrl);
            intent.putExtra("mLoadingUrl", mUrl);
            mContext.startActivity(intent);
        }
    };
}
