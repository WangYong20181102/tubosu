package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.config.MyApplication;
import com.tbs.tbsbusiness.util.SpUtil;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initJpush();
    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(mContext);
        SpUtil.setPushRegisterId(MyApplication.getContext(), JPushInterface.getRegistrationID(MyApplication.getContext()));
    }
}
