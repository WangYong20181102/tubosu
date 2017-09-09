package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CaseHorizontalListViewAdapter;
import com.tbs.tobosupicture.adapter.SampleHorizontalListViewAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.DesignerCaseEntity;
import com.tbs.tobosupicture.bean.DesignerEntity;
import com.tbs.tobosupicture.bean.DesignerImpressionEntity;
import com.tbs.tobosupicture.bean.DesignerJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.HorizontalListView;

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
 * 设计师页面
 * Created by Lie on 2017/7/18.
 */

public class DesignerActivity extends BaseActivity {

    @BindView(R.id.reDesignerlBack)
    RelativeLayout reDesignerlBack;
    @BindView(R.id.ivDesignerHeadPic)
    ImageView ivDesignerHeadPic;
    @BindView(R.id.tvDesignerName)
    TextView tvDesignerName;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvConcern)
    TextView tvConcern;
    @BindView(R.id.tvDesigneForme)
    TextView tvDesigneForme;
    @BindView(R.id.tvSampleImgNum)
    TextView tvSampleImgNum;
    @BindView(R.id.tvSampleImgAll)
    TextView tvSampleImgAll;
    @BindView(R.id.sampleHorizontalListView)
    HorizontalListView sampleHorizontalListView;
    @BindView(R.id.tvCaseImgNum)
    TextView tvCaseImgNum;
    @BindView(R.id.tvCaseImgAll)
    TextView tvCaseImgAll;
    @BindView(R.id.caseHorizontalListView)
    HorizontalListView caseHorizontalListView;
    @BindView(R.id.tvGoodAt)
    TextView tvGoodAt;
    @BindView(R.id.tvGoodAtStyle)
    TextView tvGoodAtStyle;
    @BindView(R.id.tvDesignerDesc)
    TextView tvDesignerDesc;
    @BindView(R.id.designerCaseLayout)
    LinearLayout designerCaseLayout;
    @BindView(R.id.designerSampleLayout)
    LinearLayout designerSampleLayout;

    private SampleHorizontalListViewAdapter sampleAdapter;
    private CaseHorizontalListViewAdapter caseAdapter;
    private DesignerEntity designerEntity;

    private String iconUrl = "";
    private String viewNum = "";
    private String fanNum = "";
    private String designerName = "";

    private String designerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "DesignerActivity";
        mContext = DesignerActivity.this;
        setContentView(R.layout.activity_designer);
        ButterKnife.bind(this);
        getDataFromNet();
    }

    private void getDataFromNet() {

        Intent intent = getIntent();
        if (intent != null && intent.getBundleExtra("designer_bundle") != null) {
            designerId = intent.getBundleExtra("designer_bundle").getString("designer_id");
        }

        if (Utils.isNetAvailable(mContext)) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("id", designerId);
            hashMap.put("token", Utils.getDateToken());
            hashMap.put("uid", SpUtils.getUserUid(mContext));
            HttpUtils.doPost(UrlConstans.DESIGNER_URL, hashMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(mContext, "系统繁忙，稍后再试!");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Utils.setErrorLog(TAG, json);
                    DesignerJsonEntity designerJsonEntity = new DesignerJsonEntity(json);
                    if (designerJsonEntity.getStatus() == 200) {
                        designerEntity = designerJsonEntity.getDesignerEntity();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initView();
                            }
                        });
                    }

                }
            });

        }
    }

    private void initView() {
        if ("1".equals(designerEntity.getDesignerInfoEntity().getIs_follow())) {
            tvConcern.setTextColor(Color.parseColor("#FA8817"));
            tvConcern.setText("取消关注");
            Drawable leftDrawable = getResources().getDrawable(R.drawable.jiaguanzhu);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            tvConcern.setCompoundDrawables(leftDrawable, null, null, null);
        } else {
            tvConcern.setTextColor(Color.parseColor("#858585"));
            tvConcern.setText("关注");
            Drawable leftDrawable = getResources().getDrawable(R.drawable.jiaguanzhu2);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            tvConcern.setCompoundDrawables(leftDrawable, null, null, null);
        }

        iconUrl = designerEntity.getDesignerInfoEntity().getIcon();
        if (!"".equals(iconUrl)) {
            GlideUtils.glideLoader(mContext, iconUrl, R.mipmap.pic, R.mipmap.pic, ivDesignerHeadPic, 0);
        }
        designerName = designerEntity.getDesignerInfoEntity().getDesname();
        tvDesignerName.setText(designerName);
        viewNum = designerEntity.getDesignerInfoEntity().getView_count();
        if ("".equals(viewNum)) {
            viewNum = "0";
        }
        fanNum = designerEntity.getDesignerInfoEntity().getFans_count();
        if ("".equals(fanNum)) {
            fanNum = "0";
        }
        tvNum.setText("粉丝: " + fanNum + " /  浏览" + viewNum);

        tvSampleImgNum.setText("(" + designerEntity.getDesignerInfoEntity().getImpression_count() + ")");
        tvCaseImgNum.setText("(" + designerEntity.getDesignerInfoEntity().getCase_count() + ")");
        // sample
        ArrayList<DesignerImpressionEntity> sampleData = designerEntity.getDesignerImpressionEntityList();

        if (sampleData != null && sampleData.size() > 0) {
            sampleAdapter = new SampleHorizontalListViewAdapter(mContext, sampleData);
            sampleHorizontalListView.setAdapter(sampleAdapter);
            sampleHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    startActivity(new Intent(mContext, .class));
                }
            });
        } else {
            sampleHorizontalListView.setVisibility(View.GONE);
            designerSampleLayout.setVisibility(View.GONE);
        }

        // case
        ArrayList<DesignerCaseEntity> caseData = designerEntity.getDesignerCaseEntityList();

        if (caseData != null && caseData.size() > 0) {

            caseAdapter = new CaseHorizontalListViewAdapter(mContext, caseData);
            caseHorizontalListView.setAdapter(caseAdapter);
            caseHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    startActivity(new Intent(mContext, .class));
                }
            });
        } else {
            caseHorizontalListView.setVisibility(View.GONE);
            designerCaseLayout.setVisibility(View.GONE);
        }

        if (!"".equals(designerEntity.getDesignerInfoEntity().getArea())) {
            tvGoodAt.setText(designerEntity.getDesignerInfoEntity().getArea());
        }

        if (!"".equals(designerEntity.getDesignerInfoEntity().getStyle())) {
            tvGoodAtStyle.setText(designerEntity.getDesignerInfoEntity().getStyle());
        }

        if (!"".equals(designerEntity.getDesignerInfoEntity().getIntro())) {
            String text = designerEntity.getDesignerInfoEntity().getIntro().replace("\"", "&quot;");
            tvDesignerDesc.setText(text);
        }

    }

    @OnClick({R.id.tvConcern, R.id.tvDesigneForme, R.id.reDesignerlBack, R.id.tvCaseImgAll, R.id.tvSampleImgAll})
    public void onViewClickedDesignerActivtiy(View view) {
        switch (view.getId()) {
            case R.id.tvConcern:
                if(Utils.userIsLogin(mContext)){
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("token", Utils.getDateToken());
                    hashMap.put("uid", SpUtils.getUserUid(mContext));
                    hashMap.put("designer_id", designerId);
                    HttpUtils.doPost(UrlConstans.CONCERN_URL, hashMap, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, "关注失败，稍后再试");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String json = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject object = new JSONObject(json);
                                        if (object.getInt("status") == 200) {
                                            if (object.getString("msg").contains("取消")) {
                                                tvConcern.setText("关注");
                                                tvConcern.setTextColor(Color.parseColor("#858585"));
                                                Drawable leftDrawable = getResources().getDrawable(R.drawable.jiaguanzhu2);
                                                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                                                tvConcern.setCompoundDrawables(leftDrawable, null, null, null);
                                            } else {
                                                tvConcern.setText("取消关注");
                                                tvConcern.setTextColor(Color.parseColor("#FA8817"));
                                                Drawable leftDrawable = getResources().getDrawable(R.drawable.jiaguanzhu);
                                                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                                                tvConcern.setCompoundDrawables(leftDrawable, null, null, null);
                                            }
                                        }
                                        Utils.setToast(mContext, object.getString("msg"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.tvDesigneForme:
                startActivity(new Intent(mContext, GetPriceActivity.class));
                break;
            case R.id.reDesignerlBack:
                finish();
                break;
            case R.id.tvCaseImgAll:
                Intent caseIntent = new Intent(mContext, DesignerImgListActivity.class);
                Bundle caseB = new Bundle();
                caseB.putString("iconUrl", iconUrl);
                caseB.putString("viewNum", viewNum);
                caseB.putString("fanNum", fanNum);
                caseB.putString("desid", designerId);
                caseB.putString("designerName", designerName);
                caseB.putInt("type", 1); // 传1表示案例图
                caseB.putString("isCollect", designerEntity.getDesignerInfoEntity().getIs_follow());
                caseIntent.putExtra("designerBundle", caseB);
                startActivity(caseIntent);
                break;
            case R.id.tvSampleImgAll:
                Intent intent = new Intent(mContext, DesignerImgListActivity.class);
                Bundle b = new Bundle();
                b.putString("iconUrl", iconUrl);
                b.putString("viewNum", viewNum);
                b.putString("fanNum", fanNum);
                b.putString("desid", designerId);
                b.putString("designerName", designerName);
                b.putInt("type", 0); // 传0表示样板图
                b.putString("isCollect", designerEntity.getDesignerInfoEntity().getIs_follow());
                intent.putExtra("designerBundle", b);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromNet();
    }
}
