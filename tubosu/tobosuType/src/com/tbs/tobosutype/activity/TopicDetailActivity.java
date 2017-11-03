package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.tbs.tobosutype.adapter.TopicDetailAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean._TopicDetail;
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
 * 专题详情
 * create by lin
 */

public class TopicDetailActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.topic_detail_back)
    LinearLayout topicDetailBack;
    @BindView(R.id.topic_detail_title)
    TextView topicDetailTitle;
    @BindView(R.id.topic_detail_share)
    LinearLayout topicDetailShare;
    @BindView(R.id.topic_detail_recycler)
    RecyclerView topicDetailRecycler;
    @BindView(R.id.topic_detail_find_price)
    TextView topicDetailFindPrice;
    @BindView(R.id.ltopic_detail_find_price_rl)
    RelativeLayout ltopicDetailFindPriceRl;
    @BindView(R.id.topic_detail_banner)
    RelativeLayout topicDetailBanner;
    @BindView(R.id.topic_detail_all)
    RelativeLayout topicDetailAll;

    private Context mContext;
    private String TAG = "TopicDetailActivity";
    private Intent mIntent;
    private String mTopicId = "";//专题详情的id 从上一个界面传来
    private LinearLayoutManager mLinearLayoutManager;
    private Gson mGson;
    private _TopicDetail mTopicDetail;
    private TopicDetailAdapter mTopicDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        topicDetailBanner.setBackgroundColor(Color.parseColor("#ffffff"));
        topicDetailAll.setBackgroundColor(Color.parseColor("#ffffff"));
        mTopicId = mIntent.getStringExtra("mTopicId");
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        topicDetailRecycler.setLayoutManager(mLinearLayoutManager);
        //获取详情
        HttpGetTopicDetail();
    }

    private void HttpGetTopicDetail() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", mTopicId);
        OKHttpUtil.post(Constant.TOPIC_DETAIL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //数据链接成功

                        String data = jsonObject.getString("data");
                        mTopicDetail = mGson.fromJson(data, _TopicDetail.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTopicDetailAdapter = new TopicDetailAdapter(mContext, mTopicDetail);
                                topicDetailRecycler.setAdapter(mTopicDetailAdapter);
                                mTopicDetailAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        //链接数据失败

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.topic_detail_back, R.id.topic_detail_share, R.id.topic_detail_find_price, R.id.ltopic_detail_find_price_rl})
    public void onViewClickedInTopicDetailActivity(View view) {
        switch (view.getId()) {
            case R.id.topic_detail_back:
                finish();
                break;
            case R.id.topic_detail_share:
                //分享
                UMWeb umWeb = new UMWeb(mTopicDetail.getShare_url());
                umWeb.setDescription(mTopicDetail.getDesc());
                umWeb.setTitle(mTopicDetail.getTitle());
                umWeb.setThumb(new UMImage(mContext, mTopicDetail.getCover_url()));
                new ShareAction(TopicDetailActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                        .withMedia(umWeb).open();
                break;
            case R.id.topic_detail_find_price:
            case R.id.ltopic_detail_find_price_rl:
                //跳转到发单页
                /// TODO: 2017/10/24  跳转到免费报价发单页暂时写固定url
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", Constant.DALIBAO);
                mContext.startActivity(intent);
                break;
        }
    }

}
