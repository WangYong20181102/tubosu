package com.tbs.tobosutype.activity;

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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.AnswerDetailsViewPagerAdapter;
import com.tbs.tobosutype.adapter.AnswerItemDetailsAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.AskDetailDataBean;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.SharePreferenceUtil;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/11/6 11:39.
 * 回答详情页
 */
public class AnswerItemDetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.relBackShoucang)
    RelativeLayout relBackShoucang;        //返回按钮父布局
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

    /**
     * 点击浏览大图适配器
     */
    private AnswerDetailsViewPagerAdapter viewPagerAdapter;
    private String question_id = ""; //问题ID
    private AnswerItemDetailsAdapter adapter;   //适配器
    private AskDetailDataBean beanList; //数据集合类
    private boolean isLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_details);
        ButterKnife.bind(this);
        gson = new Gson();
        initViewEvent();
    }

    /**
     * 初始化
     */
    private void initViewEvent() {
        //获取intent传递数据
        question_id = getIntent().getStringExtra("question_id");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAnswerDetails.setLayoutManager(linearLayoutManager);

        ivAd.addOnPageChangeListener(this);

        initHttpRequest(question_id);
        initStatisticsRequest();


    }

    /**
     * 进入详情页面浏览量统计
     */
    private void initStatisticsRequest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("question_id", question_id);
        OKHttpUtil.post(Constant.ASK_QUESTION_VIEW_COUNT, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 回调
     *
     * @param event 事件
     */
    @Override
    protected void receiveEvent(Event event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EC.EventCode.SEND_SUCCESS_CLOSE_ASKANSWER: //提问成功回调
                initHttpRequest((String) event.getData());
                break;
            case EC.EventCode.SEND_SUCCESS_REPLY:   //回答、评论成功回调
                initHttpRequest(question_id);
                break;
            case EC.EventCode.CLOSE_NEW_LOGIN_ACTIVITY://登录成功
                initHttpRequest(question_id);
                break;
        }
    }

    /**
     * 初始化网络请求
     */
    private void initHttpRequest(String questionId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("id", questionId);
        params.put("uid", AppInfoUtil.getUserid(mContext));
        params.put("system_plat", 1);
        OKHttpUtil.post(Constant.ASK_QUESTION_DETAIL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.optString("data");
                        beanList = gson.fromJson(data, AskDetailDataBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter == null) {
                                    adapter = new AnswerItemDetailsAdapter(AnswerItemDetailsActivity.this, beanList);
                                    rvAnswerDetails.setAdapter(adapter);
                                    adapter.setOnDianZanAdapterClickLister(onDianZanAdapterClickLister);
                                } else {
                                    adapter.changeList(beanList);
                                    rvAnswerDetails.scrollToPosition(0);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(mContext,jsonObject.optString("msg"));
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
                case R.id.image_like_icon:   //点赞
                case R.id.ll_like:   //点赞父布局
                    if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {   //未登录状态下点赞逻辑处理
                        if (!isLike) {
                            isLike = true;
                            //点赞成功将当前的数据模型改变
                            beanList.getAnswerList().get(position).setIs_agree(1);//修改点赞状态
                            //之前的点赞数量
                            int likeNum;
                            if (beanList.getAnswerList().get(position).getAgree_count().trim().isEmpty()) {
                                likeNum = 0;
                            } else {
                                likeNum = Integer.parseInt(beanList.getAnswerList().get(position).getAgree_count());
                            }
                            beanList.getAnswerList().get(position).setAgree_count((likeNum + 1) + "");
                            adapter.notifyItemChanged(position + 1);
                            HttpLikeRequest(position, false);
                        } else {
                            isLike = false;
                            //改变当前的数据模型 取消点赞状态
                            beanList.getAnswerList().get(position).setIs_agree(2);//修改点赞状态
                            //之前的点赞数量
                            int likeNum;
                            if (beanList.getAnswerList().get(position).getAgree_count().trim().isEmpty()) {
                                likeNum = 0;
                            } else {
                                likeNum = Integer.parseInt(beanList.getAnswerList().get(position).getAgree_count());
                            }
                            beanList.getAnswerList().get(position).setAgree_count((likeNum - 1) + "");
                            adapter.notifyItemChanged(position + 1);
                        }
                        return;
                    }
                    HttpLikeRequest(position, true);
                    break;
            }
        }

    };

    /**
     * 点赞网络请求
     *
     * @param position
     * @param b
     */
    private void HttpLikeRequest(final int position, final boolean b) {
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
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (b) {
                                    //处理显示逻辑
                                    if (msg.equals("点赞成功")) {
                                        //点赞成功将当前的数据模型改变
                                        beanList.getAnswerList().get(position).setIs_agree(1);//修改点赞状态
                                        //之前的点赞数量
                                        int likeNum;
                                        if (beanList.getAnswerList().get(position).getAgree_count().trim().isEmpty()) {
                                            likeNum = 0;
                                        } else {
                                            likeNum = Integer.parseInt(beanList.getAnswerList().get(position).getAgree_count());
                                        }
                                        beanList.getAnswerList().get(position).setAgree_count((likeNum + 1) + "");
                                        adapter.notifyItemChanged(position + 1);
                                    } else {
                                        //改变当前的数据模型 取消点赞状态
                                        beanList.getAnswerList().get(position).setIs_agree(2);//修改点赞状态
                                        //之前的点赞数量
                                        int likeNum;
                                        if (beanList.getAnswerList().get(position).getAgree_count().trim().isEmpty()) {
                                            likeNum = 0;
                                        } else {
                                            likeNum = Integer.parseInt(beanList.getAnswerList().get(position).getAgree_count());
                                        }
                                        beanList.getAnswerList().get(position).setAgree_count((likeNum - 1) + "");
                                        adapter.notifyItemChanged(position + 1);
                                    }
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(AnswerItemDetailsActivity.this, msg);
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
                String imageUrl;
                if (beanList == null) {    //是空就返回，防止没数据时用户点击闪退
                    return;
                }
                if (!beanList.getQuestionList().getImg_urls()[0].trim().isEmpty()) {
                    imageUrl = beanList.getQuestionList().getImg_urls()[0];
                } else {
                    imageUrl = "";
                }
                new ShareUtil(this, beanList.getQuestionList().getTitle(), beanList.getQuestionList().getContent(), imageUrl, "");
                break;
            case R.id.linear_askquestion:   //我要回答
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
                    Toast.makeText(mContext, "您还没有登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, NewLoginActivity.class);
                    startActivityForResult(intent, 0);
                    return;
                }
                Intent intent1 = new Intent(AnswerItemDetailsActivity.this, ReplyActivity.class);
                intent1.putExtra("question_id", beanList.getQuestionList().getQuestion_id());
                startActivity(intent1);
                break;
            case R.id.linear_reply:     //  我要提问
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
                    Toast.makeText(mContext, "您还没有登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, NewLoginActivity.class);
                    startActivityForResult(intent, 0);
                    return;
                }
                Intent intent2 = new Intent(this, AskQuestionActivity.class);
                intent2.putExtra("type", 2); //从详情界面进入提问
                startActivity(intent2);
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
        if (position == 0) {     //防止点击第一张图片下标位置数字不改变
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
