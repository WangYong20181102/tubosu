package com.tbs.tobosupicture.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tbs.tobosupicture.R;

/**
 * 欢迎启动页 用于加载闪屏
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
