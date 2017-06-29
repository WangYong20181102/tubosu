package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by Mr.Lin on 2017/5/10 15:41.
 */

public class BaseActivity extends AppCompatActivity {
    protected  static final String TAG = BaseActivity.class.getSimpleName();
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
