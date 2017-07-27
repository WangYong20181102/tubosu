package com.tbs.tobosupicture.activity;

import android.os.Bundle;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;

/**
 * 工装 家装 看大图
 * Created by Lie on 2017/7/19.
 */

public class SeeImageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "SeeImageActivity";
        mContext = SeeImageActivity.this;
        setContentView(R.layout.activity_see_image);

    }
}
