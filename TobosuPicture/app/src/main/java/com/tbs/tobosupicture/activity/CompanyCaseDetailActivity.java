package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CaseDetailImgAdapter;
import com.tbs.tobosupicture.adapter.CaseDetailImgGVAdapter;
import com.tbs.tobosupicture.adapter.CaseDetailStayInAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.CaseDetailJsonEntity1;
import com.tbs.tobosupicture.bean.CaseDetailJsonEntity2;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.MyListView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
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
 * Created by Lie on 2017/8/1.
 */

public class CompanyCaseDetailActivity extends BaseActivity {
    @BindView(R.id.ivCompanyPic)
    ImageView ivCompanyPic;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.ivCompanyVerified)
    ImageView ivCompanyVerified;
    @BindView(R.id.companyAddress)
    TextView companyAddress;
    @BindView(R.id.companyTel)
    TextView companyTel;
    @BindView(R.id.companyPhone)
    TextView companyPhone;
    @BindView(R.id.ivDetailImg)
    ImageView ivDetailImg;
    @BindView(R.id.relCaseDetailBack)
    RelativeLayout relCaseDetailBack;
    @BindView(R.id.tvCaseText)
    TextView tvCaseText;
    @BindView(R.id.tvDistrict)
    TextView tvDistrict;
    @BindView(R.id.tvBudget)
    TextView tvBudget;
    @BindView(R.id.tvDestrictStyle)
    TextView tvDestrictStyle;
    @BindView(R.id.tvDistrictSquare)
    TextView tvDistrictSquare;
    @BindView(R.id.ivDesinHead)
    ImageView ivDesinHead;
    @BindView(R.id.tvDesignerName)
    TextView tvDesignerName;
    @BindView(R.id.ivBigHuxingTu)
    ImageView ivBigHuxingTu;
    @BindView(R.id.mylistviewCaseDetial)  // 设计图
    MyListView mylistviewCaseDetial;
    @BindView(R.id.tvCaseDescription)
    TextView tvCaseDescription;
    @BindView(R.id.myStageCaseDetial)
    MyListView myStageCaseDetial;        // 施工阶段
    @BindView(R.id.mylistviewStayIn)
    MyListView mylistviewStayIn;         // 入住场景
    @BindView(R.id.layoutIneedPrice)
    LinearLayout layoutIneedPrice;
    @BindView(R.id.layoutGetPriceKnow)
    LinearLayout layoutGetPriceKnow;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;
    @BindView(R.id.ivShareCase)
    ImageView ivShareCase;
    @BindView(R.id.shareProgressBar)
    ProgressBar shareProgressBar;
    @BindView(R.id.caseDetailScrollView)
    ScrollView caseDetailScrollView;

    @BindView(R.id.layoutStayInPic)
    LinearLayout layoutStayInPic;
    @BindView(R.id.layoutStage)
    LinearLayout layoutStage;
    @BindView(R.id.layoutDesignChart)
    LinearLayout layoutDesignChart;

    private CaseDetailJsonEntity2 detaiJsonEntity;

    private String id;
    private String des = "";            // 分享 简介
    private String shareUrl = "";       // 分享 链接
    private String shareTitle = "";     // 分享 标题
    private String shareImg = "";       // 分享 图片

    private String imgStringData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search_case_detail);
        ButterKnife.bind(this);
        mContext = CompanyCaseDetailActivity.this;
        TAG = "CaseDetailActivity";
        caseDetailScrollView.smoothScrollTo(0,20);
        getIntentData();
        setClick();
    }

    private void getIntentData() {
        if (getIntent() != null && getIntent().getBundleExtra("case_bundle") != null) {
            id = getIntent().getBundleExtra("case_bundle").getString("id");
            getDataFromNet(id);
        }

        Utils.setErrorLog(TAG, "case_id是什么呢" + id +"  用户id"+ SpUtils.getUserUid(mContext) + " ***  " + Utils.getDateToken());

    }


    /**
     * 点击收藏
     */
    private void setClick(){
        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetAvailable(mContext)){
                    if(Utils.userIsLogin(mContext)){
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("case_id", id);
                        hashMap.put("uid", SpUtils.getUserUid(mContext));
                        hashMap.put("token", Utils.getDateToken());

                        Utils.setErrorLog(TAG, "==案例id>>" + id +"     用户id"+ SpUtils.getUserUid(mContext) + "   ==>>>" + Utils.getDateToken());

                        HttpUtils.doPost(UrlConstans.CLICK_CASE_COLLECT_URL, hashMap, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.setToast(mContext, "网络繁忙，请稍后再试~");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String json = response.body().string();
                                Utils.setErrorLog(TAG, json);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(json);
                                            String ms = jsonObject.getString("msg");
                                            if(jsonObject.getInt("status") == 200){
                                                if(ms.contains("取消")){
                                                    // 取消收藏成功
                                                    ivCollect.setBackgroundResource(R.mipmap.shoucang21);
                                                }else {
                                                    // 收藏成功
                                                    ivCollect.setBackgroundResource(R.mipmap.shoucang4);
                                                }
                                                Utils.setToast(mContext, ms);
                                            }else {
                                                Utils.setToast(mContext, "操作失败，稍后再试~");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }
            }
        });
    }

    /**
     * 请求网络获取本详情数据
     * @param caseid
     */
    private void getDataFromNet(String caseid) {
        if (Utils.isNetAvailable(mContext)) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("case_id", caseid);
            hashMap.put("token", Utils.getDateToken());
            hashMap.put("uid", SpUtils.getUserUid(mContext));

            Utils.setErrorLog(TAG, "companycase 长度  caseid = " + caseid);

            HttpUtils.doPost(UrlConstans.COMPANY_CASE_DETAIL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(mContext, "系统繁忙");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Utils.setErrorLog(TAG, json);

                    detaiJsonEntity = new CaseDetailJsonEntity2(json);
                    if(detaiJsonEntity.getStatus() == 200){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgStringData = detaiJsonEntity.getAllImgDataString();
                                initDataInView();
                            }
                        });
                    }else {
                        final String msg = detaiJsonEntity.getMsg();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(mContext, msg);
                            }
                        });
                    }

                }
            });
        }
    }

    private String isCollect = "3"; // 3是无意义的
    private void initDataInView() {
        GlideUtils.glideLoader(mContext, detaiJsonEntity.getCompanyEntity().getIcon(), R.mipmap.loading_img_fail, R.mipmap.loading_img, ivCompanyPic);
        tvCompanyName.setText(detaiJsonEntity.getCompanyEntity().getName());
        if("1".equals(detaiJsonEntity.getCompanyEntity().getCertification())){
            ivCompanyVerified.setVisibility(View.VISIBLE);
        }else {
            ivCompanyVerified.setVisibility(View.GONE);
        }
        String companyAddressString = detaiJsonEntity.getCompanyEntity().getAddress();
        String companyPhoneString = detaiJsonEntity.getCompanyEntity().getCellphone();
        String companyTelString = detaiJsonEntity.getCompanyEntity().getTelephone_number();
        if(!"".equals(companyAddressString)){
            companyAddress.setText(companyAddressString);
        }else {
            companyAddress.setText("暂无");
        }

        if(!"".equals(companyTelString)){
            companyTel.setText(companyTelString);
        }else {
            companyTel.setText("暂无");
        }

        if(!"".equals(companyPhoneString)){
            companyPhone.setText(companyPhoneString);
        }else {
            companyPhone.setText("暂无");
        }

        des = detaiJsonEntity.getCaseInfoEntity().getDescription();
        tvCaseDescription.setText(des);

        shareUrl = detaiJsonEntity.getCaseInfoEntity().getShare_url();
        shareTitle = detaiJsonEntity.getCaseInfoEntity().getTitle();
        shareImg = detaiJsonEntity.getCaseInfoEntity().getCover_url();

        isCollect = detaiJsonEntity.getCaseInfoEntity().getIs_collect();
        Utils.setErrorLog(TAG, "当前进入页面时的标识是" + isCollect);

        if("0".equals(isCollect)){
            // 未收藏
            ivCollect.setBackgroundResource(R.mipmap.shoucang21);
        }else{
            ivCollect.setBackgroundResource(R.mipmap.shoucang4);
        }
        GlideUtils.glideLoader(mContext, detaiJsonEntity.getCaseInfoEntity().getCover_url(), R.mipmap.loading_img_fail, R.mipmap.loading_img, ivDetailImg);
        GlideUtils.glideLoader(mContext, detaiJsonEntity.getCaseInfoEntity().getDesigner_icon(), R.mipmap.pic, R.mipmap.pic, ivDesinHead, 0);
        GlideUtils.glideLoader(mContext, detaiJsonEntity.getCaseInfoEntity().getLayout_url(),R.mipmap.loading_img_fail, R.mipmap.loading_img,ivBigHuxingTu, 1);
        ivBigHuxingTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SeeBigImgActivity.class);
                it.putExtra("imgStringData", imgStringData);
                it.putExtra("imgurl", detaiJsonEntity.getCaseInfoEntity().getLayout_url());
                startActivity(it);
            }
        });


        // 设计图
        if(detaiJsonEntity.getSuiteEntiyDataList()!=null){
            CaseDetailImgAdapter imgAdapter = new CaseDetailImgAdapter(mContext, detaiJsonEntity.getSuiteEntiyDataList());
            mylistviewCaseDetial.setAdapter(imgAdapter);
            mylistviewCaseDetial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it = new Intent(mContext, SeeBigImgActivity.class);
                    it.putExtra("imgStringData", imgStringData);
                    it.putExtra("imgurl", detaiJsonEntity.getSuiteEntiyDataList().get(position).getImg_url());
                    startActivity(it);
                }
            });
        }else {
            // 无设计图
            layoutDesignChart.setVisibility(View.GONE);
        }

        //  施工阶段
        if(detaiJsonEntity.getOnlineDiagramDataList()!=null){
            CaseDetailImgGVAdapter imgGVAdapter = new CaseDetailImgGVAdapter(mContext, detaiJsonEntity.getOnlineDiagramDataList(), imgStringData);
            myStageCaseDetial.setAdapter(imgGVAdapter);
        }else {
            // 无施工阶段
            layoutStage.setVisibility(View.GONE);
        }

        // 入住场景
        if(detaiJsonEntity.getStayRealImgList() != null && detaiJsonEntity.getStayRealImgList().size()>0){
            CaseDetailStayInAdapter stayInAdapter = new CaseDetailStayInAdapter(mContext, detaiJsonEntity.getStayRealImgList());
            mylistviewStayIn.setAdapter(stayInAdapter);
            mylistviewStayIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it = new Intent(mContext, SeeBigImgActivity.class);
                    it.putExtra("imgStringData", imgStringData);
                    it.putExtra("imgurl", detaiJsonEntity.getStayRealImgList().get(position));
                    startActivity(it);
                }
            });
        }else{
            // 无入住场景
            layoutStayInPic.setVisibility(View.GONE);
            Utils.setErrorLog(TAG, " 长度  入住场景为空");
        }


        tvCaseText.setText(detaiJsonEntity.getCaseInfoEntity().getCity_name() + " " +
//                detaiJsonEntity.getCaseInfoEntity().getOwner_name() + " " +
                detaiJsonEntity.getCaseInfoEntity().getShi() + "室" +
                detaiJsonEntity.getCaseInfoEntity().getTing() + "厅" +
                detaiJsonEntity.getCaseInfoEntity().getWei() + "卫");
        tvDistrict.setText(detaiJsonEntity.getCaseInfoEntity().getDistrict_name());
        tvBudget.setText(detaiJsonEntity.getCaseInfoEntity().getPrice() + "万 - "+ detaiJsonEntity.getCaseInfoEntity().getDesmethod());
        tvDestrictStyle.setText(detaiJsonEntity.getCaseInfoEntity().getStyle_name());
        tvDistrictSquare.setText(detaiJsonEntity.getCaseInfoEntity().getArea() + "m²");
        tvDesignerName.setText(detaiJsonEntity.getCaseInfoEntity().getDesigner_name());
    }

    @OnClick({R.id.relCaseDetailBack, R.id.layoutIneedPrice, R.id.layoutGetPriceKnow, R.id.ivShareCase})
    public void onViewClickedCaseDetailActivity(View view) {
        switch (view.getId()) {
            case R.id.relCaseDetailBack:
                finish();
                break;
            case R.id.layoutIneedPrice:
                startActivity(new Intent(mContext, GetPriceActivity.class));
                break;
            case R.id.layoutGetPriceKnow:
                startActivity(new Intent(mContext, SmartDesignActivity.class));
                break;
            case R.id.ivShareCase:
                UMWeb umWeb = new UMWeb(shareUrl);
                umWeb.setDescription(des);
                umWeb.setTitle(shareTitle);
                umWeb.setThumb(new UMImage(mContext,shareImg));
                new ShareAction(CompanyCaseDetailActivity.this)
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .withMedia(umWeb)
//                        .setCallback(umShareListener)
                        .open();
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            shareProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            shareProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            shareProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            shareProgressBar.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}