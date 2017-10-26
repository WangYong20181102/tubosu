package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecoCaseDetailAdapter;
import com.tbs.tobosutype.bean._DecoCaseDetail;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * create by lin
 * 案例详情
 * 从案例列表点击进入
 * 头部包含相关信息
 */
public class DecorationCaseDetailActivity extends BaseActivity {

    @BindView(R.id.deco_case_detail_back)
    LinearLayout decoCaseDetailBack;
    @BindView(R.id.deco_case_detail_share)
    LinearLayout decoCaseDetailShare;
    @BindView(R.id.deco_case_detail_banner)
    RelativeLayout decoCaseDetailBanner;
    @BindView(R.id.deco_case_detail_recycler)
    RecyclerView decoCaseDetailRecycler;
    @BindView(R.id.deco_case_detail_find_price)
    TextView decoCaseDetailFindPrice;
    @BindView(R.id.deco_case_detail_find_price_rl)
    RelativeLayout decoCaseDetailFindPriceRl;

    private Context mContext;
    private String TAG = "DecoCaseDetailActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private Gson mGson;
    private Intent mIntent;
    private String deco_case_id;//从上一个界面传来的案例id
    private _DecoCaseDetail mDecoCaseDetail;//数据源
    private DecoCaseDetailAdapter mDecoCaseDetailAdapter;//适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration_case_detail);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    //初始化view事件
    private void initViewEvent() {
        mIntent = getIntent();
        deco_case_id = mIntent.getStringExtra("deco_case_id");

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mGson = new Gson();
        //RecycleView列表设置相关的事件
        decoCaseDetailRecycler.setLayoutManager(mLinearLayoutManager);
        decoCaseDetailRecycler.setOnScrollListener(onScrollListener);
        //获取数据
        HttpGetDecoDetail();
    }

    //请求数据
    private void HttpGetDecoDetail() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", deco_case_id);
        OKHttpUtil.post(Constant.CASE_DETAIL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败======" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    mDecoCaseDetail = mGson.fromJson(data, _DecoCaseDetail.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDecoCaseDetailAdapter = new DecoCaseDetailAdapter(mContext, mDecoCaseDetail);
                            decoCaseDetailRecycler.setAdapter(mDecoCaseDetailAdapter);
                            mDecoCaseDetailAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //列表滑动事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            /// TODO: 2017/10/25  在这个地方设置banner的变化
        }
    };

    @OnClick({R.id.deco_case_detail_back, R.id.deco_case_detail_share, R.id.deco_case_detail_find_price, R.id.deco_case_detail_find_price_rl})
    public void onViewClickedInDecorationCaseDetailActivity(View view) {
        switch (view.getId()) {
            case R.id.deco_case_detail_back:
                //返回按钮
                finish();
                break;
            case R.id.deco_case_detail_share:
                /// TODO: 2017/10/25  分享按钮 分享整个页面 拿到分享的URl
                break;
            case R.id.deco_case_detail_find_price:
            case R.id.deco_case_detail_find_price_rl:
                /// TODO: 2017/10/25  进入报价页面
                break;
        }
    }
}
