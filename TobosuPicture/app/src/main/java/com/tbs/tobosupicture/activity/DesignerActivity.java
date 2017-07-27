package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
    private SampleHorizontalListViewAdapter sampleAdapter;
    private CaseHorizontalListViewAdapter caseAdapter;
    private DesignerEntity designerEntity;


    private String iconUrl = "";
    private String viewNum = "";
    private String fanNum = "";
    private String designerName = "";

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
        String designerId = "";
        Intent intent = getIntent();
        if (intent != null && intent.getBundleExtra("designer_bundle") != null) {
            designerId = intent.getBundleExtra("designer_bundle").getString("designer_id");
        }

        if (Utils.isNetAvailable(mContext)) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("id", designerId);
            hashMap.put("token", Utils.getDateToken());
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
        iconUrl = designerEntity.getDesignerInfoEntity().getIcon();
        if (!"".equals(iconUrl)) {
            GlideUtils.glideLoader(mContext, iconUrl, R.mipmap.pic, R.mipmap.pic, ivDesignerHeadPic, 0);
        }
        designerName = designerEntity.getDesignerInfoEntity().getDesname();
        tvDesignerName.setText(designerName);
        viewNum = designerEntity.getDesignerInfoEntity().getView_count();
        if("".equals(viewNum)){
            viewNum = "0";
        }
        fanNum = designerEntity.getDesignerInfoEntity().getFans_count();
        if("".equals(fanNum)){
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
        }

        if(!"".equals(designerEntity.getDesignerInfoEntity().getArea())){
            tvGoodAt.setText(designerEntity.getDesignerInfoEntity().getArea());
        }

        if(!"".equals(designerEntity.getDesignerInfoEntity().getStyle())){
            tvGoodAtStyle.setText(designerEntity.getDesignerInfoEntity().getStyle());
        }

        if(!"".equals(designerEntity.getDesignerInfoEntity().getIntro())){
            tvDesignerDesc.setText(designerEntity.getDesignerInfoEntity().getIntro());
        }

    }

    @OnClick({R.id.tvConcern, R.id.tvDesigneForme, R.id.reDesignerlBack, R.id.tvCaseImgAll, R.id.tvSampleImgAll})
    public void onViewClickedDesignerActivtiy(View view) {
        switch (view.getId()) {
            case R.id.tvConcern:
                Utils.setToast(mContext, "关注 接口没有");
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("", "");
                // 关注 接口没有

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
                        String json = response.body().string();
                        try {
                            JSONObject object = new JSONObject(json);
                            if (object.getInt("status") == 0) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                caseB.putString("designerName", designerName);
                caseB.putInt("type", 1); // 传0表示样板图
                caseIntent.putExtra("designerBundle", caseB);
                startActivity(caseIntent);
                break;
            case R.id.tvSampleImgAll:
                Intent intent = new Intent(mContext, DesignerImgListActivity.class);
                Bundle b = new Bundle();
                b.putString("iconUrl", iconUrl);
                b.putString("viewNum", viewNum);
                b.putString("fanNum", fanNum);
                b.putString("designerName", designerName);
                b.putInt("type", 0); // 传0表示样板图
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

}
