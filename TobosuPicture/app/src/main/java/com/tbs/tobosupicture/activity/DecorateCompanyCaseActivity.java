package com.tbs.tobosupicture.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lie on 2017/8/16.
 */

public class DecorateCompanyCaseActivity extends BaseActivity {

    @BindView(R.id.reDecorateCaseBack)
    RelativeLayout reDecorateCaseBack;
    @BindView(R.id.ivNoDecorateCaseData)
    ImageView ivNoDecorateCaseData;
    @BindView(R.id.DecorateCaseRecyclerView)
    RecyclerView DecorateCaseRecyclerView;
    @BindView(R.id.DecorateCaseSwipeRefreshLayout)
    SwipeRefreshLayout DecorateCaseSwipeRefreshLayout;
    @BindView(R.id.tvTiptext)
    TextView tvTiptext;

    private int page = 1;
    private int pageSize = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = DecorateCompanyCaseActivity.this;
        TAG = DecorateCompanyCaseActivity.class.getSimpleName();
        setContentView(R.layout.activity_decorate_company_case);
        ButterKnife.bind(this);
        getDataFromNet();
    }

    private void getDataFromNet() {
        String text = "去网站<p><font color=\\\"#FF9934\\\">上传对应新的案例</p>,可直接在用户端展示自己的案例以及联系方式,获取更多装修资源哦。";
        tvTiptext.setText(Html.fromHtml(text));
        if(Utils.isNetAvailable(mContext)){
            if(Utils.userIsLogin(mContext)){
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", Utils.getDateToken());
                hashMap.put("uid", SpUtils.getUserUid(mContext));
                hashMap.put("page", page);
                hashMap.put("page_size", pageSize);
                HttpUtils.doPost(UrlConstans.SAME_CITY_DECORATE_CASE_LIST_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(mContext, "系统繁忙，稍后再试~");
                                ivNoDecorateCaseData.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Utils.setErrorLog(TAG, json);

                    }
                });
            }else{
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        }

    }

    @OnClick(R.id.reDecorateCaseBack)
    public void onDecorateCompanyCaseActivityViewClicked(View view) {
        switch (view.getId()){
            case R.id.reDecorateCaseBack:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
