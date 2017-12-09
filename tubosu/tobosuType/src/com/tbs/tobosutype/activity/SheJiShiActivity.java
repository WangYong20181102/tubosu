package com.tbs.tobosutype.activity;


import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;

public class SheJiShiActivity extends com.tbs.tobosutype.base.BaseActivity {
    private Context mContext;
    private String TAG = SheJiShiActivity.class.getSimpleName();
    private android.widget.RelativeLayout relShejishiBack;
    private android.widget.ImageView shejishiShare;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_shijishi);
        mContext = SheJiShiActivity.this;

        bindViews();
        initViews();



    }

    private void bindViews(){
        shejishiShare = (ImageView) findViewById(R.id.shejishiShare);
        relShejishiBack = (RelativeLayout) findViewById(R.id.relShejishiBack);



    }

    private void initViews(){

    }


}
