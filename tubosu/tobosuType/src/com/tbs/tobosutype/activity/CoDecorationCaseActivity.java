package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorationCaseAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean._DecorationCaseItem;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.SpUtil;
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

public class CoDecorationCaseActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.co_deco_case_back)
    LinearLayout coDecoCaseBack;
    @BindView(R.id.co_deco_case_banner_rl)
    RelativeLayout coDecoCaseBannerRl;
    @BindView(R.id.co_deco_case_recycler)
    RecyclerView coDecoCaseRecycler;
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.co_deco_case_find_price)
    TextView coDecoCaseFindPrice;
    @BindView(R.id.co_deco_case_find_price_rl)
    RelativeLayout coDecoCaseFindPriceRl;

    private Context mContext;
    private String TAG = "CoDecorationCaseActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private Intent mIntent;
    private Gson mGson;
    private String mCompanyId = "";
    private boolean isLoading = false;//是否正在加载数据
    private int mPage = 1;//加载更多数据的页数
    private int mPageSize = 10;//加载更多数据的页数
    private DecorationCaseAdapter mDecorationCaseAdapter;
    private ArrayList<_DecorationCaseItem> decorationCaseItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_decoration_case);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvet();
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    private void initViewEvet() {
        //实例化相关工具
        mGson = new Gson();
        mIntent = getIntent();
        mCompanyId = mIntent.getStringExtra("mCompanyId");
        SpUtil.setStatisticsEventPageId(mContext, mCompanyId);
        coDecoCaseBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        //设置RecycleView相关事务
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        coDecoCaseRecycler.setLayoutManager(mLinearLayoutManager);
        coDecoCaseRecycler.setOnScrollListener(onScrollListener);
        //请求网络获取数据
        HttpGetCoDecorationCase(mPage);
    }

    //列表上拉加载更多事件
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
        HttpGetCoDecorationCase(mPage);
    }

    private void HttpGetCoDecorationCase(int mPage) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", mCompanyId);
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.COMPANY_CASE_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "数据链接失败=====" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "数据链接成功=======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _DecorationCaseItem decorationCaseItem = mGson.fromJson(jsonArray.get(i).toString(), _DecorationCaseItem.class);
                            decorationCaseItemArrayList.add(decorationCaseItem);
                        }
                        //设置适配器
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mDecorationCaseAdapter == null) {
                                    mDecorationCaseAdapter = new DecorationCaseAdapter(mContext, decorationCaseItemArrayList);
                                    coDecoCaseRecycler.setAdapter(mDecorationCaseAdapter);
                                    mDecorationCaseAdapter.notifyDataSetChanged();
                                } else {
                                    mDecorationCaseAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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

    @OnClick({R.id.co_deco_case_back, R.id.co_deco_case_find_price_rl})
    public void onViewClickedInCoDecorationCaseAc(View view) {
        switch (view.getId()) {
            case R.id.co_deco_case_back:
                finish();
                break;
            case R.id.co_deco_case_find_price_rl:
                /// TODO: 2017/10/24  跳转到免费报价发单页暂时写固定url
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", Constant.ANLI_LIST_FADAN);
                mContext.startActivity(intent);
                break;
        }
    }
}
