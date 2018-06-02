package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tbs.tbsbusiness.R;

/**
 * 登录页面
 * 分别为手机登录和账号
 */
public class LoginActivity extends AppCompatActivity {
    private String TAG = "";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
