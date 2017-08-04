package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CaseDetailImgAdapter;
import com.tbs.tobosupicture.adapter.CaseDetailImgGVAdapter;
import com.tbs.tobosupicture.adapter.CaseDetailStayInAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.CaseDetailEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.MyListView;

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

public class CaseDetailActivity extends BaseActivity {

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
    @BindView(R.id.mylistviewCaseDetial)
    MyListView mylistviewCaseDetial;
    @BindView(R.id.tvCaseDescription)
    TextView tvCaseDescription;
    @BindView(R.id.myStageCaseDetial)
    MyListView myStageCaseDetial;
    @BindView(R.id.mylistviewStayIn)
    MyListView mylistviewStayIn;
    @BindView(R.id.layoutIneedPrice)
    LinearLayout layoutIneedPrice;
    @BindView(R.id.layoutGetPriceKnow)
    LinearLayout layoutGetPriceKnow;


    // 数据源
    private CaseDetailEntity caseDetailEntity;
    private String id;
    private String des = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        ButterKnife.bind(this);
        mContext = CaseDetailActivity.this;
        TAG = "CaseDetailActivity";

        getIntentData();
    }

    private void getIntentData() {
        if (getIntent() != null && getIntent().getBundleExtra("case_bundle") != null) {
            id = getIntent().getBundleExtra("case_bundle").getString("id");
            getDataFromNet(id);
        }
    }

    private void getDataFromNet(String param) {
        if (Utils.isNetAvailable(mContext)) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("case_id", param);
            hashMap.put("token", Utils.getDateToken());
            HttpUtils.doPost(UrlConstans.CASE_DETAIL_URL, hashMap, new Callback() {
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
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("status") == 200) {
                            Gson gson = new Gson();
                            caseDetailEntity = gson.fromJson(jsonObject.getJSONObject("data").toString(), CaseDetailEntity.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initDataInView();
                                }
                            });
                        } else {
                            final String msg = jsonObject.getString("msg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, msg);
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

    private void initDataInView() {
        des = caseDetailEntity.getCase_data().getDescription();
        tvCaseDescription.setText(des);
        GlideUtils.glideLoader(mContext, caseDetailEntity.getCase_data().getCover_url(), R.mipmap.loading_img_fail, R.mipmap.loading_img, ivDetailImg);
        GlideUtils.glideLoader(mContext, caseDetailEntity.getCase_data().getDesigner_icon(), R.mipmap.pic, R.mipmap.pic, ivDesinHead, 0);
        GlideUtils.glideLoader(mContext, caseDetailEntity.getCase_data().getLayout_url(),R.mipmap.loading_img_fail, R.mipmap.loading_img,ivBigHuxingTu, 1);

        CaseDetailImgAdapter imgAdapter = new CaseDetailImgAdapter(mContext, caseDetailEntity.getSuite());
        mylistviewCaseDetial.setAdapter(imgAdapter);
        CaseDetailImgGVAdapter imgGVAdapter = new CaseDetailImgGVAdapter(mContext, caseDetailEntity.getOnline_diagram());
        myStageCaseDetial.setAdapter(imgGVAdapter);

        CaseDetailStayInAdapter stayInAdapter = new CaseDetailStayInAdapter(mContext, caseDetailEntity.getStay_real().get(0).getImg_url());
        mylistviewStayIn.setAdapter(stayInAdapter);


        tvCaseText.setText(caseDetailEntity.getCase_data().getCity_name() + " " +
                            caseDetailEntity.getCase_data().getOwner_name() + " " +
                            caseDetailEntity.getCase_data().getShi() + "室" +
                caseDetailEntity.getCase_data().getTing() + "厅" +
                caseDetailEntity.getCase_data().getWei() + "位");
        tvDistrict.setText(caseDetailEntity.getCase_data().getDistrict_name());
        tvBudget.setText(caseDetailEntity.getCase_data().getPrice() + "万 - "+ caseDetailEntity.getCase_data().getDesmethod());
        tvDestrictStyle.setText(caseDetailEntity.getCase_data().getStyle_name());
        tvDistrictSquare.setText(caseDetailEntity.getCase_data().getArea() + "m²");
        tvDesignerName.setText(caseDetailEntity.getCase_data().getDesigner_name());
    }

    @OnClick({R.id.relCaseDetailBack, R.id.layoutIneedPrice, R.id.layoutGetPriceKnow})
    public void onViewClickedCaseDetailActivity(View view) {
        switch (view.getId()) {
            case R.id.relCaseDetailBack:
                finish();
                break;
            case R.id.layoutIneedPrice:
                startActivity(new Intent(mContext, GetPriceActivity.class));
                break;
            case R.id.layoutGetPriceKnow:
                Utils.setToast(mContext, "咨询报价");
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}