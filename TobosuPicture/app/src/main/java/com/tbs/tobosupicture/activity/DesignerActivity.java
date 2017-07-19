package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.DesignerEntity;
import com.tbs.tobosupicture.bean.DesignerJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;

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
    private DesignerEntity designerEntity;

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
        String designerId = null;
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
                        initView();
                    }

                }
            });

        }
    }

    private void initView(){
        GlideUtils.glideLoader(mContext, designerEntity.getDesignerInfoEntity().getIcon(), R.mipmap.loading_img_fail,R.mipmap.loading_img, ivDesignerHeadPic);
        tvDesignerName.setText(designerEntity.getDesignerInfoEntity().getDesname());
//        tvNum.setText("粉丝: " + designerEntity.getDesignerInfoEntity().get + " /  浏览" + designerEntity.getDesignerInfoEntity().getDesname());

    }

    @OnClick({R.id.tvConcern, R.id.tvDesigneForme})
    public void onViewClickedDesignerActivtiy(View view) {
        switch (view.getId()) {
            case R.id.tvConcern:
                HashMap<String, Object> hashMap = new HashMap<String,Object>();
                hashMap.put("", "");


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
                            if(object.getInt("status") == 0){

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
        }
    }
}
