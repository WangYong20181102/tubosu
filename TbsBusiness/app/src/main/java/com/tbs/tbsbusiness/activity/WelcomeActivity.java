package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;

/**
 * 欢迎页面(启动页面)
 */
public class WelcomeActivity extends BaseActivity {
    private String TAG = "WelcomeActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
