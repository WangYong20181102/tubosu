package com.tbs.tobosupicture.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class OwnerCaseActivity extends BaseActivity {

    @BindView(R.id.reOwenerCaseBack)
    RelativeLayout reOwenerCaseBack;
    @BindView(R.id.ivNoOwenerCaseData)
    ImageView ivNoOwenerCaseData;
    @BindView(R.id.OwenerCaseRecyclerView)
    RecyclerView OwenerCaseRecyclerView;
    @BindView(R.id.OwenerCaseSwipeRefreshLayout)
    SwipeRefreshLayout OwenerCaseSwipeRefreshLayout;

    private int page = 1;
    private int pageSize = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = OwnerCaseActivity.this;
        TAG = OwnerCaseActivity.class.getSimpleName();
        setContentView(R.layout.activity_owner_case);
        ButterKnife.bind(this);
        getDataFromNet();
    }

    private void getDataFromNet() {
        if(Utils.isNetAvailable(mContext)){
            if(Utils.userIsLogin(mContext)){
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", Utils.getDateToken());
                hashMap.put("uid", SpUtils.getUserUid(mContext));
                hashMap.put("page", page);
                hashMap.put("page_size", pageSize);
                HttpUtils.doPost(UrlConstans.SAME_CITY_OWENER_CASE_LIST_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(mContext, "系统繁忙，稍后再试~");
                                ivNoOwenerCaseData.setVisibility(View.VISIBLE);
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
    public void onOwenerCaseActivityViewClicked(View view) {
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
