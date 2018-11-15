package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.AnswerDetailsViewPagerAdapter;
import com.tbs.tobosutype.adapter.AnswerItemDetailsAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.AnswerListBean;
import com.tbs.tobosutype.bean.AskDetailDataBean;
import com.tbs.tobosutype.bean.AskQuestionBean;
import com.tbs.tobosutype.bean.RelationListBean;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.utils.Util;
import com.tbs.tobosutype.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/11/6 11:39.
 * 回答详情页
 */
public class AnswerItemDetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.relBackShoucang)
    RelativeLayout relBackShoucang;        //返回按钮
    @BindView(R.id.image_top_share)
    ImageView imageTopShare;    //分享
    @BindView(R.id.rv_answerdetail)
    RecyclerView rvAnswerDetails;   //滑动布局
    @BindView(R.id.rl_cover_ad_bg)
    RelativeLayout rlCoverAdBg;
    @BindView(R.id.iv_ad)
    ViewPager ivAd;     //滑动图片显示
    @BindView(R.id.tv_current_num)
    TextView tvCurrentNum;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    @BindView(R.id.linear_askquestion)
    LinearLayout linearAskQuestion;
    @BindView(R.id.linear_reply)
    LinearLayout linearReply;

    private Gson gson;

    private AnswerDetailsViewPagerAdapter viewPagerAdapter;
    private AskQuestionBean questionBean;
    private AnswerItemDetailsAdapter adapter;   //适配器
    private AskDetailDataBean beanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_details);
        ButterKnife.bind(this);
        gson = new Gson();
        initViewEvent();
    }

    //    初始化
    private void initViewEvent() {

        questionBean = (AskQuestionBean) getIntent().getSerializableExtra(AskQuestionBean.class.getName());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAnswerDetails.setLayoutManager(linearLayoutManager);

        ivAd.addOnPageChangeListener(this);

        initHttpRequest();


    }

    /**
     * 初始化网络请求
     */
    private void initHttpRequest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
//        params.put("id", questionBean.getId());
//        params.put("uid", AppInfoUtil.getUserid(mContext));
        params.put("id", 164);
        params.put("uid", 313894);
        params.put("system_plat", 1);
        OKHttpUtil.post(Constant.ASK_QUESTION_DETAIL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.optString("data");
                        beanList = gson.fromJson(data, AskDetailDataBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new AnswerItemDetailsAdapter(AnswerItemDetailsActivity.this, beanList);
                                adapter.setOnDianZanAdapterClickLister(onDianZanAdapterClickLister);
                                rvAnswerDetails.setAdapter(adapter);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    //列表子集点击事件
    private AnswerItemDetailsAdapter.OnDianZanAdapterClickLister onDianZanAdapterClickLister = new AnswerItemDetailsAdapter.OnDianZanAdapterClickLister() {
        @Override
        public void onDianZanAdapterClick(View view, int position) {
            switch (view.getId()) {
                case R.id.image_dianzan_icon:   //点赞
                case R.id.ll_dianzan:   //点赞父布局
//                    if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
//                        //用户未登录 跳转到登录页面
//                        Toast.makeText(mContext, "您还没有登陆", Toast.LENGTH_SHORT).show();
//                    } else {
                    HttpDianZanRequest(position);
//                    }
                    break;
            }
        }

    };

    /**
     * 点赞网络请求
     *
     * @param position
     */
    private void HttpDianZanRequest(final int position) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("answer_id", beanList.getAnswerList().get(position).getAnswer_id());
        params.put("agree_uid", AppInfoUtil.getUserid(mContext));
        OKHttpUtil.post(Constant.ASK_ANSWER_AGREE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        final String msg = jsonObject.optString("msg");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //处理显示逻辑
                                if (msg.equals("操作成功")) {
                                    //收藏成功将当前的数据模型改变
                                    beanList.getAnswerList().get(position).setIs_agree(1);//修改收藏状态
                                    //之前的收藏数量
                                    int dianzanNum = Integer.parseInt(beanList.getAnswerList().get(position).getAgree_count());
                                    beanList.getAnswerList().get(position).setAgree_count((dianzanNum + 1) + "");
                                    adapter.notifyItemChanged(position + 1);
                                } else {
                                    //改变当前的数据模型 取消收藏状态
                                    beanList.getAnswerList().get(position).setIs_agree(2);//修改收藏状态
                                    //之前的收藏数量
                                    int dianzanNum = Integer.parseInt(beanList.getAnswerList().get(position).getAgree_count());
                                    beanList.getAnswerList().get(position).setAgree_count((dianzanNum - 1) + "");
                                    adapter.notifyItemChanged(position + 1);
                                }
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnClick({R.id.relBackShoucang, R.id.image_top_share, R.id.linear_askquestion, R.id.linear_reply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relBackShoucang:  //返回
                finish();
                break;
            case R.id.image_top_share:  //分享
                new ShareUtil(this, "装修tittle", "zhaugnxiu", Constant.PIPE);
                break;
            case R.id.linear_askquestion:   //我要回答
                startActivity(new Intent(AnswerItemDetailsActivity.this, ReplyActivity.class));
                break;
            case R.id.linear_reply:     //  我要提问
                startActivity(new Intent(AnswerItemDetailsActivity.this, AskQuestionActivity.class));
                break;
        }
    }

    /**
     * 点击图片预览大图
     */
    public void showBigPhoto(List<String> stringList, int position) {
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new AnswerDetailsViewPagerAdapter(this, stringList);
            ivAd.setAdapter(viewPagerAdapter);
        } else {
            viewPagerAdapter.setActivityBases(stringList);
            viewPagerAdapter.notifyDataSetChanged();
        }
        ivAd.setCurrentItem(position);
        ivAd.setOffscreenPageLimit(stringList.size());
        if (position == 0){     //防止点击第一张图片下标位置数字不改变
            tvCurrentNum.setText((position + 1) + "");
        }
        tvTotalNum.setText("/" + stringList.size());
        rlCoverAdBg.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏轮播图
     */
    public void hindViewPagerImage() {
        if (viewPagerAdapter != null) {
            viewPagerAdapter = null;
        }
        rlCoverAdBg.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvCurrentNum.setText((position + 1) + "");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.stopViewPagerThread();
        }
    }
}
