package com.tbs.tobosutype.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.Util;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SheJiShiActivity extends com.tbs.tobosutype.base.BaseActivity implements View.OnClickListener {
    private Intent dataIntent;
    private String des_id;
    private RelativeLayout relShejishiBack;
    private ImageView shejishiShare;
    private android.support.v7.widget.RecyclerView shejishiRecyclerView;
    private android.support.v4.widget.SwipeRefreshLayout shejishiSwip;



    private int page = 1;
    private int page_size = 10;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_shijishi);

        mContext = SheJiShiActivity.this;
        TAG = SheJiShiActivity.class.getSimpleName();
        bindViews();
        initViews();
        getData();


    }

    private void bindViews(){
        shejishiSwip = (SwipeRefreshLayout) findViewById(R.id.shejishiSwip);
        shejishiRecyclerView = (RecyclerView) findViewById(R.id.shejishiRecyclerView);
        shejishiShare = (ImageView) findViewById(R.id.shejishiShare);
        relShejishiBack = (RelativeLayout) findViewById(R.id.relShejishiBack);

        shejishiShare.setOnClickListener(this);
        relShejishiBack.setOnClickListener(this);


        dataIntent = getIntent();
        des_id = dataIntent.getStringExtra("designer_id");
    }

    private void initViews(){


        Intent it = new Intent(mContext, SheJiShiActivity.class);
        it.putExtra("designer_id", "186922");
        startActivity(it);
    }

    private void getData(){
        if(Util.isNetAvailable(mContext)){
            HashMap<String, Object> hashMap = new HashMap<String,Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("des_id", des_id);
            hashMap.put("page",page);
            hashMap.put("page_size",page_size);
                    OKHttpUtil.post(Constant.SHEJISHI_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "获取设计师信息失败，稍后再试~");
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Util.setErrorLog(TAG, json);

                }
            });


        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shejishiShare:
                Util.setToast(mContext,"分享啦...");
                break;
            case R.id.relShejishiBack:
                finish();
                break;
        }
    }
}
