package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

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
public class DecorationCaseDetailActivity extends Activity {

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
    @BindView(R.id.decoration_case_detail_ll)
    RelativeLayout decorationCaseDetailLl;
    @BindView(R.id.deco_case_detail_title)
    TextView decoCaseDetailTitle;
    @BindView(R.id.banner_dever)
    View bannerDever;

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
        //设置控件的透明度
        decoCaseDetailBanner.getBackground().setAlpha(0);
        decoCaseDetailBanner.setVisibility(View.GONE);


        decorationCaseDetailLl.setBackgroundColor(Color.parseColor("#ffffff"));
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
                Log.e(TAG, "链接成功========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    mDecoCaseDetail = mGson.fromJson(data, _DecoCaseDetail.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDecoCaseDetailAdapter = new DecoCaseDetailAdapter(mContext, mDecoCaseDetail);
                            mDecoCaseDetailAdapter.setOnItemBtnClickLister(onItemBtnClickLister);
                            if (TextUtils.isEmpty(mDecoCaseDetail.getOwner_name())) {
                                decoCaseDetailTitle.setText("案例详情");
                            } else {
                                decoCaseDetailTitle.setText(mDecoCaseDetail.getOwner_name());
                            }
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

    //列表子项点击事件
    private DecoCaseDetailAdapter.OnItemBtnClickLister onItemBtnClickLister = new DecoCaseDetailAdapter.OnItemBtnClickLister() {
        @Override
        public void onItemBtnClick(View view, int position) {
            switch (view.getId()) {
                case R.id.item_deco_detail_back:
                    //返回
                    finish();
                    break;
                case R.id.item_deco_detail_share:
                    //分享
                    UMWeb umWeb = new UMWeb(mDecoCaseDetail.getShare_url());
                    umWeb.setDescription(mDecoCaseDetail.getDesc());
                    umWeb.setTitle(mDecoCaseDetail.getOwner_name());
                    umWeb.setThumb(new UMImage(mContext, mDecoCaseDetail.getCover_url()));
                    new ShareAction(DecorationCaseDetailActivity.this)
                            .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                            .withMedia(umWeb).open();
                    break;
            }
        }
    };
    //列表滑动事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        private int totalDy = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e(TAG, "onScrollStateChanged======" + newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            /// TODO: 2017/10/25  在这个地方设置banner的变化
            totalDy += dy;
//            Log.e(TAG, "列表的数据变换=====totalDy====" + totalDy + "===dy===" + dy);
            //控制显示与否
            if (totalDy > 3) {
                decoCaseDetailBanner.setVisibility(View.VISIBLE);
                mDecoCaseDetailAdapter.changeTitle(0);
            } else {
                decoCaseDetailBanner.setVisibility(View.GONE);
                mDecoCaseDetailAdapter.changeTitle(1);
            }
            //设置控件的透明度 totaldy==700zuo you
            if (totalDy <= 450) {
                decoCaseDetailBanner.getBackground().setAlpha((int) (totalDy * (255 / 450.1)));
                bannerDever.setVisibility(View.GONE);
            } else {
                decoCaseDetailBanner.getBackground().setAlpha(255);
                bannerDever.setVisibility(View.VISIBLE);
            }

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
                //分享
                UMWeb umWeb = new UMWeb(mDecoCaseDetail.getShare_url());
                umWeb.setDescription(mDecoCaseDetail.getDesc());
                umWeb.setTitle(mDecoCaseDetail.getOwner_name());
                umWeb.setThumb(new UMImage(mContext, mDecoCaseDetail.getCover_url()));
                new ShareAction(DecorationCaseDetailActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                        .withMedia(umWeb).open();
                break;
            case R.id.deco_case_detail_find_price:
            case R.id.deco_case_detail_find_price_rl:
                /// TODO: 2017/10/24  跳转到免费报价发单页暂时写固定url
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", Constant.DALIBAO);
                mContext.startActivity(intent);
                break;
        }
    }
}
